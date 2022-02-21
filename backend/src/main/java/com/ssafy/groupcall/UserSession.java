package com.ssafy.groupcall;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.kurento.client.Continuation;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.IceCandidateFoundEvent;
import org.kurento.client.ListenerSubscription;
import org.kurento.client.MediaPipeline;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.StoppedEvent;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

public class UserSession implements Closeable {

	private static final Logger log = LoggerFactory.getLogger(UserSession.class);

	private final int userId;
	private final int meetingId;
	private final WebSocketSession session;
	private String filePath;
	private MediaPipeline pipeline;
	private WebRtcEndpoint outgoingMedia;
	private RecorderEndpoint recorderEndpoint;
	private String nickname;

	private final ConcurrentMap<Integer, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

	public UserSession(int userId, String userNickname, int meetingId, WebSocketSession session,
			MediaPipeline pipeline) {
		this.pipeline = pipeline;
		this.userId = userId;
		this.session = session;
		this.meetingId = meetingId;
		this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
		this.nickname = userNickname;
		this.outgoingMedia.setTurnUrl("myuser:mypassword@172.26.1.220:3478");

		this.outgoingMedia.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

			@Override
			public void onEvent(IceCandidateFoundEvent event) {
				JsonObject response = new JsonObject();
				response.addProperty("id", "iceCandidate");
				response.addProperty("userId", userId);
				response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
				try {
					synchronized (session) {
						session.sendMessage(new TextMessage(response.toString()));
					}
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			}
		});
		
	}

	public int getUserId() {
		return userId;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public MediaPipeline getPipeline() {
		return pipeline;
	}

	public WebRtcEndpoint getOutgoingWebRtcPeer() {
		return outgoingMedia;
	}

	public void setMediaPipeline(MediaPipeline pipeline) {
		this.pipeline = pipeline;
	}

	public void setOutgoingMedia(WebRtcEndpoint outgoingMedia) {
		this.outgoingMedia = outgoingMedia;
	}

	public void setRecorderEndpoint(RecorderEndpoint recorderEndpoint) {
		this.recorderEndpoint = recorderEndpoint;
	}

	/**
	 * 현재 접속되어있는 미팅 id
	 */
	public int getMeetingId() {
		return meetingId;
	}
	
	public String getNickname() {
		return nickname;
	}


	public void receiveVideoFrom(UserSession sender, String sdpOffer) throws IOException {
		log.info("USER {}: connecting with {} in room {}", this.userId, sender.getUserId(), this.meetingId);

		log.trace("USER {}: SdpOffer for {} is {}", this.userId, sender.getUserId(), sdpOffer);

		final String ipSdpAnswer = this.getEndpointForUser(sender).processOffer(sdpOffer);
		final JsonObject scParams = new JsonObject();
		scParams.addProperty("id", "receiveVideoAnswer");
		scParams.addProperty("userId", sender.getUserId());
		scParams.addProperty("sdpAnswer", ipSdpAnswer);

		log.trace("USER {}: SdpAnswer for {} is {}", this.userId, sender.getUserId(), ipSdpAnswer);
		this.sendMessage(scParams);
		log.debug("gather candidates");
		this.getEndpointForUser(sender).gatherCandidates();
	}

	public WebRtcEndpoint getEndpointForUser(final UserSession sender) {
		if (sender.getUserId() == userId) {
			log.debug("PARTICIPANT {}: configuring loopback", this.userId);
			return outgoingMedia;
		}

		log.debug("PARTICIPANT {}: receiving video from {}", this.userId, sender.getUserId());

		WebRtcEndpoint incoming = incomingMedia.get(sender.getUserId());
		if (incoming == null) {
			log.debug("PARTICIPANT {}: creating new endpoint for {}", this.userId, sender.getUserId());
			incoming = new WebRtcEndpoint.Builder(pipeline).build();

			incoming.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

				@Override
				public void onEvent(IceCandidateFoundEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate");
					response.addProperty("userId", sender.getUserId());
					response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						log.debug(e.getMessage());
					}
				}
			});

			incomingMedia.put(sender.getUserId(), incoming);
		}

		log.debug("PARTICIPANT {}: obtained endpoint for {}", this.userId, sender.getUserId());
		sender.getOutgoingWebRtcPeer().connect(incoming);

		return incoming;
	}

	public void cancelVideoFrom(final UserSession sender) {
		this.cancelVideoFrom(sender.getUserId());
	}

	public void cancelVideoFrom(final int senderId) {
		log.debug("PARTICIPANT {}: canceling video reception from {}", this.userId, senderId);
		final WebRtcEndpoint incoming = incomingMedia.remove(senderId);

		log.debug("PARTICIPANT {}: removing endpoint for {}", this.userId, senderId);
		incoming.release(new Continuation<Void>() {
			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace("PARTICIPANT {}: Released successfully incoming EP for {}", UserSession.this.userId,
						senderId);
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn("PARTICIPANT {}: Could not release incoming EP for {}", UserSession.this.userId, senderId);
			}
		});
	}

	@Override
	public void close() throws IOException {
		log.debug("PARTICIPANT {}: Releasing resources", this.userId);
		for (final Integer remoteParticipantName : incomingMedia.keySet()) {

			log.trace("PARTICIPANT {}: Released incoming EP for {}", this.userId, remoteParticipantName);

			final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantName);

			ep.release(new Continuation<Void>() {

				@Override
				public void onSuccess(Void result) throws Exception {
					log.trace("PARTICIPANT {}: Released successfully incoming EP for {}", UserSession.this.userId,
							remoteParticipantName);
				}

				@Override
				public void onError(Throwable cause) throws Exception {
					log.warn("PARTICIPANT {}: Could not release incoming EP for {}", UserSession.this.userId,
							remoteParticipantName);
				}
			});
		}

		outgoingMedia.release(new Continuation<Void>() {

			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace("PARTICIPANT {}: Released outgoing EP", UserSession.this.userId);
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn("USER {}: Could not release outgoing EP", UserSession.this.userId);
			}
		});
	}

	public void sendMessage(JsonObject message) throws IOException {
		log.debug("USER {}: Sending message {}", userId, message);
		synchronized (session) {
			session.sendMessage(new TextMessage(message.toString()));
		}
	}

	public void addCandidate(IceCandidate candidate, int userId) {
		if (this.userId == userId) {
			outgoingMedia.addIceCandidate(candidate);
		} else {
			WebRtcEndpoint webRtc = incomingMedia.get(userId);
			if (webRtc != null) {
				webRtc.addIceCandidate(candidate);
			}
		}
	}

	public void stop() {
		if (recorderEndpoint != null) {
			final CountDownLatch stoppedCountDown = new CountDownLatch(1);
			ListenerSubscription subscriptionId = recorderEndpoint
					.addStoppedListener(new EventListener<StoppedEvent>() {

						@Override
						public void onEvent(StoppedEvent event) {
							stoppedCountDown.countDown();
						}
					});
			recorderEndpoint.stop();
			try {
				if (!stoppedCountDown.await(5, TimeUnit.SECONDS)) {
					log.error("Error waiting for recorder to stop");
				}
			} catch (InterruptedException e) {
				log.error("Exception while waiting for state change", e);
			}
			recorderEndpoint.removeStoppedListener(subscriptionId);
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + meetingId;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof UserSession)) {
			return false;
		}
		UserSession other = (UserSession) obj;
		boolean eq = userId == other.userId;
		eq &= meetingId == other.meetingId;
		return eq;
	}

}