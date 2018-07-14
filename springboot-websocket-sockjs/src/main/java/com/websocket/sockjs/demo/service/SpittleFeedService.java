package com.websocket.sockjs.demo.service;

import com.websocket.sockjs.demo.vo.Greeting;
import com.websocket.sockjs.demo.vo.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpittleFeedService {

    private SimpMessageSendingOperations simpMessage;

    private SimpMessagingTemplate messaging;

    @Autowired
    public SpittleFeedService(SimpMessageSendingOperations simpMessage, SimpMessagingTemplate messaging) {
        this.simpMessage = simpMessage;
        this.messaging = messaging;
    }

    public void broadcastSpittle(Shout shout) {
        simpMessage.convertAndSend("/topic/spittlefeed", shout);
    }

    public void broadcastSpittleToUser(Greeting greeting) {
        messaging.convertAndSendToUser("guest", "/queue/notifications", new Greeting("I am a msg from SubscribeMapping('/macro')."));
    }
}
