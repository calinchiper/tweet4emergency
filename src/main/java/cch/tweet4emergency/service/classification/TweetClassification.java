package cch.tweet4emergency.service.classification;

import cch.tweet4emergency.classification.ClassifiedTweet;
import cch.tweet4emergency.model.Certitude;
import opennlp.tools.doccat.DoccatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetClassification implements DataClassification<Certitude> {
    private final DoccatModel model;

    @Autowired
    public TweetClassification(DoccatModel model) {
        this.model = model;
    }

    @Override
    public Certitude classify(String tweet) {
        return new ClassifiedTweet(model).result(tweet);
    }
}
