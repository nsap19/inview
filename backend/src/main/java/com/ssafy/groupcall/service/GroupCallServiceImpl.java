package com.ssafy.groupcall.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import org.kurento.client.EndOfStreamEvent;
import org.kurento.client.ErrorEvent;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidateFoundEvent;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.MediaType;
import org.kurento.client.PausedEvent;
import org.kurento.client.PlayerEndpoint;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.RecordingEvent;
import org.kurento.client.StoppedEvent;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ssafy.api.request.ArchiveRegisterPostReq;
import com.ssafy.api.service.ArchiveService;
import com.ssafy.api.service.meeting.MeetingInsideService;
import com.ssafy.common.util.ArchiveUtil;
import com.ssafy.db.entity.ArchiveType;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.meeting.Meeting;
import com.ssafy.db.repository.UserRepositorySupport;
import com.ssafy.groupcall.CallHandler;
import com.ssafy.groupcall.RoomManager;
import com.ssafy.groupcall.UserRegistry;
import com.ssafy.groupcall.UserSession;

@Service
public class GroupCallServiceImpl implements GroupCallService {
	private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	MeetingInsideService meetingInsideService;

	@Autowired
	ArchiveService arhciveService;

	@Autowired
	ArchiveUtil archiveUtil;
	
	@Autowired
	private KurentoClient kurento;

	public void start(UserSession userSession, final WebSocketSession session, JsonObject jsonMessage) {
		try {
			// 1. Media logic (webRtcEndpoint in loopback)
			MediaPipeline pipeline = userSession.getPipeline();
			WebRtcEndpoint webRtcEndpoint = userSession.getOutgoingWebRtcPeer();
			webRtcEndpoint.connect(webRtcEndpoint);

			MediaProfileSpecType profile = getMediaProfileFromMessage(jsonMessage);

			User user = userRepositorySupport.findUserByUserId(userSession.getUserId());
			String meetingId = String.valueOf(userSession.getMeetingId());
			ArchiveType archiveType = ArchiveType.VIDEO;
			String savepath = archiveUtil.getSavepath(archiveType, meetingId);
			String filename = archiveUtil.getFilename(archiveType, user, "", ".webm");
			String filepath = archiveUtil.getFilepath(archiveType, savepath, filename);
			
			archiveUtil.InsertToArchive(archiveType, meetingId, filepath, filename, user);
			userSession.setFilePath(filepath);
			RecorderEndpoint recorder = new RecorderEndpoint.Builder(pipeline, filepath).withMediaProfile(profile)
					.build();

			recorder.addRecordingListener(new EventListener<RecordingEvent>() {
				@Override
				public void onEvent(RecordingEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "recording");
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			});

			recorder.addStoppedListener(new EventListener<StoppedEvent>() {
				@Override
				public void onEvent(StoppedEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "stopped");
					System.out.println(recorder.getUri());
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			});

			recorder.addPausedListener(new EventListener<PausedEvent>() {
				@Override
				public void onEvent(PausedEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "paused");
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			});

			connectAccordingToProfile(webRtcEndpoint, recorder, profile);

			// 2. Store user session
			userSession.setRecorderEndpoint(recorder);
			webRtcEndpoint.gatherCandidates();

			recorder.record();
		} catch (Throwable t) {
			log.error("Start error", t);
			sendError(session, t.getMessage());
		}
	}

	private MediaProfileSpecType getMediaProfileFromMessage(JsonObject jsonMessage) {

		MediaProfileSpecType profile = MediaProfileSpecType.WEBM;
		return profile;
	}

	private void connectAccordingToProfile(WebRtcEndpoint webRtcEndpoint, RecorderEndpoint recorder,
			MediaProfileSpecType profile) {
		switch (profile) {
		case WEBM:
			webRtcEndpoint.connect(recorder, MediaType.AUDIO);
			webRtcEndpoint.connect(recorder, MediaType.VIDEO);
			break;
		case WEBM_AUDIO_ONLY:
			webRtcEndpoint.connect(recorder, MediaType.AUDIO);
			break;
		case WEBM_VIDEO_ONLY:
			webRtcEndpoint.connect(recorder, MediaType.VIDEO);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported profile for this tutorial: " + profile);
		}
	}

	public void play(UserSession userSession, final WebSocketSession session, JsonObject jsonMessage) {
		try {
			// 1. Media logic
			final MediaPipeline pipeline = kurento.createMediaPipeline();
			WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
			String filePath = userSession.getFilePath();
			PlayerEndpoint player = new PlayerEndpoint.Builder(pipeline, filePath).build();
			player.connect(webRtcEndpoint);
			System.out.println(player.getVideoInfo().toString());

			// Player listeners
			player.addErrorListener(new EventListener<ErrorEvent>() {
				@Override
				public void onEvent(ErrorEvent event) {
					log.info("ErrorEvent for session '{}': {}", session.getId(), event.getDescription());
					sendPlayEnd(session, pipeline);
				}
			});
			player.addEndOfStreamListener(new EventListener<EndOfStreamEvent>() {
				@Override
				public void onEvent(EndOfStreamEvent event) {
					log.info("EndOfStreamEvent for session '{}'", session.getId());
					sendPlayEnd(session, pipeline);
				}
			});

			// 2. Store user session
			userSession.setMediaPipeline(pipeline);
			userSession.setOutgoingMedia(webRtcEndpoint);

			// 3. SDP negotiation
			String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
			String sdpAnswer = webRtcEndpoint.processOffer(sdpOffer);

			JsonObject response = new JsonObject();
			response.addProperty("id", "playResponse");
			response.addProperty("sdpAnswer", sdpAnswer);

			// 4. Gather ICE candidates
			webRtcEndpoint.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

				@Override
				public void onEvent(IceCandidateFoundEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate");
					response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			});

			// 5. Play recorded stream
			player.play();

			synchronized (session) {
				session.sendMessage(new TextMessage(response.toString()));
			}

			webRtcEndpoint.gatherCandidates();
		} catch (Throwable t) {
			log.error("Play error", t);
			sendError(session, t.getMessage());
		}
	}

	public void sendPlayEnd(WebSocketSession session, MediaPipeline pipeline) {
		try {
			JsonObject response = new JsonObject();
			response.addProperty("id", "playEnd");
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.error("Error sending playEndOfStream message", e);
		}
		// Release pipeline
		pipeline.release();
	}

	private void sendError(WebSocketSession session, String message) {
		try {
			JsonObject response = new JsonObject();
			response.addProperty("id", "error");
			response.addProperty("message", message);
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.error("Exception sending message", e);
		}
	}
}
