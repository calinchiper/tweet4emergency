package cch.tweet4emergency.consumer;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.model.Sentiment;
import cch.tweet4emergency.service.semantic.SemanticAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@Service
public class SemanticAnalysisServiceConsumer implements Consumer<Event<EarthquakeRelatedInfo>> {
    private final SemanticAnalysisService<EarthquakeRelatedInfo, Sentiment> semanticAnalysisService;
    private final EventBus eventBus;

    @Autowired
    public SemanticAnalysisServiceConsumer(EventBus eventBus,
                                           SemanticAnalysisService<EarthquakeRelatedInfo, Sentiment> semanticAnalysisService) {
        this.semanticAnalysisService = semanticAnalysisService;
        this.eventBus = eventBus;
    }

    @Override
    public void accept(Event<EarthquakeRelatedInfo> earthquakeRelatedInfoEvent) {
        EarthquakeRelatedInfo info = earthquakeRelatedInfoEvent.getData();
        info.setSentiment(
                semanticAnalysisService.analyze(info)
        );
        System.out.println(info.getSentiment());
    }
}
