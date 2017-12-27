package cch.tweet4emergency.sentiment;

public interface SentimentAnalysis<R> {
    R sentiment(String content);
}
