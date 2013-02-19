package testuggine.timepatterns.src;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.princeton.cs.introcs.Out;

/**
 * This class takes a restaurant into account, and prints the time patterns of
 * its history
 */
public class TimePatterns {
	MySQLConnection db;

	// For each (starting) date,
	// you have a mean val (double) and the number of reviews (Int)

	public TimePatterns() throws Exception {
		db = new MySQLConnection();
		ArrayList<String> Yelp = getAllSuitableRestaurants("Yelp");
		ArrayList<String> TripAdvisor = getAllSuitableRestaurants("TripAdvisor");
		ArrayList<String> OpenTable = getAllSuitableRestaurants("OpenTable");
		
		for (String restaurant_id : Yelp) {
			TimeStampedRatingMap T = getTimeStampedTable("Yelp", restaurant_id);
			DateMovingAvg filter = new DateMovingAvg(T);
			writeData("Yelp", restaurant_id, filter.computeBatch());
		}
		
		for (String restaurant_id : TripAdvisor) {
			TimeStampedRatingMap T = getTimeStampedTable("TripAdvisor", restaurant_id);
			DateMovingAvg filter = new DateMovingAvg(T);
			writeData("TripAdvisor", restaurant_id, filter.computeBatch());
		}
		
		for (String restaurant_id : OpenTable) {
			TimeStampedRatingMap T = getTimeStampedTable("OpenTable", restaurant_id);
			DateMovingAvg filter = new DateMovingAvg(T);
			writeData("OpenTable", restaurant_id, filter.computeBatch());
		}
	}

	private TimeStampedRatingMap constructMap(ResultSet res)
			throws NumberFormatException, SQLException {
		TimeStampedRatingMap returnVal = new TimeStampedRatingMap();

		while (res.next()) {
			Date d = parseSQLDate(res.getString("Date"));
			Integer rating = Integer.valueOf(res.getString("rating"));
			returnVal.insert(d, rating);
		}

		return returnVal;
	}

	private Date parseSQLDate(String dateStr) {
		dateStr = dateStr.trim();
		String[] fields = dateStr.split("-");
		if (fields.length != 3) {
			throw new RuntimeException("Date parse error");
		}
		Integer year = Integer.parseInt(fields[0]);
		Integer month = Integer.parseInt(fields[1]);
		Integer day = Integer.parseInt(fields[2]);
		return new Date(month, day, year);
	}

	private ArrayList<String> getAllSuitableRestaurants(String website) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		String query = "select id from " + website + "TopRestaurant";
		PreparedStatement statement = db.con.prepareStatement(query);
		ResultSet res = statement.executeQuery();
		while (res.next()) {
			String id = res.getString("id");
			result.add(id);
		}
		return result;
	}

	public TimeStampedRatingMap getTimeStampedTable(String website,
			String restaurant_id) throws Exception {
		if (website.equals("TripAdvisor"))
			return getTATable(restaurant_id);
		else if (website.equals("Yelp"))
			return getYTable(restaurant_id);
		else if (website.equals("OpenTable"))
			return getOTTable(restaurant_id);
		else
			throw new Exception(website + " is not a valid website "
					+ "(only TripAdvisor, Yelp and OpenTable are legal)");
	}

	private TimeStampedRatingMap getOTTable(String restaurant_id)
			throws SQLException {
		String query = "select restaurant_id, date, overallRating as rating"
				+ "from TripAdvisorReview" + "where restaurant_id = ?";
		PreparedStatement statement = db.con.prepareStatement(query);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return constructMap(res);
	}

	private TimeStampedRatingMap getYTable(String restaurant_id)
			throws SQLException {
		String query = "select restaurant_id, date, rating "
				+ "from TripAdvisorReview" + "where restaurant_id = ?";
		PreparedStatement statement = db.con.prepareStatement(query);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return constructMap(res);
	}

	private TimeStampedRatingMap getTATable(String restaurant_id)
			throws SQLException {
		String query = "select restaurant_id, date, globalRating as rating"
				+ "from TripAdvisorReview" + "where restaurant_id = ?";
		PreparedStatement statement = db.con.prepareStatement(query);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return constructMap(res);
	}

	/**
	 * Format:
	 * 
	 * rest_site rest_id startDate endDate avg count 
	 * ex: Yelp Cesca1291783306
	 * 04/19/12 11/19/12 3.7534 14
	 * @param result 
	 * 
	 * */
	public void writeData(String website, String restaurant_id, TreeMap<Date, Pair<Double, Integer>> result) {
		Out out = new Out(
				"/Users/davide/Documents/UCSB/Statistics Report/weeklyAvg.txt");
		out.println("website restaurantID startDate center endDate mean count");

		for (Entry<Date, Pair<Double, Integer>> entry : result.entrySet()) {
			Date start = entry.getKey();
			Date end = start.advance(7);
			float mean = DateMovingAvg.truncate(entry.getValue().first);
			int count = entry.getValue().second;
			out.println(website + " " + restaurant_id + " " + start + " " + start.advance(3) + " " 
			+ end + " " + mean + " " + count);
		}
	}

}
