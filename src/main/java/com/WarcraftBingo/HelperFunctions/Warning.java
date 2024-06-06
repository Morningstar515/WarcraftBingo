package com.WarcraftBingo.HelperFunctions;

import com.WarcraftBingo.SocketConfig.SocketHandler;

public class Warning {
    private String message = "";
    private String roomCode = "";

    public Warning(String room,String message) {
        this.message = message;
        this.roomCode = room;
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

    public void setRoom(String room) {
        this.roomCode = room;
    }
}
