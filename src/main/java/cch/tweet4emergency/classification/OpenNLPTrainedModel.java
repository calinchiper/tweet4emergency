package cch.tweet4emergency.classification;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OpenNLPTrainedModel implements TrainedModel<DoccatModel> {
    private final String inputPath;
    private DoccatModel model;
    private final int trainingIterations;

    public OpenNLPTrainedModel(String inputRelativePath, int trainingIterations) {
        this.inputPath = inputRelativePath;
        this.trainingIterations = trainingIterations;
    }

    @Override
    public DoccatModel get() {
        InputStream input = null;
        try {
            input = new FileInputStream(new ClassPathResource(inputPath).getFile().getAbsolutePath());
            ObjectStream<String> lineStream = new PlainTextByLineStream(input, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
            int cutOff = 2;
            model = DocumentCategorizerME.train("en", sampleStream, cutOff, trainingIterations);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return model;
    }
}
