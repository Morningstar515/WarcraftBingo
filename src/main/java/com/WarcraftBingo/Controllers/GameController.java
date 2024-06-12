package com.WarcraftBingo.Controllers;
import com.WarcraftBingo.ChatRoomFunctions.RoomMembers;
import org.springframework.beans.factory.annotation.Autowired;
import com.WarcraftBingo.HelperFunctions.RoomFunctions;
import com.WarcraftBingo.HelperFunctions.Warning;
import com.WarcraftBingo.SocketConfig.SocketHandler;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/test")
    public String test(){
        return "Someone missed the bridge jump and is now in the lava";
    }

    @GetMapping("/generateRoomCode")
    public String generateRoomCode(){
        return RoomFunctions.generateCode();
    }

    @PostMapping("/members")
    public List<String> membersList(@RequestBody Warning warning){      /* Using warning object to get room code */
        System.out.println(socketHandler.getMembers(warning.getRoomCode()) + "????????");
        return socketHandler.getMembers(warning.getRoomCode());
    }


    @PostMapping("/warning")
    public void warning(@RequestBody Warning warning){
        String room = warning.getRoomCode();
        String message = warning.getMessage();
        String username = warning.getUsername();

        String oneSpaceCheck = warning.getOneSpaceAway();
        socketHandler.broadcastWin(room,username,message);
    }


}
