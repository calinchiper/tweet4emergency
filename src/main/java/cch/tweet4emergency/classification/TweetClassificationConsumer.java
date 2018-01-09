package cch.tweet4emergency.classification;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.model.Certitude;
import cch.tweet4emergency.service.classification.DataClassification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
public class TweetClassificationConsumer implements Consumer<Event<EarthquakeRelatedInfo>> {
    private final DataClassification<Certitude> dataClassification;
    private final EventBus eventBus;

    @Autowired
    public TweetClassificationConsumer(EventBus eventBus,
                                       DataClassification<Certitude> dataClassification) {
        this.dataClassification = dataClassification;
        this.eventBus = eventBus;
    }

    @Override
    public void accept(Event<EarthquakeRelatedInfo> earthquakeRelatedInfoEvent) {
        EarthquakeRelatedInfo info = earthquakeRelatedInfoEvent.getData();
        info.setCertitude(dataClassification.classify(info.getContent()));
        eventBus.notify("websocket", Event.wrap(info)); // notify the websocket handler
    }
}
