package testuggine.timepatterns.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.StdRandom;
import testuggine.timepatterns.src.TimeStampedRatingMap;
import testuggine.timepatterns.src.Date;

public class testTimeStampedRatingMap extends TestCase {

	// Members
	private TimeStampedRatingMap map;
	private ArrayList<Integer> whatIput;

	// Constructors

	@Before
	public void setUp() throws Exception {
		map = new TimeStampedRatingMap();
		whatIput = new ArrayList<Integer>();
		
		Date d = new Date(4, 10, 2012);
		for (int j = 0; j < 15; j++, d = d.next()) { // 15 days max
			int q = StdRandom.uniform(10);
			for (int i = 0; i < q; i++) {
				Integer rand = StdRandom.uniform(10000);
				map.insert(d, rand);
				whatIput.add(rand);
			}
		}

	}

	@After
	public void tearDown() throws Exception {
		map = null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Tests
	// /////////////////////////////////////////////////////////////////////////

	@Test
	public void testSizeCoherence() {
		ArrayList<Integer> l = new ArrayList<Integer>();
		l = map.flattenInterval(new Date(4, 10, 2012), new Date(4, 25, 2012)); // 1 after the end
		assertEquals(l.size(), map.ratingsCount());
	}
	
	public void testFinalDataCoherence() {
		ArrayList<Integer> l = new ArrayList<Integer>();
		l = map.flattenInterval(new Date(4, 10, 2012), new Date(4, 25, 2012)); // 1 after the end
		assertEquals(l, whatIput);
		
		
		
		StdOut.println("l = "+ l);
		StdOut.println("whatIput = " + whatIput);
		StdOut.println("This one " + map);
	}
}
