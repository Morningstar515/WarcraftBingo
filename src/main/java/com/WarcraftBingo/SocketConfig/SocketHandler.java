package com.WarcraftBingo.SocketConfig;

import com.WarcraftBingo.ChatRoomFunctions.JoinMessage;
import com.WarcraftBingo.Controllers.Auxillary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Scope("singleton")
@Component
public class SocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession> > activeRooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public SocketHandler() {
        System.out.println("SocketHandler instance created: ---" + System.identityHashCode(this) + "---");
    }

    /* Handles room connections*/
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JoinMessage joinMsg = objectMapper.readValue(payload, JoinMessage.class);
        String roomCode = joinMsg.getRoomCode();
        if ("JOIN".equals(joinMsg.getType()) && roomCode != null) {

            // Add session(individual connection) to the room
            for (String key : activeRooms.keySet()){
                if (key.equals(roomCode)){
                    activeRooms.get(key).add(session);
                }
            }
            printRooms();
            System.out.println("Room Code: " + roomCode + " Session: " + session.getId());

            // Notify the session about the successful join
            session.sendMessage(new TextMessage("Joined room: " + roomCode));
        }

        /* Must be start of new room */
        else {
            activeRooms.computeIfAbsent(roomCode, k -> new HashSet<>()).add(session);
            System.out.println("New Room Made --> Socket: " + this);
            System.out.println(System.identityHashCode(this) + "]]]]] ");

            printRooms();
        }
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Session added: " + session.getId());
        System.out.println();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try{
            activeRooms.values().remove(session);
            System.out.println("Session removed: " + session.getId());
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Session not found");
        }
    }

    public void printRooms() {

        this.activeRooms.forEach((roomCode, sessions) -> {
            System.out.println("Room: " + roomCode + " has " + sessions.size() + " participants:");
            for (WebSocketSession session : sessions) {
                System.out.println(" - Session ID: " + session.getId());
            }
        });
    }


    public void broadcast(String room, String message) {
        System.out.println(this.activeRooms + ">>>");

        Set<WebSocketSession> selectedRoom = this.activeRooms.get(room);
        printRooms();
        System.out.println("------");
        if (selectedRoom != null){
            for (WebSocketSession session : selectedRoom) {

                if (session != null && session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Session not found or closed: " + session.getId());
                }
            }
        }
        else{
            System.out.println("Room not found: " + room);
        }

    }


}
