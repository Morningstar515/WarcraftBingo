package com.WarcraftBingo.SocketConfig;


import com.WarcraftBingo.ChatRoomFunctions.JoinMessage;
import com.WarcraftBingo.ChatRoomFunctions.Room;
import com.WarcraftBingo.ChatRoomFunctions.RoomMembers;
import com.WarcraftBingo.Controllers.Auxillary;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
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
    private final Map<String, Room> activeRooms = new ConcurrentHashMap<>();
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
        String boardType = joinMsg.getBoardType();
        System.out.println(boardType);
        if ("JOIN".equals(joinMsg.getType()) && roomCode != null) {

            // Add session(individual connection) to the room
            for (String key : activeRooms.keySet()){
                if (key.equals(roomCode)){
                    activeRooms.get(key).setMembers(session,username);
                    boardType = activeRooms.get(key).getBoardType();
                }
            }
            printRooms();
            System.out.println("Room Code: " + roomCode + " Session: " + session.getId());

            // Collect usernames into a list
            List<String> usernames = new ArrayList<>(activeRooms.get(roomCode).getMembers().values());
            // Convert list of usernames to JSON
            String jsonUsernames = objectMapper.writeValueAsString(usernames);

            JsonObject obj = Json.createObjectBuilder()
                    .add("Usernames", jsonUsernames)
                    .add("Room", roomCode)
                    .add("BoardType", boardType)
                    .build();

            String json = objectMapper.writeValueAsString(obj);

            for (WebSocketSession key : activeRooms.get(roomCode).getMembers().keySet()) {
                System.out.println("sending message to " + key.getId());
                System.out.println("Usernames: " + jsonUsernames);
                key.sendMessage(new TextMessage(json));
            }
        }


        /* Must be start of new room */
        else {
            Room newRoom = new Room(new HashMap<>(),boardType,roomCode);
            newRoom.setMembers(session,username);
            if(!activeRooms.containsKey(roomCode)){
                activeRooms.put(roomCode,newRoom);
            }

            // Collect usernames into a list
            List<String> usernames = new ArrayList<>(activeRooms.get(roomCode).getMembers().values());
            // Convert list of usernames to JSON
            String jsonUsernames = objectMapper.writeValueAsString(usernames);


            JsonObject obj = Json.createObjectBuilder()
                    .add("Usernames", jsonUsernames)
                    .add("Room", roomCode)
                    .add("BoardType", boardType)
                    .build();

            String json = objectMapper.writeValueAsString(obj);


            session.sendMessage(new TextMessage(json));
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
                if(activeRooms.get(room).getMembers().containsKey(session)){
                    activeRooms.get(room).deleteMember(session);
                    removed = room;
                }
            }

            //Update other sessions member lists
            for (WebSocketSession sessionId : activeRooms.get(removed).getMembers().keySet()){

                // Collect usernames into a list & Convert to JSON
                List<String> usernames = new ArrayList<>(activeRooms.get(removed).getMembers().values());
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

        this.activeRooms.forEach((roomCode, room) -> {
            System.out.println("Room: " + roomCode + " has " + room.getMembers().size() + " participants:");
            for (WebSocketSession session : room.getMembers().keySet()) {
                System.out.println(" - Session ID: " + session.getId());
                System.out.println(" - User: " + activeRooms.get(roomCode).getMembers().get(session));
            }
        });
    }


    public void broadcastWin(String room,String username, String message, boolean win) {
        HashMap<WebSocketSession,String> selectedRoom = this.activeRooms.get(room).getMembers();
        if (selectedRoom != null){
            for (WebSocketSession session : selectedRoom.keySet()) {

                if (session != null && session.isOpen()) {
                    try {

                        JsonObject obj = Json.createObjectBuilder()
                                .add("Username", username)
                                .add("Message", message)
                                .add("Win",win)
                                .build();

                        String jsonWarningMessage = objectMapper.writeValueAsString(obj);
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

    public Map<String, Room> getActiveRooms() {
        return activeRooms;
    }

    public List <String> getMembers(String room) {
        HashMap<WebSocketSession,String> selectedRoom = activeRooms.get(room).getMembers();
        List<String> roomMembers = new ArrayList<>();
        for (String user : selectedRoom.values()){
            roomMembers.add(user);
        }
        return roomMembers;
    }
}
