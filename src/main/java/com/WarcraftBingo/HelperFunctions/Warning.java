package com.WarcraftBingo.HelperFunctions;

public class Warning {
    private String message = "";
    private String roomCode = "";
    private String username = "";
    private Boolean bingo = false;

    public Warning(String room,String username,String message) {
        this.message = message;
        this.roomCode = room;
        this.username = username;
        this.bingo = false;
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

    public boolean getBingo() {
        return bingo;
    }

    public void setBingo() {
        this.bingo = !this.bingo;
    }
}
