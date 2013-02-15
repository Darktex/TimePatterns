package testuggine.timepatterns.test;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import testuggine.timepatterns.src.MovingAverage;

public class TestMovingAverage extends TestCase {
	
	// Members
    private MovingAverage movingAvg;

    // Constructors

    @Before
    public void setUp() throws Exception {
    	movingAvg = new MovingAverage(5);
    }

    @After
    public void tearDown() throws Exception {
        movingAvg = null;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Tests
    ///////////////////////////////////////////////////////////////////////////

    @Test
	public void testIsBootstrapped() {
		for (int i=0; i < movingAvg.size()-1; i++) {
			movingAvg.advance(1);
			assertFalse("Says it's bootstrapped but it isn't", movingAvg.isBootstrapped());
		}
		
		for (int i=0; i < 100; i ++)
			movingAvg.advance(1);
		assertTrue(movingAvg.isBootstrapped());
	}
    
	@Test
	public void testAdvance() {
		while (!movingAvg.isBootstrapped()) {
			float rand = (float) StdRandom.random();
			float a = movingAvg.advance(rand);
		}
		for (int i = 0; i < 1000; i++) {
			float rand = (float) StdRandom.random();
			movingAvg.advance(rand);
		}
		float rand = (float) StdRandom.random();
		float q = movingAvg.advance(rand);
		float mean = (float) (StdStats.mean(movingAvg.a()));
		assertEquals(MovingAverage.truncate(q), MovingAverage.truncate(mean));
	}

}
