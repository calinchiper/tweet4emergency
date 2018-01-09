package cch.tweet4emergency;

import cch.tweet4emergency.factory.EarthquakeRelatedInfoFactoryConsumer;
import cch.tweet4emergency.classification.TweetClassificationConsumer;
import cch.tweet4emergency.classification.OpenNLPTrainedModel;
import cch.tweet4emergency.websocket.StreamingSocketHandler;
import opennlp.tools.doccat.DoccatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


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

    public static void main(String[] args) {
        SpringApplication.run(Tweet4EmergencyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // setting the event bus
        eventBus.on($("earthquakeRelatedInfoFactory"), earthquakeRelatedInfoFactory);
        eventBus.on($("classification"), tweetClassificationConsumer);
        eventBus.on($("websocket"), streamingSocketHandler);
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
    DoccatModel model() {
        return new OpenNLPTrainedModel("tweets.txt", 30).get();
    }

}
