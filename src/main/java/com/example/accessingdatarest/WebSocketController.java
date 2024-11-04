package com.example.accessingdatarest;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/personDelta")
    @SendTo("/topic/personDelta")
    public String sendPersonDelta(String message) {
        return message;
    }
}
