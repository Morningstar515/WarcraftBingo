package com.WarcraftBingo.Controllers;

import com.WarcraftBingo.HelperFunctions.RoomFunctions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@org.springframework.stereotype.Controller


@RestController
public class GameController {

    @GetMapping("/test")
    public String test(){
        return "Someone missed the bridge jump and is now in the lava";
    }

    @GetMapping("/generateRoomCode")
    public String generateRoomCode(){
        return RoomFunctions.generateCode();
    }


}
