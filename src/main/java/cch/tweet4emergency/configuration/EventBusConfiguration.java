package cch.tweet4emergency.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

@Configuration
public class EventBusConfiguration {

    @Bean
    EventBus eventBus() {
        return EventBus.create(
                Environment.initializeIfEmpty()
                           .assignErrorJournal(),
                Environment.THREAD_POOL
        );
    }


}
