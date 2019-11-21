import java.io.IOException;
//import java.net.MalformedURLException;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	private  String URLkey;
	private  String topic;
	private final int pageLimit = 10;
	private ArrayList<String> BadWords = new ArrayList<>();
	private ArrayList<String> pageList = new ArrayList<>();
	private ArrayList<String> visitedList = new ArrayList<>();
	private static DataBase db = new DataBase();
	
	
	public Crawler(String URLkey, String topic) {
		visitedList.clear();
		this.URLkey = URLkey;
		this.topic = topic;
	}
	 
	public void cleandb() throws SQLException, IOException {
		
		db.runSql2("TRUNCATE Record;");
		
	}
	
	public void addBadWord(String badword) {
		BadWords.add(badword);
	}
 
	
	//check urls to see if they should be visited
	private boolean shouldVisit(Element link) {
		
		boolean visit = true;
			
	for(String word : BadWords) {
				
			if (link.attr("abs:href").contains(word))
				visit = false;
		}
			
		visit = (visit
				&& link.attr("abs:href").contains(URLkey) 
				&& pageList.size() < pageLimit 
				&& !visitedList.contains(link.attr("abs:href")));
			
		return visit;
	}
	
	
	//checks page for topic and if present saves page to data base
	private void getPage(String URL) {
		
		try {
		
			Document doc = Jsoup.connect(URL).get();
			
			
			
			if(doc.text().contains(topic)){
				System.out.println("Saving: " + URL);
				pageList.add(URL);
				
				
				//store the URL to database to avoid parsing again
				String sql = "INSERT INTO  `crawlerdb`.`Record` " + "(`URL`) VALUES " + "(?);";
				PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, URL);
				stmt.execute();
				
			}
		
		}catch(Exception ex){
			System.out.println("[" + URL + "] :"+" Could not be read :(");
		}
	}
	

	public void searchPage(String URL) throws SQLException, IOException{
		
//		String sql;
		int i = 0; //pages searched
		
		System.out.println(URL);

		//check if the given URL is already in database
//		sql = "select * from Record where URL = '"+URL+"'";
//		ResultSet rs = db.runSql(sql);
//		if(!rs.next()){
 
			try {
				//get doc object
				Document doc = Jsoup.connect(URL).get();			
				
				
				//get all links from doc object and process each
				Elements questions = doc.select("a[href]");
				for(Element link: questions){
					String url = link.attr("abs:href");
					if(shouldVisit(link)) {
						visitedList.add(url);
						System.out.println(url);	
						//getPage(url);
						i++;
					}
				}
				System.out.println("Pages returned: " + i);
			}catch(Exception ex){
				System.out.println("[" + URL + "] :"+" Could not be read :(");
			}
//		}
	}
	                                                             
}

