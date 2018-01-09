package cch.tweet4emergency.websocket;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StreamingSocketHandler extends TextWebSocketHandler implements Consumer<Event<EarthquakeRelatedInfo>> {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Producer producer;

    @Autowired
    public StreamingSocketHandler(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        new Thread(() -> producer.produce()).start();
    }

    @Override
    public void accept(Event<EarthquakeRelatedInfo> earthquakeRelatedInfoEvent) {
        EarthquakeRelatedInfo info = earthquakeRelatedInfoEvent.getData();
        sendStreamingData(info.toJsonText());
        System.out.println("[ " + info.getContent() + " ]");
    }

    private void sendStreamingData(String data) {
        for (WebSocketSession session : sessions) {
            try {

                synchronized (this) {
                    session.sendMessage(new TextMessage(data));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed");
        producer.stopProducing();
        sessions.remove(session);
    }
}
