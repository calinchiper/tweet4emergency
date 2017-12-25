package cch.tweet4emergency.streaming;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import twitter4j.*;

public class TwitterStatusReceiver extends Receiver<Status> {

    private final TwitterStream twitterStream;
    private final String[] keywords;

    public TwitterStatusReceiver(StorageLevel storageLevel, String ...keywords) {
        super(storageLevel);
        this.twitterStream = new TwitterStreamFactory().getInstance();
        this.keywords = keywords;
    }

    @Override
    public void onStart() {
        twitterStream.onStatus(this::store);
        twitterStream.filter(filters());
    }

    @Override
    public void onStop() {
        twitterStream.clearListeners();
        twitterStream.cleanUp();
    }

    private FilterQuery filters() {
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keywords);
        return filterQuery;
    }
}
