package testuggine.timepatterns.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.StdRandom;
import testuggine.timepatterns.src.DateMovingAvg;
import testuggine.timepatterns.src.TimeStampedRatingMap;

import static java.util.Arrays.asList;

public class TestDateMovingAvg extends TestCase {

	// Members
	private TimeStampedRatingMap map;
	private ArrayList<Integer> whatIput;
	private DateMovingAvg d;

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
	
	/** Deterministic version for development */
//	public void setUp() throws Exception {
//	map = new TimeStampedRatingMap();
//	
//	Date d = new Date(4, 10, 2012);
//	ArrayList<Integer> f = new ArrayList<Integer>(asList(3292, 4735, 2346, 8180, 5560, 8213, 4828));
//	
//	map.insert(d, f);
//	
//	d = d.next();
//	ArrayList<Integer> q = new ArrayList<Integer>(asList(6619, 1002));
//	map.insert(d, q);
//	
//	d = d.next(); // il 14 non ci sono valori
//	
//	d = d.next();
//	ArrayList<Integer> m = new ArrayList<Integer>(asList(2156, 5544, 5914, 8702, 1175));
//	map.insert(d, m);
//	
//	d = d.next();
//	ArrayList<Integer> diocan = new ArrayList<Integer>(asList(4274, 4745, 5417));
//	map.insert(d, diocan);
//	
//	d = d.next();
//	ArrayList<Integer> canaia = new ArrayList<Integer>(asList(4728));
//	map.insert(d, canaia);
//	
//	d = d.next();
//	ArrayList<Integer> dedios = new ArrayList<Integer>(asList(6110, 5066, 7864, 691, 7042, 385, 5284));
//	map.insert(d, dedios);
//	
//	d = d.next(); // il 17 manco
//	
//	d = d.next();
//	ArrayList<Integer> mariacagna = new ArrayList<Integer>(asList(8919, 1141, 2901, 6361));
//	map.insert(d, mariacagna);
//	
//	d = d.next();
//	ArrayList<Integer> madonnatroiagravida = new ArrayList<Integer>(asList(2400, 3200, 6012, 732, 8708, 7103, 5619, 493));
//	map.insert(d, madonnatroiagravida);
//	
//	d = d.next();
//	ArrayList<Integer> gesuricchione = new ArrayList<Integer>(asList(2163, 7741));
//	map.insert(d, gesuricchione);
//	
//	d = d.next();
//	ArrayList<Integer> madonnasincopata = new ArrayList<Integer>(asList(7393, 5994, 4263, 2594, 9305, 2378));
//	map.insert(d, madonnasincopata);
//	
//	d = d.next();
//	ArrayList<Integer> germanomosconi = new ArrayList<Integer>(asList(4416, 7772, 6345, 6014));
//	map.insert(d, germanomosconi);
//	
//	d = d.next();
//	ArrayList<Integer> quelmonachesbattelaporta = new ArrayList<Integer>(asList(5635, 5724, 169, 7494, 7715));
//	map.insert(d, quelmonachesbattelaporta);
//	
//	d = d.next();
//	ArrayList<Integer> elachiudeurlando = new ArrayList<Integer>(asList(6404, 6549));
//	map.insert(d, elachiudeurlando);	
//	
//}
	

	@After
	public void tearDown() throws Exception {
		map = null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Tests
	// /////////////////////////////////////////////////////////////////////////

	@Test
	public void testMeanCoherence() throws Exception {
		Date filterStart = new Date(4, 10, 2012);
		Date filterEnd = new Date(4, 17, 2012);
		
		Date listStart = new Date(4, 10, 2012);
		Date listEnd = new Date(4, 17, 2012);
		
		d = new DateMovingAvg(map, filterStart);
		ArrayList<Integer> l = new ArrayList<Integer>();
		l = map.flattenInterval(listStart, listEnd); // 1 after the end
		float wholeList = TimeStampedRatingMap.listAverage(l);
		float filter = d.getCurrentMeanFloat();
		StdOut.println("OUT OF LOOP l = " + l +
		"\nd = " + d + " and with mean(l) = " + wholeList + ", mean(d) = " + 
		filter +".\n");
		
		assertEquals(wholeList, filter);
		for (int i = 0; i < 3; i++) {
			listStart = listStart.next();
			listEnd = listEnd.next();
			l = map.flattenInterval(listStart, listEnd); // 1 after the end
			wholeList = TimeStampedRatingMap.listAverage(l);
			filter = d.advance();
			StdOut.println("Test failed for i = " + i + ", l = " + l +
					", d = " + d + " and with mean(l) = " + wholeList + ", mean(d) = " + 
					filter +".\n");
			assertEquals("Test failed for i = " + i + ", l = " + l +
					", d = " + d + " and with mean(l) = " + wholeList + ", mean(d) = " + 
					filter +".\n", wholeList, filter);
		}
			
	}
	
}
