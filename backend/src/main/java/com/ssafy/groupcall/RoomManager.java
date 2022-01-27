package com.ssafy.groupcall;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomManager {

  private final Logger log = LoggerFactory.getLogger(RoomManager.class);

  @Autowired
  private KurentoClient kurento;

  private final ConcurrentMap<Integer, Room> rooms = new ConcurrentHashMap<>(); //key : meetingId

  /**
   * Looks for a room in the active room list.
   *
   * @param roomName
   *          the name of the room
   * @return the room if it was already created, or a new one if it is the first time this room is
   *         accessed
   */
  public Room getRoom(int meetingId) {
    log.debug("Searching for room {}", meetingId);
    Room room = rooms.get(meetingId);

    if (room == null) {
      log.debug("Room {} not existent. Will create now!", meetingId);
      room = new Room(meetingId, kurento.createMediaPipeline());
      rooms.put(meetingId, room);
    }
    log.debug("Room {} found!", meetingId);
    return room;
  }

  /**
   * Removes a room from the list of available rooms.
   *
   * @param room
   *          the room to be removed
   */
  public void removeRoom(Room room) {
    this.rooms.remove(room.getMeetingId());
    room.close();
    log.info("Room {} removed and closed", room.getMeetingId());
  }

}