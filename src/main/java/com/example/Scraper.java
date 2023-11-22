//code lifted from these two tutorials:
// from this tutorial:
// https://medium.com/@elvismiranda213/web-scraping-with-java-top-10-google-search-results-6008a07afae6

//& this tutorial
//https://serpdog.io/blog/web-scraping-google-search-results-with-java/#Scraping_Google_Search_Results_With_Java

//Then modified to work with the **current** google search results page as retrieved by the jsoup library user agent

//Created/Modified by Courtney Brown, Oct. 2023. 
//Notes: Replaced Jaunt API library from previous years with jsoup library due to jaunt not working with Maven and the annoying feature that it expires monthly & requires re-download. 

//NOTE: IF you modify this code, please note where you modified, etc. in the comments for grading.

package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//This was used to write the file to a textfile in order to discover which div class contained the search results
//Left here in case it it is useful again for a student, why not? Although, the code to write the file has been deleted in the interest of clarity & code performance
import java.io.File; // Import the File class
import java.io.FileWriter;
import java.io.IOException; // Import the IOException class to handle errors

import java.util.ArrayList;

public class Scraper {

    // from the 1st tutorial -- defines the user agent -- basically the headless (ie, no GUI) browser we're using to go to web pages 
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

    // nested class for storing the google results
    class GoogleSearchResults {
        String title; // title of result page
        String url; // url of result page
        String text; // all the text EVER in the result page -- this is not a snippet, but the entire
                     // text of the page
        int position; // which rank in the results

        GoogleSearchResults(String title_, String url_, String text_, int position_) {
            title = title_;
            url = url_;
            text = text_;
            position = position_;
        }

        String getText() {
            return text;
        }

        String getURL() {
            return url;
        }

        String getTitle() {
            return title;
        }

        int getPosition() {
            return position;
        }
    } //end GoogleSearchClass definition

    // Webscraping is OPTIONAL! You can use this code or not. It's just a tool for
    // you to use if you want to use it.
    // This webscrapes google results. You can use this code, modify it, or use as a
    // model to scrape other webpages.
    public ArrayList<GoogleSearchResults> googleSearch(String searchString) {

        ArrayList<GoogleSearchResults> search_results = new ArrayList<GoogleSearchResults>(); // all the search results
                                                                                              // from the query
        try { //NOTE: this is a try-catch block. It's a way to handle errors. You can read about it online. It's not necessary for you to understand it for this class.
              //Basically, the following code might fail, so we need to catch the errors and handle it.

            // Fetch the page
            final Document doc = Jsoup.connect("https://google.com/search?q=" + searchString).userAgent(USER_AGENT)
                    .get();

            // CBD -- search through html to find <div> with class="Gx5Zad fP1Qef xpd EtOod pkphOe"
            // this is the div that contains the search results -- it used to be class=g
            // (LOL, google changed it)
            // This was discovered by printing the html of the returned doc -- it differed
            // from the results in my browser and even current tutorials
            // Therefore, I rec writing to file the html you find and finding the correct
            // div class if this returns no results -- doc.html() will return the html
            // Again, web-scraping is optional -- some have chosen for final project & if
            // you have more coding experience, particularly in html + parsing, it's not too hard and a good skill to have for jobs
            // I can help some during lab time if this is not working on your machine, 
            // but I will prioritize students who are having problems with required aspects of the projects first
            Elements results = doc.select("div.Gx5Zad.fP1Qef.xpd.EtOod.pkphOe");
            System.out.println(results.size());

            int count = 0; // changed to count from "c" bc naming conventions
            for (Element result : results) {
                // Extract the title and link of the result, from
                // https://medium.com/@elvismiranda213/web-scraping-with-java-top-10-google-search-results-6008a07afae6
                String title = result.select("h3").text();
                String link = result.select("a").attr("href");

                // CBD code --> parsing through the link in the href to actually get the url
                String url = link.substring(link.indexOf("url=") + 4, (link.length() - (4 + link.indexOf("url="))));
                url = url.substring(0, url.indexOf("&"));

                // CBD Code --> actually not a snippet. I just show all the text on the page.
                // I'd recommend some text pre-processing or additional parsing through HTML
                // here for training, though.
                final Document linked_page = Jsoup.connect(url).userAgent(USER_AGENT).get();
                String snippet = linked_page.text();

                // CBD Code --> add to the results
                search_results.add(new GoogleSearchResults(title, url, snippet, count));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Encountered an unsupported file type or webpage. Moving on...");
        }

        return search_results;
    } //end googleSearch() function
} //end Scraper class