package com.websocket.sockjs.demo.controller;

import com.websocket.sockjs.demo.service.SpittleFeedService;
import com.websocket.sockjs.demo.vo.Greeting;
import com.websocket.sockjs.demo.vo.HelloMessage;
import com.websocket.sockjs.demo.vo.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    @Autowired
    SpittleFeedService spittleFeedService;

    // A1）@MessageMapping注解：表示 handleShout()方法能够处理 指定目的地上到达的消息；
    // A2）这个目的地（消息发送目的地url）就是 "/server/app/hello"，其中 "/app" 是 隐含的 ,"/server" 是 springmvc 项目名称；
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws InterruptedException {
        System.out.println("receiving " + message.getName());
        Thread.sleep(1000); // simulated delay
        System.out.println("connecting successfully.");
        return new Greeting("Hello, " + message.getName() + "!");
    }


    // 订阅的时候调用的，基本是只执行一次的方法，client调用定义在server的该Annotation标注的方法它就会返回结果，不经过代理。
    @SubscribeMapping("/macro")
    public Shout handleSubscription() {
        Shout outgoing = new Shout();
        outgoing.setMessage("Subscribe success!");
        return outgoing;
    }

    // 通过调用SimpMessageSendingOperations.convertAndSend方法发布消息
    @RequestMapping("/dospittle")
    @ResponseBody
    public void doSpillte() {
        Shout outgoing = new Shout();
        outgoing.setMessage("Feed success!");
        spittleFeedService.broadcastSpittle(outgoing);
    }

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    // broadcast = false把避免推送到一个帐号所有的session中
    @MessageMapping("/spittle/other")
    // @SendToUser(value = "/notifications")
    public Greeting greetingToUser(HelloMessage message) throws InterruptedException {
        System.out.println("receiving " + message.getName());
        Thread.sleep(1000); // simulated delay
        System.out.println("connecting successfully.");
        spittleFeedService.broadcastSpittleToUser(new Greeting(),"guest");
        return new Greeting("Hello user, " + message.getName() + "!");
    }

    // broadcast = false把避免推送到一个帐号所有的session中
    @MessageMapping("/spittle/self")
    @SendToUser(value = "/queue/notifications")
    public Greeting greetingToUserSelf(HelloMessage message) throws InterruptedException {
        System.out.println("receiving " + message.getName());
        return new Greeting("Hello self, " + message.getName() + "!");
    }
}
