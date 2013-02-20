package testuggine.timepatterns.src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import testuggine.timepatterns.src.Date;


public class TimeStampedRatingMap implements Iterable<Map.Entry<Date, ArrayList<Integer>>> {

	SortedMap<Date, ArrayList<Integer>> map;
	int ratingsCount; // number of Integers contained inside this map
	
	public TimeStampedRatingMap() {
		// TODO Consider LinkedHashMap + query already sorted on date.
		map = new TreeMap<Date, ArrayList<Integer>>(); // using TreeMap because it's sorted
		ratingsCount = 0;
	}
	
	public TimeStampedRatingMap(TimeStampedRatingMap map) {
		// TODO Consider LinkedHashMap + query already sorted on date.
		this.map = new TreeMap<Date, ArrayList<Integer>>(map.map); // using TreeMap because it's sorted
		this.ratingsCount = map.ratingsCount;
	}
	
	public TimeStampedRatingMap(SortedMap<Date, ArrayList<Integer>> map) {
		this.map = map;
		ArrayList<Integer> elems = new ArrayList<Integer>();
		for (Iterator<Entry<Date, ArrayList<Integer>>> iterator = map.entrySet().iterator(); 
				iterator.hasNext();) {
			Entry<Date, ArrayList<Integer>> entry = iterator.next();
			elems.addAll(entry.getValue());
		}
		this.ratingsCount = elems.size(); 
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
		ratingsCount++;
	}
	
	/** Lets you batch insert elements from an ArrayList. If a list was already there,
	 *  the elements of the input list get concatenated to the already present list */
	public void insert(Date d, ArrayList<Integer> vs) {
			if (!map.containsKey(d)) {
				map.put(d, vs);
			}
			else 
				map.get(d).addAll(vs);
			ratingsCount += vs.size();
	}
	
	
	/** Computes the mean of ratings for the day using Sedgewick's StdStats.
	 * 	Too bad StdStats.mean() accepts only a int[] and Java can't unbox
	 *  Integer[] to int[].
	 *  */
	public float dailyAvg(Date key) {
		ArrayList<Integer> ratings = getRatings(key);
		return listAverage(ratings);
	}
	
	/** Concatenates all ratings from all lists in an interval in a single List<Int> */
	public ArrayList<Integer> flattenInterval(Date start, Date end) {
		ArrayList<Integer> elems = new ArrayList<Integer>();
		for (Iterator<Entry<Date, ArrayList<Integer>>> iterator = this.subMap(start, end)
				.iterator(); iterator.hasNext();) {
			Entry<Date, ArrayList<Integer>> entry = iterator.next();
			elems.addAll(entry.getValue());
		}
		return elems;
	}
	
	public float intervalAvg(Date start, Date end) {
		ArrayList<Integer> flatList = flattenInterval(start, end);
		return listAverage(flatList);
	}
	
	public float allElementsAvg() {
		Date start = map.firstKey();
		Date end = map.lastKey();
		return intervalAvg(start, end);
	}
	
	public double allElementsAvgDouble() {
		Date start = map.firstKey();
		Date end = map.lastKey();
		ArrayList<Integer> flatList = flattenInterval(start, end.next());
		return listAverageDouble(flatList);
	}
	
	
	
	public int dailyReviews(Date key) {
		return getRatings(key).size();
	}
	
	/** Removes an element from the map and returns it if it was there
	 * otherwise it will return null */
	public ArrayList<Integer> remove(Date key) {
		ratingsCount -= this.getRatings(key).size();
		return map.remove(key);
	}
	
	public int ratingsCount() {
		return ratingsCount;
	}
	
	public static float listAverage(ArrayList<Integer> list) {
		float sum = 0;
		for (Integer elem : list)
			sum += elem;
		return sum / list.size();
	}
	
	public static double listAverageDouble(ArrayList<Integer> list) {
		double sum = 0;
		for (Integer elem : list)
			sum += elem;
		return sum / list.size();
	}
	
	public TimeStampedRatingMap subMap(Date start, Date end) {
		TimeStampedRatingMap result = new TimeStampedRatingMap(map.subMap(start, end));
		return result;
	}

	public Iterator<Map.Entry<Date, ArrayList<Integer>>> iterator() {
		return map.entrySet().iterator(); // Already sorted
	}

	@Override
	public String toString() {
		return "TimeStampedRatingMap [map=" + map + "]";
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

	public boolean isLongerThanAWeek() {
		if (map.isEmpty()) return false;
		Date last = map.lastKey();
		Date first = map.firstKey();
		for (int i = 0; i < 7; i++)
			first = first.next();
		return last.compareTo(first) > 0;
	}
	
	
}
