package com.ssafy.groupcall;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PreDestroy;

import org.kurento.client.Continuation;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Room implements Closeable {
  private final Logger log = LoggerFactory.getLogger(Room.class);

  private final ConcurrentMap<Integer, UserSession> participants = new ConcurrentHashMap<>(); //key : userId
  private final MediaPipeline pipeline;
  private final int meetingId;

  public int getMeetingId() {
	return meetingId;
}

  public Room(int meetingId, MediaPipeline pipeline) {
    this.meetingId = meetingId;
    this.pipeline = pipeline;
    log.info("ROOM {} has been created", meetingId);
  }

  @PreDestroy
  private void shutdown() {
    this.close();
  }

  public UserSession join(int userId, WebSocketSession session) throws IOException {
    log.info("ROOM {}: adding participant {}", this.meetingId, userId);
    final UserSession participant = new UserSession(userId, this.meetingId, session, this.pipeline);
    joinRoom(participant);
    participants.put(participant.getUserId(), participant);
    sendParticipantNames(participant);
    return participant;
  }
  
  public UserSession join(int userId, String userNickname, WebSocketSession session) throws IOException {
	    log.info("ROOM {}: adding participant {}", this.meetingId, userId);
	    final UserSession participant = new UserSession(userId, this.meetingId, session, this.pipeline);
	    joinRoom(participant);
	    participants.put(participant.getUserId(), participant);
	    sendParticipantNames(participant, userNickname);
	    return participant;
	  }

  public void leave(UserSession user) throws IOException {
    log.debug("PARTICIPANT {}: Leaving room {}", user.getUserId(), this.meetingId);
    this.removeParticipant(user.getUserId());
    user.close();
  }

  private Collection<Integer> joinRoom(UserSession newParticipant) throws IOException {
    final JsonObject newParticipantMsg = new JsonObject();
    newParticipantMsg.addProperty("id", "newParticipantArrived");
    newParticipantMsg.addProperty("userId", newParticipant.getUserId());

    final List<Integer> participantsList = new ArrayList<>(participants.values().size());
    log.debug("ROOM {}: notifying other participants of new participant {}", meetingId,
        newParticipant.getUserId());

    for (final UserSession participant : participants.values()) {
      try {
        participant.sendMessage(newParticipantMsg);
      } catch (final IOException e) {
        log.debug("ROOM {}: participant {} could not be notified", meetingId, participant.getUserId(), e);
      }
      participantsList.add(participant.getUserId());
    }

    return participantsList;
  }

  private void removeParticipant(int userId) throws IOException {
    participants.remove(userId);

    log.debug("ROOM {}: notifying all users that {} is leaving the room", this.meetingId, userId);

    final List<Integer> unnotifiedParticipants = new ArrayList<>();
    final JsonObject participantLeftJson = new JsonObject();
    participantLeftJson.addProperty("id", "participantLeft");
    participantLeftJson.addProperty("userId", userId);
    for (final UserSession participant : participants.values()) {
      try {
        participant.cancelVideoFrom(userId);
        participant.sendMessage(participantLeftJson);
      } catch (final IOException e) {
        unnotifiedParticipants.add(participant.getUserId());
      }
    }

    if (!unnotifiedParticipants.isEmpty()) {
      log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.meetingId,
          unnotifiedParticipants, userId);
    }

  }

  public void sendParticipantNames(UserSession user) throws IOException {

    final JsonArray participantsArray = new JsonArray();
    for (final UserSession participant : this.getParticipants()) {
      if (!participant.equals(user)) {
        final JsonElement participantName = new JsonPrimitive(participant.getUserId());
        participantsArray.add(participantName);
      }
    }
       
    final JsonObject existingParticipantsMsg = new JsonObject();
    existingParticipantsMsg.addProperty("id", "existingParticipants");
    existingParticipantsMsg.add("data", participantsArray);
    existingParticipantsMsg.add("userId", new JsonPrimitive(user.getUserId()));
    existingParticipantsMsg.add("meetingId", new JsonPrimitive(user.getMeetingId()));
    
    log.debug("PARTICIPANT {}: sending a list of {} participants", user.getUserId(),
        participantsArray.size());
    user.sendMessage(existingParticipantsMsg);
  }

  public void sendParticipantNames(UserSession user, String userNickname) throws IOException {

	    final JsonArray participantsArray = new JsonArray();
	    for (final UserSession participant : this.getParticipants()) {
	      if (!participant.equals(user)) {
	        final JsonElement participantName = new JsonPrimitive(participant.getUserId());
	        participantsArray.add(participantName);
	      }
	    }
	    
	    final JsonObject existingParticipantsMsg = new JsonObject();
	    existingParticipantsMsg.addProperty("id", "existingParticipants");
	    existingParticipantsMsg.add("data", participantsArray);
	    existingParticipantsMsg.add("userId", new JsonPrimitive(user.getUserId()));
	    existingParticipantsMsg.add("meetingId", new JsonPrimitive(user.getMeetingId()));
	    existingParticipantsMsg.add("userNickname", new JsonPrimitive(userNickname));
	    
	    
	    log.debug("PARTICIPANT {}: sending a list of {} participants", user.getUserId(),
	        participantsArray.size());
	    user.sendMessage(existingParticipantsMsg);
	  }
  
  public Collection<UserSession> getParticipants() {
    return participants.values();
  }

  public UserSession getParticipant(int userId) {
    return participants.get(userId);
  }

  @Override
  public void close() {
    for (final UserSession user : participants.values()) {
      try {
        user.close();
      } catch (IOException e) {
        log.debug("ROOM {}: Could not invoke close on participant {}", this.meetingId, user.getUserId(),
            e);
      }
    }

    participants.clear();

    pipeline.release(new Continuation<Void>() {

      @Override
      public void onSuccess(Void result) throws Exception {
        log.trace("ROOM {}: Released Pipeline", Room.this.meetingId);
      }

      @Override
      public void onError(Throwable cause) throws Exception {
        log.warn("PARTICIPANT {}: Could not release Pipeline", Room.this.meetingId);
      }
    });

    log.debug("Room {} closed", this.meetingId);
  }

}
