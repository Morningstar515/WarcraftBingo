package com.WarcraftBingo.ChatRoomFunctions;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {

    private final String roomCode;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public ChatRoom(String roomCode) {
        this.roomCode = roomCode;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
        // Notify other participants
        sendMessageToAll(new TextMessage("A new user has joined the room: " + roomCode));
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
        // Notify other participants
        sendMessageToAll(new TextMessage("A user has left the room: " + roomCode));
    }

    public void sendMessageToAll(TextMessage message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
