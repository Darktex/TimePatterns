package testuggine.timepatterns.src;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.TreeMap;


import edu.princeton.cs.introcs.Out;


/** This class takes a restaurant into account, and prints the time patterns of its history */
public class TimePatterns {
	MySQLConnection db;
	TreeMap<Date, Pair<Double, Integer>> result;
	// For each (starting) date, 
	// you have a mean val (double) and the number of reviews (Int)
	
	public TimePatterns(String restaurant_id) throws NumberFormatException, SQLException, DomainTooShortException {
		db = new MySQLConnection();
		ResultSet res = getRestaurantDataFromDB(restaurant_id);
		TimeStampedRatingMap domain = setupDomain(res);
		DateMovingAvg filter = new DateMovingAvg(domain);
		result = filter.computeBatch();
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


	private ResultSet getRestaurantDataFromDB(String restaurant_id) throws SQLException {
		String dataQuery = "SELECT * FROM  TripAdvisorReview WHERE  restaurant_id =  ?";
		PreparedStatement statement = db.con
				.prepareStatement(dataQuery);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return res;
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

	/** Format:
	 * 
	 *  rest_site rest_id startDate endDate avg count
	 *  ex:
	 *  Yelp Cesca1291783306 04/19/12 11/19/12 3.7534 14
	 * 
	 *  */
	public void writeData() {
		Out out = new Out("/Users/davide/Documents/UCSB/Statistics Report/weeklyAvg.txt");
		out.println("website restaurantID starDate endDate mean count");
		for (Entry<Date, Pair<Double, Integer>> entry  : result.entrySet()) {
			Date start = entry.getKey();
			Date end = start.advance(7);
			float mean = DateMovingAvg.truncate(entry.getValue().first);
			int count = entry.getValue().second;
			out.println("Yelp" + " " + restaurant_id + " " + 
			start + " " + end + " " + mean + " " + count);
		}
	}



}
