package finalSearchEngine;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.PrintStream;
public class webcrawaler {

  public static void main(String[] args) throws IOException{
      File file=new File("C:\\Users\\vadre\\OneDrive\\Documents\\acc notes\\trial5-1\\ACC-Web-Search-Engine-master\\src\\finalSearchEngine\\output.txt");
        PrintStream stream=new PrintStream(file);
              System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
      System.setOut(stream);
    try {
      // fetch the document over HTTP
      Document doc = Jsoup.connect("https://www.ebay.com/globaldeals").get();
      // get all links in page
      Elements links = doc.select("a[href]");
      // get the value from the href attribute
      for (Element link : links) {
        System.out.println(link.attr("href"));
      }
    } catch (IOException e) {
    e.printStackTrace();
    }
  }
}