package testuggine.timepatterns.src;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;


import edu.princeton.cs.algs4.Date;


/** This class takes a restaurant into account, and prints the time patterns of its history */
public class TimePatterns {
	// For each (starting) date, 
	// you have a mean val (double) and the number of reviews (Int)
	
	public TimePatterns(String restaurantName) throws NumberFormatException, SQLException, DomainTooShortException {
		ResultSet res = getDataFromDB(restaurantName);
		TimeStampedRatingMap domain = setupDomain(res);
		DateMovingAvg filter = new DateMovingAvg(domain);
		TreeMap<Date, Pair<Double, Integer>> result = filter.computeBatch();
	} 



	private TimeStampedRatingMap setupDomain(ResultSet res) throws NumberFormatException, SQLException {
		TimeStampedRatingMap returnVal = new TimeStampedRatingMap();
		
		while (res.next()) {
			Date d = parseSQLDate(res.getString("Date"));
			Integer rating = Integer.valueOf(res.getString("rating"));
			returnVal.insert(d, rating);
		}

		return returnVal;
	}


	private ResultSet getDataFromDB(String restaurantName) {
		
		return null;
	}

	private Date parseSQLDate(String dateStr) {
		dateStr = dateStr.trim();
		String[] fields = dateStr.split("-");
        if (fields.length != 3) {
            throw new RuntimeException("Date parse error");
        }
        Integer year = Integer.parseInt(fields[0]);
        Integer month   = Integer.parseInt(fields[1]);
        Integer day  = Integer.parseInt(fields[2]);
        return new Date(month, day, year);
	}




}
