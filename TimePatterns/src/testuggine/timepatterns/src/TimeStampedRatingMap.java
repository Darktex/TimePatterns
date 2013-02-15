package testuggine.timepatterns.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.introcs.StdStats;

public class TimeStampedRatingMap implements Iterable<Map.Entry<Date, ArrayList<Integer>>> {

	SortedMap<Date, ArrayList<Integer>> map;
	
	public TimeStampedRatingMap() {
		// TODO Consider LinkedHashMap + query already sorted on date.
		map = new TreeMap<Date, ArrayList<Integer>>(); // using TreeMap because it's sorted
	}
	
	public ArrayList<Integer> getRatings(Date key) { // this throws the same exception from HashMap.
		return map.get(key);
	}
	
	public boolean contains(Date key) {
		return map.containsKey(key);
	}
	
	public void insert(Date d, Integer v) {
		if (!map.containsKey(d)) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			map.put(d, l);
		}
		map.get(d).add(v);	
	}
	
	/** Computes the mean of ratings for the day using Sedgewick's StdStats.
	 * 	Too bad StdStats.mean() accepts only a int[] and Java can't unbox
	 *  Integer[] to int[].
	 *  */
	public float dailyAvg(Date key) {
		ArrayList<Integer> ratings = getRatings(key);
		int[] javaIHateYou = new int[ratings.size()];
		for (int whyDoIHaveToDoIt = 0; whyDoIHaveToDoIt < ratings.size(); whyDoIHaveToDoIt++)
			javaIHateYou[whyDoIHaveToDoIt] = ratings.get(whyDoIHaveToDoIt);
			
		double intermediate = StdStats.mean(javaIHateYou);
		return (float) intermediate;
	}
	
	public int dailyReviews(Date key) {
		return getRatings(key).size();
	}

	public Iterator<Map.Entry<Date, ArrayList<Integer>>> iterator() {
		return map.entrySet().iterator(); // Already sorted
	}
}
