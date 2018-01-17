package cch.tweet4emergency.classification;

import cch.tweet4emergency.model.Certitude;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

import static cch.tweet4emergency.model.Certitude.NEGATIVE;
import static cch.tweet4emergency.model.Certitude.POSITIVE;

@Service
public class TweetClassification {
    private DoccatModel model;
    private final String trainingDataFilePath;

    public TweetClassification(String trainingDataFilePath) {
        this.trainingDataFilePath = trainingDataFilePath;
        trainModel();
    }

    public Certitude classify(String content) {
        DocumentCategorizerME tweetCategorizer = new DocumentCategorizerME(model);
        double[] outcomes = tweetCategorizer.categorize(content);
        String certitudeValue = tweetCategorizer.getBestCategory(outcomes);

        return certitudeValue.equalsIgnoreCase("1") ? POSITIVE : NEGATIVE;
    }

    private void trainModel() {
        InputStream inputStream = null;
        try {
             inputStream = new FileInputStream(
                    new File(new ClassPathResource(trainingDataFilePath).getFile().getAbsolutePath())
             );

            ObjectStream<String> lineSteam = new PlainTextByLineStream(inputStream, "UTF-8");
            ObjectStream<DocumentSample> sample = new DocumentSampleStream(lineSteam);

            model = DocumentCategorizerME.train("en", sample, 2, 30);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
