package cch.tweet4emergency.classification;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
public class TweetClassificationConsumer implements Consumer<Event<EarthquakeRelatedInfo>> {
    private final TweetClassification tweetClassification;
    private final EventBus eventBus;

    @Autowired
    public TweetClassificationConsumer(EventBus eventBus,
                                       TweetClassification dataClassification) {
        this.tweetClassification = dataClassification;
        this.eventBus = eventBus;
    }

    @Override
    public void accept(Event<EarthquakeRelatedInfo> earthquakeRelatedInfoEvent) {
        EarthquakeRelatedInfo info = earthquakeRelatedInfoEvent.getData();
        info.setCertitude(tweetClassification.classify(info.getContent()));
        eventBus.notify("websocket", Event.wrap(info)); // notify the websocket handler
    }
}
