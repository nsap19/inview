package com.ssafy.groupcall;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

/**
 * Map of users registered in the system. This class has a concurrent hash map to store users, using
 * its name as key in the map.
 * 
 */
public class UserRegistry {

  private final ConcurrentHashMap<String, UserSession> usersByName = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, UserSession> usersBySessionId = new ConcurrentHashMap<>();

  public void register(UserSession user) {
    usersByName.put(user.getName(), user);
    usersBySessionId.put(user.getSession().getId(), user);
  }

  public UserSession getByName(String name) {
    return usersByName.get(name);
  }

  public UserSession getBySession(WebSocketSession session) {
    return usersBySessionId.get(session.getId());
  }

  public boolean exists(String name) {
    return usersByName.keySet().contains(name);
  }

  public UserSession removeBySession(WebSocketSession session) {
    final UserSession user = getBySession(session);
    usersByName.remove(user.getName());
    usersBySessionId.remove(session.getId());
    return user;
  }

}