package cch.tweet4emergency;

import cch.tweet4emergency.service.publisher.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    Publisher publisher;

    @GetMapping
    public String test()  {
        new Thread(() -> run()).start();
        return "asd";
    }

    private void run() {
        publisher.publish();
    }

}
