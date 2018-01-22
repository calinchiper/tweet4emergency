package cch.tweet4emergency;

import cch.tweet4emergency.factory.EarthquakeRelatedInfoFactoryConsumer;
import cch.tweet4emergency.classification.TweetClassificationConsumer;
import cch.tweet4emergency.repository.EarthRelatedInfoRepositoryConsumer;
import cch.tweet4emergency.websocket.StreamingSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import reactor.Environment;
import reactor.bus.EventBus;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static reactor.bus.selector.Selectors.$;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Tweet4EmergencyApplication implements CommandLineRunner {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private EarthquakeRelatedInfoFactoryConsumer earthquakeRelatedInfoFactory;

    @Autowired
    private TweetClassificationConsumer tweetClassificationConsumer;

    @Autowired
    private StreamingSocketHandler streamingSocketHandler;

    @Autowired
    private EarthRelatedInfoRepositoryConsumer repository;

    public static void main(String[] args) {
        SpringApplication.run(Tweet4EmergencyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // setting the event bus
        eventBus.on($("earthquakeRelatedInfoFactory"), earthquakeRelatedInfoFactory);
        eventBus.on($("classification"), tweetClassificationConsumer);
        eventBus.on($("websocket"), streamingSocketHandler);
        eventBus.on($("repository"), repository);
    }

    @Bean
    EventBus eventBus() {
        return EventBus.create(
                Environment.initializeIfEmpty()
                        .assignErrorJournal(),
                Environment.THREAD_POOL
        );
    }

    @Bean
    TwitterStream twitterStream() {
        return new TwitterStreamFactory().getInstance();
    }

    @Bean
    String trainingData() {
        return "tweets.txt";
    }

}
