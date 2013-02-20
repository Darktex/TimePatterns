package testuggine.timepatterns.src;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class DateMovingAvg {
	TimeStampedRatingMap domain;
	Double weeklyMean;
	Integer weeklyRatingsCount;
	Date currentStartingDay;
	Date currentEndingDay;
	Date center; // the date around with this filter is centered
	
	TreeMap<Date, Pair<Double, Integer>> results;
	
	public DateMovingAvg(TimeStampedRatingMap domain, Date startingDate) throws DomainTooShortException {
		center = startingDate;
		currentStartingDay = center.goBack(3);
		currentEndingDay = center.advance(4); // 1 after the end
		
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
		if (!domain.isLongerThanAWeek()) throw new DomainTooShortException("The domain contains less than a week of activity");
		currentStartingDay = it.next().getKey();
		center = currentStartingDay.advance(3);
		currentEndingDay = currentStartingDay.advance(7);
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
		if (weeklyRatingsCount == 0) weeklyMean = 0.0;
		else weeklyMean = (new_count * new_mean 
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
	
	@Override
	public String toString() {
		return "DateMovingAvg [center= " + center + "\nweeklyRatings="
				+ domain.subMap(currentStartingDay, currentEndingDay) + "\nweeklyMean=" + weeklyMean + "\ndomain=" + domain + "]";
	}
		
}
