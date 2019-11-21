import java.io.IOException;
import java.sql.SQLException;

 
public class CrawlerController {
 
	public static void main(String[] args) throws SQLException, IOException {
		Crawler Rcrawler = new Crawler("https://www.reddit.com/r/","dank");
		
		Rcrawler.addBadWord("login");
		Rcrawler.addBadWord("username");
		Rcrawler.addBadWord("register");
		Rcrawler.addBadWord("wiki");
		
		

		Rcrawler.cleandb();
		
		long startTime = System.nanoTime();

		Rcrawler.searchPage("https://www.reddit.com/");

		long endTime = System.nanoTime();
		
		long duration = (endTime - startTime);
		
		double seconds = duration/Double.valueOf(1000000000);
		
		int minutes = (int)seconds/60;
		
		int secs = (int)seconds%60;
		

		System.out.println("Time elapsed: " + minutes + "m " + secs + "s");

	}   
 
	
}