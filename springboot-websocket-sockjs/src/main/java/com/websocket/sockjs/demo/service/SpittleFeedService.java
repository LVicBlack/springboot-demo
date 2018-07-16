package com.websocket.sockjs.demo.service;

import com.websocket.sockjs.demo.config.SocketSessionRegistry;
import com.websocket.sockjs.demo.vo.Greeting;
import com.websocket.sockjs.demo.vo.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpittleFeedService {
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

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

    public void broadcastSpittleToUser(Greeting greeting,String username) {
        String sessionId=webAgentSessionRegistry.getSessionIds(username).stream().findFirst().get();
        messaging.convertAndSendToUser(sessionId, "/queue/notifications", new Greeting("I am a msg from SubscribeMapping('/macro')."),createHeaders(sessionId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
