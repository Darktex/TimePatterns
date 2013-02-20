package testuggine.timepatterns.src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.princeton.cs.introcs.StdOut;

/**
 * This class takes a restaurant into account, and prints the time patterns of
 * its history
 */
public class TimePatterns {
	MySQLConnection db;
	String path;

	// For each (starting) date,
	// you have a mean val (double) and the number of reviews (Int)

	public TimePatterns(boolean wantYelp, boolean wantTA, boolean wantOT, String path) throws Exception  {
		db = new MySQLConnection();
		this.path = path;
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/Yelp.csv", false)));
		out.println("website\trestaurantID\tstartDate\tcenter\tendDate\tmean\tcount\tdeltaM\tdeltaC");
		out.close();
		
		out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/TripAdvisor.csv", false)));
		out.println("website\trestaurantID\tstartDate\tcenter\tendDate\tmean\tcount\tdeltaM\tdeltaC");
		out.close();
		
		out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/OpenTable.csv", false)));
		out.println("website\trestaurantID\tstartDate\tcenter\tendDate\tmean\tcount\tdeltaM\tdeltaC");
		out.close();
		
		ArrayList<String> container = new ArrayList<String>();
		
		if (wantYelp) {
			 container = getAllSuitableRestaurants("Yelp");
			 work("Yelp", container);
		}
		else container = null;
		
		if (wantTA) {
			container = getAllSuitableRestaurants("TripAdvisor");
			work("TripAdvisor", container);
		}
		else container = null;
		
		if (wantOT) {
			container = getAllSuitableRestaurants("OpenTable");
			work("OpenTable", container);
		}
		else container = null;
		
		
	}

	private void work(String website, ArrayList<String> container) throws Exception {
		for (String restaurant_id : container) {
			TimeStampedRatingMap T = getTimeStampedTable(website, restaurant_id);
			DateMovingAvg filter;
			try {
				filter = new DateMovingAvg(T);
				writeData(website, restaurant_id, filter.computeBatch());
			} catch (DomainTooShortException e) {
				StdOut.println("Restaurant " + restaurant_id + " from " + website);
				continue;
			}
			finally {
				filter = null;
				T = null;
			}
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
		String query = "select restaurant_id, date, overallRating as rating "
				+ "from OpenTableReview where restaurant_id = ?";
		PreparedStatement statement = db.con.prepareStatement(query);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return constructMap(res);
	}

	private TimeStampedRatingMap getYTable(String restaurant_id)
			throws SQLException {
		String query = "select restaurant_id, date, rating "
				+ "from YelpReview where restaurant_id = ?";
		PreparedStatement statement = db.con.prepareStatement(query);
		statement.setString(1, restaurant_id);
		ResultSet res = statement.executeQuery();
		return constructMap(res);
	}

	private TimeStampedRatingMap getTATable(String restaurant_id)
			throws SQLException {
		String query = "select restaurant_id, date, globalRating as rating "
				+ "from TripAdvisorReview where restaurant_id = ?";
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
	 * @throws IOException 
	 * 
	 * */
	public void writeData(String website, String restaurant_id, TreeMap<Date, Pair<Double, Integer>> result) throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/" + website + ".csv", true)));
		float prev_m = 0;
		int prev_c = 0;
		
		for (Entry<Date, Pair<Double, Integer>> entry : result.entrySet()) {
			Date start = entry.getKey();
			Date end = start.advance(7);
			float mean = DateMovingAvg.truncate(entry.getValue().first);
			int count = entry.getValue().second;
			
			float delta_m = mean - prev_m;
			int delta_c = count - prev_c;
			
			prev_m = mean;
			prev_c = count;
			
			if (count > 0 )
				out.println(website + "\t" + restaurant_id + "\t" + start + "\t" + start.advance(3) + "\t" 
			+ end + "\t" + mean + "\t" + count + "\t" + delta_m + "\t" + delta_c);
		}
		out.close();
	}
	

}
