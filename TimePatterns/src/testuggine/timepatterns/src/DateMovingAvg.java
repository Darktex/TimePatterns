package testuggine.timepatterns.src;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


import edu.princeton.cs.algs4.Date;

public class DateMovingAvg {
	TimeStampedRatingMap domain;
	Double weeklyMean;
	Integer weeklyRatingsCount;
	Date currentStartingDay;
	Date currentEndingDay;
	
	TreeMap<Date, Pair<Double, Integer>> results;
	
	public DateMovingAvg(TimeStampedRatingMap domain, Date startingDate) throws DomainTooShortException {
		currentStartingDay = startingDate;
		currentEndingDay = weekLater(startingDate);
		this.domain = domain;
		if (!domain.isLongerThanAWeek()) throw new DomainTooShortException("The domain contains less than a week of activity");
		TimeStampedRatingMap weeklyRatings = domain.subMap(currentStartingDay, currentEndingDay);
		weeklyRatingsCount = weeklyRatings.ratingsCount();
		weeklyMean = weeklyRatings.allElementsAvgDouble();
		results = new TreeMap<Date, Pair<Double, Integer>>();
		Pair<Double, Integer> pair = Pair.of(weeklyMean, weeklyRatingsCount);
		results.put(currentStartingDay, pair);
	}
	
	public DateMovingAvg(TimeStampedRatingMap domain) throws DomainTooShortException {
		this.domain = domain;
		Iterator<Map.Entry<Date, ArrayList<Integer>>> it = domain.iterator();
		currentStartingDay = it.next().getKey();
		currentEndingDay = weekLater(currentStartingDay);
		if (!domain.isLongerThanAWeek()) throw new DomainTooShortException("The domain contains less than a week of activity");
		TimeStampedRatingMap weeklyRatings = domain.subMap(currentStartingDay, currentEndingDay);
		weeklyRatingsCount = weeklyRatings.ratingsCount();
		weeklyMean = weeklyRatings.allElementsAvgDouble();
		results = new TreeMap<Date, Pair<Double, Integer>>();
		Pair<Double, Integer> pair = Pair.of(weeklyMean, weeklyRatingsCount);
		results.put(currentStartingDay, pair);
	}
	
	public float advance() {
		Date prev = currentStartingDay;
		currentStartingDay = currentStartingDay.next();
		
		ArrayList<Integer> old;
		Integer old_count;
		Double old_mean;
		
		if (domain.contains(prev)) {
			old = domain.getRatings(prev);
			old_count = old.size();
			old_mean = TimeStampedRatingMap.listAverageDouble(old);
		}
		else {
			old = null;
			old_count = 0;
			old_mean = 0.0;
		}
	
		ArrayList<Integer> newDay;
		Integer new_count;
		double new_mean;
		
		if (domain.contains(currentEndingDay)) { 
			newDay = domain.getRatings(currentEndingDay);
			new_count = newDay.size();
			new_mean = TimeStampedRatingMap.listAverage(newDay);
		}
		else {
			newDay = null;
			new_count = 0;
			new_mean = 0.0;
		}
		
		
		Integer oldWeekCount = weeklyRatingsCount;
		weeklyRatingsCount = oldWeekCount - old_count + new_count;
		weeklyMean = (new_count * new_mean 
				+ oldWeekCount * weeklyMean - old_count * old_mean) / weeklyRatingsCount;
		
		currentEndingDay = currentEndingDay.next();
		Pair<Double, Integer> pair = Pair.of(weeklyMean, weeklyRatingsCount);
		results.put(currentStartingDay, pair);
		return truncate (weeklyMean);
	}
	
	public TreeMap<Date, Pair<Double, Integer>> computeBatch() {
		while (currentEndingDay.compareTo(domain.map.lastKey()) < 0) // advance over all the domain
			advance();
		return results; // return the result
	}
	
	public double getCurrentMean() {
		return weeklyMean;
	}
	
	public float getCurrentMeanFloat() {
		return truncate(weeklyMean);
	}
	
	public static float truncate(double num) {
		return (float) (Math.round(num*10000.0)/10000.0);
	}
	
	public static Date weekLater(Date start) {
		Date retval = new Date(start.toString());
		for (int i = 0; i < 7; i++)
			retval = retval.next();
		return retval;
	}

	@Override
	public String toString() {
		return "DateMovingAvg [domain=" + domain + "weeklyRatings="
				+ domain.subMap(currentStartingDay, currentEndingDay) + ", weeklyMean=" + weeklyMean + "]";
	}
		
}
