package cch.tweet4emergency.streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.TwitterStream;

import java.util.function.Consumer;

@Service
public class TwitterStreaming implements Streaming<Status, FilterQuery> {

    private final TwitterStream twitterStream;
    private boolean emptyTweetHandler = true;

    @Autowired
    public TwitterStreaming(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void start() {
        if (!emptyTweetHandler) {
            twitterStream.sample();
            twitterStream.filter(onFiltering());
        }
    }

    @Override
    public void stop() {
        twitterStream.clearListeners();
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }

    @Override
    public void onArrival(Consumer<Status> consumerFn) {
        twitterStream.onStatus(status -> consumerFn.accept(status));
        emptyTweetHandler = false;
    }

    @Override
    public FilterQuery onFiltering() {
        FilterQuery filterQuery = new FilterQuery();
        String[] keywords = {
                "earthquake",
                "magnitude",
                "seismic",
                "tremor"
        };
        filterQuery.track(keywords);
        return filterQuery;
    }
}
