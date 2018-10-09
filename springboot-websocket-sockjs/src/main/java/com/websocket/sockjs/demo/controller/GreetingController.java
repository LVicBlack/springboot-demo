package com.websocket.sockjs.demo.controller;

import com.websocket.sockjs.demo.config.SocketSessionRegistry;
import com.websocket.sockjs.demo.vo.Greeting;
import com.websocket.sockjs.demo.vo.HelloMessage;
import com.websocket.sockjs.demo.vo.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Controller
public class GreetingController {

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    SimpMessagingTemplate messaging;

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
        messaging.convertAndSend("/topic/spittlefeed", outgoing);
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
        convertAndSendToUser(new Greeting(), "guest");
        return new Greeting("Hello user, " + message.getName() + "!");
    }

    // broadcast = false把避免推送到一个帐号所有的session中
    @MessageMapping("/spittle/self")
    @SendToUser(value = "/queue/notifications")
    public Greeting greetingToUserSelf(HelloMessage message) {
        System.out.println("receiving " + message.getName());
        return new Greeting("Hello self, " + message.getName() + "!");
    }

    // broadcast = false把避免推送到一个帐号所有的session中
    @Scheduled(fixedRate = 1000)
    @SendToUser("/topic/callback")
    public void callback() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messaging.convertAndSend("/topic/callback", df.format(new Date()));
    }

    void convertAndSendToUser(Greeting greeting, String username) {
        String sessionId = webAgentSessionRegistry.getSessionIds(username).stream().findFirst().get();
        // 模式2 - 获取id、添加MessageHeaders
        messaging.convertAndSendToUser(sessionId, "/queue/notifications", new Greeting("I am a msg from Others."), createHeaders(sessionId));
        // 模式1 - 需改写StompHeaderAccessor中User信息
//        messaging.convertAndSendToUser(username, "/queue/notifications", new Greeting("I am a msg from SubscribeMapping('/macro')."));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
