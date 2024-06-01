package com.WarcraftBingo.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@org.springframework.stereotype.Controller


@RestController
public class GameController {

    @GetMapping("/test")
    public String test(){
        return "hello there";
    }
}
