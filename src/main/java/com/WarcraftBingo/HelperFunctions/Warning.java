package com.WarcraftBingo.HelperFunctions;

import com.WarcraftBingo.SocketConfig.SocketHandler;

public class Warning {
    private String message = "";
    private String roomCode = "";
    private String username = "";
    private String oneSpaceAway = "";

    public Warning(String room,String username,String message, String oneSpaceAway) {
        this.message = message;
        this.roomCode = room;
        this.username = username;
        this.oneSpaceAway = oneSpaceAway;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOneSpaceAway() {
        return oneSpaceAway;
    }

    public void setOneSpaceAway(String oneSpaceAway) {
        this.oneSpaceAway = oneSpaceAway;
    }
}
