package cch.tweet4emergency;

import cch.tweet4emergency.streaming.TwitterStatusReceiver;
import cch.tweet4emergency.streaming.TwitterStreamingSource;
import com.google.common.collect.ImmutableList;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

@RestController
public class TestController {

    @Autowired
    TwitterStreamingSource source;

    @GetMapping
    public String test()  {
        run();
        return "asd";
    }

    private void run() {
        source.publish();
    }
}
