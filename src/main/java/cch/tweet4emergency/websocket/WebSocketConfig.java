package cch.tweet4emergency.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final StreamingSocketHandler streamingSocketHandler;

    @Autowired
    public WebSocketConfig(StreamingSocketHandler streamingSocketHandler) {
        this.streamingSocketHandler = streamingSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(streamingSocketHandler, "/stream")
                                .setAllowedOrigins("*");
    }
}
