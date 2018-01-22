package cch.tweet4emergency;

import cch.tweet4emergency.model.EarthquakeRelatedInfo;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.core.io.ClassPathResource;
import twitter4j.Status;


import java.io.*;


public class Tweet4EmergencyApplicationTests {
   DoccatModel model;

   @Test
   public void whenModelTrained_getResult() throws FileNotFoundException {
      InputStream dataIn = null;
      try {
         dataIn = new FileInputStream(new ClassPathResource("tweets.txt").getFile().getAbsolutePath());
         ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
         ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

         // Specifies the minimum number of times a feature must be seen
         int cutoff = 2;
         int trainingIterations = 30;
         model = DocumentCategorizerME.train("en", sampleStream, cutoff,
                 trainingIterations);
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (dataIn != null) {
            try {
               dataIn.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }

      String tweet = "It's an earthquake!!!";
      DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
      double[] outcomes = myCategorizer.categorize(tweet);
      String category = myCategorizer.getBestCategory(outcomes);

      assertEquals("1", category);
   }

   @Test
   public void whenGivenRelativePath_getAbsolutePath() throws IOException {
      String actualValue = new ClassPathResource("tweets.txt").getFile().getAbsolutePath();
      String expected = "C:\\Users\\Calin\\IdeaProjects\\Tweet4Emergency\\target\\classes\\tweets.txt";
      assertEquals(expected, actualValue);
   }

   @Test
   public void whenGivenPath_getFile() throws IOException {
      InputStream dataIn = new FileInputStream(new ClassPathResource("tweets.txt").getFile().getAbsolutePath());
      assertNotNull(dataIn);
   }

   @Test
   public void whenHavingFile_readFirstLine() throws IOException {
      InputStream dataIn = new FileInputStream(new ClassPathResource("tweets.txt").getFile().getAbsolutePath());
      ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
      assertEquals("1\tIt's an earthquake !", lineStream.read());
   }

   @Test
   public void whenHavingFirstLine_tokenizeIt() throws IOException {
      InputStream dataIn = new FileInputStream(new ClassPathResource("tweets.txt").getFile().getAbsolutePath());
      ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
      ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
      assertArrayEquals(new String[]{"It's", "an", "earthquake", "!"}, sampleStream.read().getText());
   }
}
