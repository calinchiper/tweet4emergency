package cch.tweet4emergency.producer;


import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.streaming.Streaming;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import twitter4j.FilterQuery;
import twitter4j.Status;

@Service
public class TweetProducer implements Producer {
    private final EventBus eventBus;
    private final Streaming<Status, FilterQuery> streaming;

    public TweetProducer(EventBus eventBus, Streaming<Status, FilterQuery> streaming) {
        this.eventBus = eventBus;
        this.streaming = streaming;
    }

    @Override
    public void produce() {
        streaming.onArrival(status -> notifyEventBus(status));
        streaming.start();
    }

    private void notifyEventBus(Status status) {
        eventBus.notify("earthquakeRelatedInfoFactory", Event.wrap(status));
    }
}
