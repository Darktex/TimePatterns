package testuggine.timepatterns.src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.introcs.StdStats;

public class DateMovingAvg {
	int N; // number of elements it contains
	int windowSize;
	Date currentStartingDay;
	Date currentEndingDay;
	MovingAverage movingAvg;
	TimeStampedRatingMap domain;
	
	
	public DateMovingAvg(int windowSize, TimeStampedRatingMap domain, Date startingDate) {
		this.N = 0;
		this.windowSize = windowSize;
		this.movingAvg = new MovingAverage(windowSize);
		currentStartingDay = startingDate;
		currentEndingDay = weekLater(currentStartingDay);
		this.domain = domain;
	}
	
	public DateMovingAvg(int windowSize, TimeStampedRatingMap domain) {
		this.domain = domain;
		Iterator<Entry<Date, List<Integer>>> it = domain.iterator();
		currentStartingDay = it.next().getKey();
		currentEndingDay = weekLater(currentStartingDay);
		this.N = 0;
		this.windowSize = windowSize;
		this.movingAvg = new MovingAverage(windowSize);
	}
	
	public boolean isBootStrapped() {
		return movingAvg.isBootstrapped();
	}
	
	public float advance() {
		Date previousStartingDay = currentStartingDay;
		currentStartingDay = currentStartingDay.next();
		currentEndingDay = currentEndingDay.next();
		float currentStartingMean = domain.dailyAvg(currentStartingDay);
		int currentStartingReviews = domain.dailyReviews(currentStartingDay);
		return movingAvg.advance(currentStartingMean);
		
	}
	
	private static Date weekLater(Date start) {
		Date retval = new Date(start.toString());
		for (int i = 0; i < 7; i++)
			retval = retval.next();
		return retval;
	}
	

	private class MovingAverage {
		float[] a;
		int N; // size of moving avg filter
		int i;
		float sum;
		
		public MovingAverage(int N) {
			this.N = N;
			this.i = 0;
			this.a = new float[N];
			this.sum = 0;
		}
		/** Advances && returns the new running avg */
		public float advance(float newElement) {
	            sum -= a[i % N];
	            a[i % N] = newElement;
	            sum += a[i % N];
	            i++;
	            return sum / N;
		}
		public int size() {
			return N;
		}
		/** If less than N values were inserted, the filter is not ready */
		public boolean isBootstrapped() {
			return i >= N;
		}
	}
}
