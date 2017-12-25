package cch.tweet4emergency.configuration;

import cch.tweet4emergency.streaming.TwitterStreamingSource;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.bus.EventBus;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.User;

@Configuration
public class SparkConfiguration {

    @Bean
    public JavaStreamingContext streamingContext() {
        Class[] serializableClasses = {GeoLocation.class,
                                       User.class,
                                       Status.class};
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(serializableClasses)
                .setAppName("streamingTask");
        return new JavaStreamingContext(conf, Durations.seconds(10));
    }

}
