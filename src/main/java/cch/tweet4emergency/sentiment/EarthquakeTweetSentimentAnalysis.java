package cch.tweet4emergency.sentiment;

import cch.tweet4emergency.model.Sentiment;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class EarthquakeTweetSentimentAnalysis implements SentimentAnalysis<Sentiment> {

    private final StanfordCoreNLP pipeline;

    @Autowired
    public EarthquakeTweetSentimentAnalysis(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public Sentiment sentiment(String content) {

        Annotation annotation = pipeline.process(content);
        int mainSentimentValue = 0;
        int longest = 0;

        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();

            if (partText.length() > longest) {
                mainSentimentValue = sentiment;
                longest = partText.length();
            }
        }

        //This logic must be improved
        if (containsEarthquakeCrisisKeyWords(content)) {
            return getSentiment(mainSentimentValue - 1);
        } else {
            return getSentiment(mainSentimentValue);
        }
    }

    private Sentiment getSentiment(int sentimentValue) {
        switch (sentimentValue) {
            case 0:
                return Sentiment.VERY_NEGATIVE;
            case 1:
                return Sentiment.NEGATIVE;
            case 2:
                return Sentiment.NEUTRAL;
            case 3:
                return Sentiment.POSITIVE;
            case 4:
                return Sentiment.VERY_POSITIVE;
            default:
                return Sentiment.NEUTRAL;
        }
    }

    private boolean containsEarthquakeCrisisKeyWords(String content) {
        return Stream.of("OMG", "HELP", "SURVIVED", "FIND", "SAVE ME")
                .anyMatch(word -> content.toUpperCase().contains(word));
    }


}

