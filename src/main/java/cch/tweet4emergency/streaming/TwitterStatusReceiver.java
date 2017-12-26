package cch.tweet4emergency.streaming;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import twitter4j.*;

public abstract class TwitterStatusReceiver extends Receiver<Status> {

    private final TwitterStream twitterStream;

    public TwitterStatusReceiver(StorageLevel storageLevel) {
        super(storageLevel);
        this.twitterStream = new TwitterStreamFactory().getInstance();
    }

    @Override
    public void onStart() {
        new Thread(this::receive).start();
    }

    @Override
    public void onStop() {
        twitterStream.clearListeners();
        twitterStream.cleanUp();
    }

    public abstract FilterQuery onFiltering();

    private void receive() {
        twitterStream.onStatus(this::store);
        twitterStream.filter(onFiltering());
    }
}
