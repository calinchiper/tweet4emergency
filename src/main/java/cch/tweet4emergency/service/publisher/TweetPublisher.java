package cch.tweet4emergency.service.publisher;

import cch.tweet4emergency.model.EarthquakeRelatedStatus;
import cch.tweet4emergency.streaming.TwitterStreamingSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetPublisher implements Publisher {

    private final TwitterStreamingSource streamingSource;

    @Autowired
    public TweetPublisher(TwitterStreamingSource streamingSource) {
        this.streamingSource = streamingSource;
    }

    @Override
    public void publish() {
        streamingSource.streamData().foreachRDD(
                statusRDD -> statusRDD.map(status -> new EarthquakeRelatedStatus(status))
                                      .foreach(earthquakeRelatedStatus -> {
                                            System.out.println(earthquakeRelatedStatus);
                                      })
        );
        streamingSource.startStreaming();
    }
}
