package com.WarcraftBingo.ChatRoomFunctions;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

public class Room {
    private HashMap <WebSocketSession,String> members;
    private String boardType;
    private String roomCode;

    public Room(HashMap<WebSocketSession, String> members, String boardType, String roomCode) {
        this.members = members;
        this.boardType = boardType;
        this.roomCode = roomCode;
    }

    public HashMap<WebSocketSession, String> getMembers() {
        return members;
    }

    public void setMembers(WebSocketSession session, String members) {
        this.members.put(session, members);
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void deleteMember(WebSocketSession session){
        this.members.remove(session);
    }
}


