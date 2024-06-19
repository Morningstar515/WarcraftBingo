package com.WarcraftBingo.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import com.WarcraftBingo.HelperFunctions.RoomFunctions;
import com.WarcraftBingo.HelperFunctions.Warning;
import com.WarcraftBingo.SocketConfig.SocketHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Controller


//@Component
@RestController
public class GameController {

    private final SocketHandler socketHandler;

    @Autowired
    public GameController(SocketHandler socket){
        this.socketHandler = socket;
    }


    @PostMapping("/getBoardType")
    public String getBoardType(@RequestBody Warning warning){
        System.out.println(warning.getRoomCode() + "<");
        System.out.println(socketHandler.getActiveRooms().get(warning.getRoomCode()).getBoardType());
        return socketHandler.getActiveRooms().get(warning.getRoomCode()).getBoardType();
    }

    @GetMapping("/generateRoomCode")
    public String generateRoomCode(){
        return RoomFunctions.generateCode();
    }


    @PostMapping("/members")
    public List<String> membersList(@RequestBody Warning warning){      /* Using warning object to get room code */
        return socketHandler.getMembers(warning.getRoomCode());
    }


    @PostMapping("/warning")
    public void warning(@RequestBody Warning warning){
        String room = warning.getRoomCode();
        String message = warning.getMessage();
        String username = warning.getUsername();
        boolean bingo = warning.getBingo();
        socketHandler.broadcastWin(room,username,message,bingo);
    }


}
