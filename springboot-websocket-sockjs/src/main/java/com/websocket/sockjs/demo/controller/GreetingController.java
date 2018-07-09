package com.websocket.sockjs.demo.controller;

import com.websocket.sockjs.demo.vo.Greeting;
import com.websocket.sockjs.demo.vo.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

public class GreetingController {
    //    A1）@MessageMapping注解：表示 handleShout()方法能够处理 指定目的地上到达的消息；
//    A2）这个目的地（消息发送目的地url）就是 "/server/app/hello"，其中 "/app" 是 隐含的 ,"/server" 是 springmvc 项目名称；
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws InterruptedException {
        System.out.println("receiving " + message.getName());
        Thread.sleep(1000); // simulated delay
        System.out.println("connecting successfully.");
        return new Greeting("Hello, " + message.getName() + "!");
    }

//
//    @SubscribeMapping({"/marco"})
//    public Shout handleSubscription() {
//        Shout outgoing = new Shout();
//        outgoing.setMessage("Polo!");
//        return outgoing;
//
//    }
}
