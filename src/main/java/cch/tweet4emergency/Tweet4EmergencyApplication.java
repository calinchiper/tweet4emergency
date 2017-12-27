package cch.tweet4emergency;

import cch.tweet4emergency.consumer.EarthquakeRelatedInfoFactoryConsumer;
import cch.tweet4emergency.consumer.SemanticAnalysisServiceConsumer;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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

import java.util.Properties;

import static reactor.bus.selector.Selectors.$;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Tweet4EmergencyApplication implements CommandLineRunner {

	@Autowired
	private  EventBus eventBus;

	@Autowired
	private EarthquakeRelatedInfoFactoryConsumer earthquakeRelatedInfoFactory;

	@Autowired
	private SemanticAnalysisServiceConsumer semanticAnalysisService;


	public static void main(String[] args) {
		SpringApplication.run(Tweet4EmergencyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		eventBus.on($("earthquakeRelatedInfoFactory"), earthquakeRelatedInfoFactory);
		eventBus.on($("semanticAnalysisService"), semanticAnalysisService);
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
	StanfordCoreNLP pipeline() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		return new StanfordCoreNLP(props);
	}
}
