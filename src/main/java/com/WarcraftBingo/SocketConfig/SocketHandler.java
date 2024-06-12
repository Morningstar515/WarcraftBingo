package com.WarcraftBingo.SocketConfig;

import com.WarcraftBingo.ChatRoomFunctions.JoinMessage;
import com.WarcraftBingo.ChatRoomFunctions.RoomMembers;
import com.WarcraftBingo.Controllers.Auxillary;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Scope("singleton")
@Component
public class SocketHandler extends TextWebSocketHandler {
    private final Map<String, HashMap<WebSocketSession,String> > activeRooms = new ConcurrentHashMap<>();
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
        String username = joinMsg.getUsername();

        if ("JOIN".equals(joinMsg.getType()) && roomCode != null) {

            // Add session(individual connection) to the room
            for (String key : activeRooms.keySet()){
                if (key.equals(roomCode)){
                    activeRooms.get(key).put(session,username);
                }
            }
            printRooms();
            System.out.println("Room Code: " + roomCode + " Session: " + session.getId());

            // Collect usernames into a list
            List<String> usernames = new ArrayList<>(activeRooms.get(roomCode).values());

            // Convert list of usernames to JSON
            String jsonUsernames = objectMapper.writeValueAsString(usernames);

            for (WebSocketSession key : activeRooms.get(roomCode).keySet()) {
                System.out.println("sending message to " + key.getId());
                System.out.println("Usernames: " + jsonUsernames);
                key.sendMessage(new TextMessage(jsonUsernames));
            }
        }


        /* Must be start of new room */
        else {
            activeRooms.computeIfAbsent(roomCode, k -> new HashMap<>()).put(session,username);

            // Collect usernames into a list
            List<String> usernames = new ArrayList<>(activeRooms.get(roomCode).values());
            // Convert list of usernames to JSON
            String jsonUsernames = objectMapper.writeValueAsString(usernames);

            session.sendMessage(new TextMessage(jsonUsernames));
            System.out.println("New Room Made --> Socket: " + this);
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
            //Save roomCode of room that gets deleted and delete room from activeRooms map
            String removed = "";
            for(String room : activeRooms.keySet()){
                if(activeRooms.get(room).containsKey(session)){
                    activeRooms.get(room).remove(session);
                    removed = room;
                }
            }

            //Update other sessions member lists
            for (WebSocketSession sessionId : activeRooms.get(removed).keySet()){

                // Collect usernames into a list & Convert to JSON
                List<String> usernames = new ArrayList<>(activeRooms.get(removed).values());
                String jsonUsernames = objectMapper.writeValueAsString(usernames);

                sessionId.sendMessage(new TextMessage(jsonUsernames));
            }
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
            for (WebSocketSession session : sessions.keySet()) {
                System.out.println(" - Session ID: " + session.getId());
                System.out.println(" - User: " + activeRooms.get(roomCode).get(session));
            }
        });
    }


    public void broadcastWin(String room,String username, String message) {
        HashMap<WebSocketSession,String> selectedRoom = this.activeRooms.get(room);
        if (selectedRoom != null){
            for (WebSocketSession session : selectedRoom.keySet()) {

                if (session != null && session.isOpen()) {
                    try {
                        String jsonWarningMessage = objectMapper.writeValueAsString(username + " " + message);
                        session.sendMessage(new TextMessage(jsonWarningMessage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    assert session != null;
                    System.out.println("Session not found or closed: " + session.getId());
                }
            }
        }
        else{
            System.out.println("Room not found: " + room);
        }
    }

    public List <String> getMembers(String room) {
        HashMap<WebSocketSession,String> selectedRoom = activeRooms.get(room);
        List<String> roomMembers = new ArrayList<>();
        for (String user : selectedRoom.values()){
            roomMembers.add(user);
        }
        return roomMembers;
    }
}
