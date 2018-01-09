package cch.tweet4emergency.factory;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.service.factory.EarthquakeRelatedInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;
import twitter4j.Status;

@Service
public class EarthquakeRelatedInfoFactoryConsumer implements Consumer<Event<Status>> {
    private final EarthquakeRelatedInfoFactory earthquakeRelatedInfoFactory;
    private final EventBus eventBus;

    @Autowired
    public EarthquakeRelatedInfoFactoryConsumer(EarthquakeRelatedInfoFactory earthquakeRelatedInfoFactory, EventBus eventBus) {
        this.earthquakeRelatedInfoFactory = earthquakeRelatedInfoFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void accept(Event<Status> statusEvent) {
        Status rawStatus = statusEvent.getData();
        EarthquakeRelatedInfo info = earthquakeRelatedInfoFactory.extract(rawStatus);
        eventBus.notify("classification", Event.wrap(info));
    }
}
