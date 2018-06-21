package com.websocket.sockjs.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketController {

    @RequestMapping(value = "/index")
    public String say() {
        return "index";
    }
}
