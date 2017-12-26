package cch.tweet4emergency.streaming;

import cch.tweet4emergency.model.EarthquakeRelatedStatus;
import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.stereotype.Component;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.User;



@Component
public final class TwitterStreamingSource implements StreamSource<JavaDStream<Status>> {

    private final transient JavaStreamingContext streamingContext;

    public TwitterStreamingSource() {
        Class[] serializableClasses = { GeoLocation.class, User.class, Status.class, EarthquakeRelatedStatus.class };
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(serializableClasses)
                .setAppName("streamingTask");
        streamingContext = new JavaStreamingContext(conf, Durations.seconds(10));
    }

    /**
     * @return streaming data
     */
    @Override
    public JavaDStream<Status> streamData()  {
        return streamingContext.receiverStream(
                new TwitterEarthquakeRelatedStatusReceiver(StorageLevel.MEMORY_ONLY())
        );
    }

    @Override
    public void startStreaming() {
        streamingContext.start();
        //streamingContext.awaitTermination();
    }

    @Override
    public void stopStreaming() {
        streamingContext.stop();
    }


}
