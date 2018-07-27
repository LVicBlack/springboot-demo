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
        // 模式2 - 获取id、添加MessageHeaders
        messaging.convertAndSendToUser(sessionId, "/queue/notifications", new Greeting("I am a msg from SubscribeMapping('/macro')."),createHeaders(sessionId));
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
