package com.websocket.sockjs.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 应用程序以 /app 为前缀，而 代理目的地以 /topic 为前缀.
        // js.url = "/spring13/app/hello" -> @MessageMapping("/hello") 注释的方法.
        config.enableSimpleBroker("/topic", "/queue");// 客户端订阅地址的前缀信息
        config.setApplicationDestinationPrefixes("/app");// 服务端接收地址的前缀
        config.setUserDestinationPrefix("/user");

        /* --------------------------------------------------------------------- */

//        这里不再模拟STOMP 代理的功能，而是由 代理中继将消息传送到一个 真正的消息代理来进行处理；

//        // 启用了 STOMP 代理中继功能，并将其代理目的地前缀设置为 /topic and /queue .
//        // setXXX()方法 是可选的
//        config.enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost("rabbit.someotherserver")
//                .setRelayPort(62623)
//                .setClientLogin("marcopolo")
//                .setClientPasscode("letmein01");
//        config.setApplicationDestinationPrefixes("/app", "/foo");// 应用程序目的地.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 在网页上我们就可以通过这个链接 /server/hello ==<c:url value='/hello'></span> 来和服务器的WebSocket连接
        registry.addEndpoint("/hello").setAllowedOrigins("*").withSockJS();
    }

    // 模式1 - 改写StompHeaderAccessor中User信息
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptorAdapter() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor =
//                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String agentId = accessor.getNativeHeader("login").get(0);
//                    accessor.setUser(new Authentication(agentId));
//                }
//                return message;
//            }
//        });
//    }

    @Bean
    public SocketSessionRegistry SocketSessionRegistry() {
        return new SocketSessionRegistry();
    }

    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener() {
        return new STOMPConnectEventListener();
    }

}
