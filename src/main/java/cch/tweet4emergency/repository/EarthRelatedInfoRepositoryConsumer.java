package cch.tweet4emergency.repository;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
public class EarthRelatedInfoRepositoryConsumer implements Consumer<Event<EarthquakeRelatedInfo>> {
    private final EventBus eventBus;
    private final EarthRelatedInfoRepository repository;

    public EarthRelatedInfoRepositoryConsumer(EventBus eventBus, EarthRelatedInfoRepository repository) {
        this.eventBus = eventBus;
        this.repository = repository;
    }

    @Override
    public void accept(Event<EarthquakeRelatedInfo> earthquakeRelatedInfoEvent) {
        EarthquakeRelatedInfo info = earthquakeRelatedInfoEvent.getData();
        repository.save(info);
    }
}
