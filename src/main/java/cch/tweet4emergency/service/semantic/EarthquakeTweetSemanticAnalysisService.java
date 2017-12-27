package cch.tweet4emergency.service.semantic;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import cch.tweet4emergency.model.Sentiment;
import cch.tweet4emergency.sentiment.SentimentAnalysis;
import org.springframework.stereotype.Service;

@Service
public class EarthquakeTweetSemanticAnalysisService implements SemanticAnalysisService<EarthquakeRelatedInfo, Sentiment> {
    private final SentimentAnalysis<Sentiment> sentimentAnalysis;

    public EarthquakeTweetSemanticAnalysisService(SentimentAnalysis<Sentiment> sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }

    @Override
    public Sentiment analyze(EarthquakeRelatedInfo info) {
        return sentimentAnalysis.sentiment(info.getContent());
    }
}
