package testuggine.timepatterns.src;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


import edu.princeton.cs.algs4.Date;


/** This class takes a restaurant into account, and prints the time patterns of its history */
public class TimePatterns {
	// For each (starting) date, 
	// you have a mean val (double) and the number of reviews (Int)
	
	public TimePatterns(String restaurantName) throws NumberFormatException, SQLException {
		HashMap<Date, Pair<Double, Integer>> result = processRestaurant(restaurantName);
	
	} 


	private HashMap<Date, Pair<Double, Integer>> processRestaurant(String restaurantName) throws NumberFormatException, SQLException {
		HashMap<Date, Pair<Double, Integer>> result = new HashMap<Date, Pair<Double, Integer>>();
		TimeStampedRatingMap domain = setupDomain();
		for (Entry<Date, List<Integer>> entry  : domain) {
			System.out.println("Ciao");
		}
		
		
		return null;
	}


	private TimeStampedRatingMap setupDomain() throws NumberFormatException, SQLException {
		TimeStampedRatingMap returnVal = new TimeStampedRatingMap();
		String restaurantName = "";
		ResultSet res = getDataFromDB(restaurantName);
		
		while (res.next()) {
			Date d = parseSQLDate(res.getString("Date"));
			Integer rating = Integer.valueOf(res.getString("rating"));
			returnVal.insert(d, rating);
		}

		return returnVal;
	}


	private ResultSet getDataFromDB(String restaurantName) {
		// TODO Auto-generated method stub
		return null;
	}


	private Date parseSQLDate(String string) {
		// TODO Auto-generated method stub
		return null;
	}




}
