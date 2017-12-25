package cch.tweet4emergency.streaming;

import cch.tweet4emergency.service.Publisher;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.EventBus;
import twitter4j.Status;

import java.io.Serializable;


@Component
public class TwitterStreamingSource implements Publisher, StreamSource<JavaReceiverInputDStream<Status>>, Serializable {

    private final transient JavaStreamingContext streamingContext;
    private final transient EventBus eventBus;

    @Autowired
    public TwitterStreamingSource(JavaStreamingContext streamingContext, EventBus eventBus) {
        this.streamingContext = streamingContext;
        this.eventBus = eventBus;
    }

    @Override
    public void publish() {
        try {
            this.stream().foreachRDD(statusJavaRDD ->
                    //publish on event bus
                statusJavaRDD.foreach(status -> System.out.println(status))
            );
            streamingContext.start();
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JavaReceiverInputDStream<Status> stream() throws InterruptedException {
        return streamingContext.receiverStream(
                new TwitterStatusReceiver(StorageLevel.MEMORY_ONLY(), "earthquake")
        );
    }

    @Override
    public void stopStreaming() {
        streamingContext.stop();
    }

}
