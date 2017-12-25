package cch.tweet4emergency;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;




@RunWith(SpringRunner.class)
@SpringBootTest
public class Tweet4EmergencyApplicationTests {

    String consumerKey = "hMd3dLlJkLs5dtGl3DYfTkJLH";
    String consumerSecret = "RyR7DqyAVC03l4JYEH3ttHq5F1VlKUVtq3Oz1BSsecKgbh8GjP";
    String accessToken = "922175928146583557-bRY5lJ4rYLNf3IpAYexkEGEOIm3q7ds";
    String accessTokenSecret = "jLV2juBofqgrTEybjonLwRJ0NtBVgqPLCAiAltIfnPern";
    String hadoopHomeDirectory = "C:\\Program Files\\Apache\\hadoop";



//    @Test
//    public void testApacheSpark() {
//
//
////        System.out.println("Twitter App Authentication");
////        System.out.println("Consumer key: " + consumerKey);
////        System.out.println("Consumer secret: " + consumerSecret);
////        System.out.println("Access token: " + accessToken);
////        System.out.println("Access token secret: " + accessTokenSecret);
////
////        System.out.println("Hadoop Home directory: " + hadoopHomeDirectory);
////
////
////        System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
////        System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
////        System.setProperty("twitter4j.oauth.accessToken", accessToken);
////        System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);
////        System.setProperty("hadoop.home.dir", hadoopHomeDirectory);
////
////        TwitterUtils.createStream(this.streamingContext)
////                    .filter(status -> status.getText().contains("earthquake"))
////                    .map(status -> "[" + status.getCreatedAt().toString() + "] : " + status.getText())
////                    .foreachRDD((VoidFunction<JavaRDD<String>>) stringJavaRDD -> {
////                        stringJavaRDD.saveAsTextFile("src/main/resources/results");
////                    });
////
////
////        this.streamingContext.start();
////        this.streamingContext.awaitTermination();
//
//    }

    @Test
    public void testTwitter4J() throws TwitterException {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("hMd3dLlJkLs5dtGl3DYfTkJLH")
                .setOAuthConsumerSecret("RyR7DqyAVC03l4JYEH3ttHq5F1VlKUVtq3Oz1BSsecKgbh8GjP")
                .setOAuthAccessToken("922175928146583557-bRY5lJ4rYLNf3IpAYexkEGEOIm3q7ds")
                .setOAuthAccessTokenSecret("jLV2juBofqgrTEybjonLwRJ0NtBVgqPLCAiAltIfnPern");

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println(status.getUser().getName() + " : " + status.getText()+ "  Tweeted AT: " + status.getCreatedAt());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {

            }
        };
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(cb.build());
        TwitterStream twitterStream = twitterStreamFactory.getInstance();

        twitterStream.addListener(listener);

        twitterStream.sample();

        FilterQuery filtre = new FilterQuery();
        String[] keywordsArray = { "earthquake", "shaking" }; //filter based on your choice of keywords
        filtre.track(keywordsArray);
        twitterStream.filter(filtre);
    }



}
