/* Programmer: Courtney Brown
 * Date: Fall 2017, modified 2019, 2023
 * This class uses the twitter4j library to update a twitter status via code and perform limited searches.
 * Using API & modfied from examples here & the twitterv2 library: http://twitter4j.org/en/
 */

package com.example;

//for twitter, also using jp.takke.twitter4j-v2 for version 2 support
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterInteraction {

    TwitterV2 twitterAPIv2 = null; //twitter API v2 -- has the API calls to tweet

    //logs into twitter using OAuth
TwitterInteraction() {

    //connecting to the twitter API and authorizing via OAuth
    //TODO: replace ********** with your keys & tokens
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true).setOAuthConsumerKey("2ytm5FT164hNOgUliDWaeI48G") //API Key here
            .setOAuthConsumerSecret("rVNx7BBS6727FdxMhDAqfOmmVwDwGtNEdaGmw8UexYV06QWq06") //Secret key here
            .setOAuthAccessToken("1727046806713106432-wqvkxqtEuwTWyDUskbpDFSveUNGtlA") //access token here
            .setOAuthAccessTokenSecret("vlLfA22pAfM4sRwGzQz0FgOczMTPnQ1es4EmGB85SPWAO"); //secret access token here

    TwitterFactory tf = new TwitterFactory(cb.build()); //get the twitter factory -- for getting the twitter instance
    Twitter twitter = tf.getInstance(); //version 1 twitter API
    try {
        twitterAPIv2 = TwitterV2ExKt.getV2(twitter); //get the v2 API from v1 instance
    } catch (Exception e) {
        e.printStackTrace();
    }

}

	//updates twitter status with the update_str
	public void updateTwitter(String update_str) {

        if( twitterAPIv2==null )
        {
            System.out.println("Twitter API not initialized and failed to load. Check your keys.");
            return;
        }
		try {
            //please see jp.takke.twitter4j-v2 on github, this is highly modified from the example code, but it will elucidate the missing arguments if interested
            //https://github.com/takke/twitter4j-v2
            CreateTweetResponse response = twitterAPIv2.createTweet(null, null,null, null, null, null, null, null, null, null, null, update_str);		
			System.out.println("Successfully updated the status to [" + update_str + "].");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to read the system input.");
		}
	}

//If you want to pay 100$/month you can search tweets. Up to you. Not a supported option for this assignment anymore.
//This is Twitter API v1.1, to search tweets, will need to pay money and probably use 2.0 API. Up to you!! :) 

// 	//returns a list of tweets with the given search term
// 	public ArrayList<String> searchForTweets(String searchTerm) {
// 		ArrayList<String> res = new ArrayList(); 
// 		try {
// 			Query query = new Query(searchTerm);
// 			query.count(100);
			
// 			QueryResult result = twitter.search(query);
// 			for (Status status : result.getTweets()) {
// //				System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
// 				res.add(status.getText()); 
// 			}
// 		} catch (TwitterException te) {
// 			te.printStackTrace();
// 			System.out.println("Failed to get timeline: " + te.getMessage());
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			System.out.println("Failed to read the system input.");
// 		}
// 		return res; 
// 	}

}
