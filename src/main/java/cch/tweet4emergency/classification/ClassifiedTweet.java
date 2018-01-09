package cch.tweet4emergency.classification;

import cch.tweet4emergency.model.Certitude;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

public class ClassifiedTweet implements ClassifiedData<String, Certitude> {
    private final DoccatModel model;

    public ClassifiedTweet(DoccatModel model) {
        this.model = model;
    }

    @Override
    public Certitude result(String tweet) {
        DocumentCategorizerME classifier = new DocumentCategorizerME(model);
        double[] outcomes = classifier.categorize(tweet);
        String category = classifier.getBestCategory(outcomes);

        if (category.equalsIgnoreCase("1")) {
            return Certitude.POSITIVE;
        } else {
            return Certitude.NEGATIVE;
        }
    }
}
