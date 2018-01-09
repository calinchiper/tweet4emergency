package cch.tweet4emergency;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


import java.io.*;


public class Tweet4EmergencyApplicationTests {
   DoccatModel model;

   @Test
   public void test() throws FileNotFoundException {
      InputStream dataIn = null;
      try {
         dataIn = new FileInputStream(new ClassPathResource("tweets.txt").getFile().getAbsolutePath());
         ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
         ObjectStream sampleStream = new DocumentSampleStream(lineStream);
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

      String tweet = "Earthquakes are fun";
      DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
      double[] outcomes = myCategorizer.categorize(tweet);
      String category = myCategorizer.getBestCategory(outcomes);

      if (category.equalsIgnoreCase("1")) {
         System.out.println("Earthquake detection ");
      } else {
         System.out.println("False alarm :( ");
      }
   }



}
