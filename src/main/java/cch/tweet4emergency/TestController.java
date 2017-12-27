package cch.tweet4emergency;


import cch.tweet4emergency.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    Producer producer;


    @GetMapping
    public String test()  {
        new Thread(() -> producer.produce()).start();
        return "asd";
    }


}
