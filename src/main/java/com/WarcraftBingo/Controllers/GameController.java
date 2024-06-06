package com.WarcraftBingo.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import com.WarcraftBingo.HelperFunctions.RoomFunctions;
import com.WarcraftBingo.HelperFunctions.Warning;
import com.WarcraftBingo.SocketConfig.SocketHandler;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller


//@Component
@RestController
public class GameController {

    private final SocketHandler socketHandler;

    @Autowired
    public GameController(SocketHandler socket){
        this.socketHandler = socket;
        System.out.println(System.identityHashCode(this.socketHandler)+ ";;;;;;;");
    }

    @GetMapping("/test")
    public String test(){
        return "Someone missed the bridge jump and is now in the lava";
    }

    @GetMapping("/generateRoomCode")
    public String generateRoomCode(){
        return RoomFunctions.generateCode();
    }

    @PostMapping("/warning")
    public void warning(@RequestBody Warning warning){

        String room = warning.getRoomCode();
        String message = warning.getMessage();
        socketHandler.broadcast(room,message);
    }


}
