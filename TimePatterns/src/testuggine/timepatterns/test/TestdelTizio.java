package testuggine.timepatterns.test;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import product.Deque;

/**
 * @author <a href="mailto:antoinegrondin@gmail.com">Antoine Grondin</a>
 *
 */
public class TestdelTizio extends TestCase{

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    // Statics
    private final static int MAX_PROBLEM_SIZE = 10000;

    // Members
    private Deque<String> mDequeue;

    // Constructors

    @Before
    public void setUp() throws Exception {
        mDequeue = new Deque<String>();
    }

    @After
    public void tearDown() throws Exception {
        mDequeue = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testDeque() {
        assertNotNull( mDequeue );
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Initialized queue should be empty", mDequeue.isEmpty());
    }

    @Test
    public void testIsEmptyAfterAddRemoveFirst() {
        mDequeue.addFirst("Something");
        boolean empty = mDequeue.isEmpty();
        assertFalse( empty );
        mDequeue.removeFirst();

        empty = mDequeue.isEmpty();
        assertTrue("Should be empty after adding then removing", 
                empty);

    }

    @Test
    public void testIsEmptyAfterAddRemoveLast() {
        mDequeue.addLast("Something");
        assertFalse(mDequeue.isEmpty());
        mDequeue.removeLast();
        assertTrue("Should be empty after adding then removing", 
                mDequeue.isEmpty());

    }

    @Test
    public void testIsEmptyAfterAddFirstRemoveLast() {
        mDequeue.addFirst("Something");
        assertFalse(mDequeue.isEmpty());
        mDequeue.removeLast();
        assertTrue("Should be empty after adding then removing", 
                mDequeue.isEmpty());
    }

    @Test
    public void testIsEmptyAfterAddLastRemoveFirst() {
        mDequeue.addLast("Something");
        assertFalse(mDequeue.isEmpty());
        mDequeue.removeFirst();
        assertTrue("Should be empty after adding then removing", 
                mDequeue.isEmpty());
    }

    @Test
    public void testIsEmptyAfterMultipleAddRemove(){
        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst("Something");
            assertFalse("Should not be empty after " + i + " item added", 
                    mDequeue.isEmpty());
        }

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            assertFalse("Should not be empty after " + i + " item removed", 
                    mDequeue.isEmpty());
            mDequeue.removeLast();
        }

        assertTrue( "Should be empty after adding and removing " 
                + MAX_PROBLEM_SIZE + " elements.", 
                mDequeue.isEmpty() );
    }

    @Test
    public void testMultipleFillAndEmpty(){
        for(int tries = 0; tries < 50; tries++){
            for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
                mDequeue.addFirst(String.valueOf(i));
            }

            assertFalse( mDequeue.isEmpty() );
            int i = 0;
            while( !mDequeue.isEmpty() ){
                assertEquals( String.valueOf(i), mDequeue.removeLast() );
                i++;
            }

            assertTrue( mDequeue.isEmpty() );

            for(int j = 0; j < MAX_PROBLEM_SIZE; j++){
                mDequeue.addLast(String.valueOf(j));
            }

            assertFalse( mDequeue.isEmpty() );
            
            i = 0;
            while( !mDequeue.isEmpty() ){
                assertEquals( String.valueOf(i), mDequeue.removeFirst() );
                i++;
            }

            assertTrue( mDequeue.isEmpty() );
        }
    }

    @Test
    public void testSize() {
        assertEquals( 0, mDequeue.size() );
        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst("Something");
            assertEquals(i+1, mDequeue.size() );
        }

        for(int i = MAX_PROBLEM_SIZE; i > 0; i--){
            assertEquals(i, mDequeue.size() );
            mDequeue.removeLast();
        }

        assertEquals( 0, mDequeue.size() );
    }

    @Test
    public void testAddFirst() {
        String[] aBunchOfString = {
                "One",
                "Two",
                "Three",
                "Four"
        };

        for(String aString : aBunchOfString){
            mDequeue.addFirst(aString);
        }

        for(int i = aBunchOfString.length - 1; i >= 0; i--){
            assertEquals(aBunchOfString[i], mDequeue.removeFirst());
        }
    }

    @Test
    public void testAddLast() {
        String[] aBunchOfString = {
                "One",
                "Two",
                "Three",
                "Four"
        };

        for(String aString : aBunchOfString){
            mDequeue.addLast(aString);
        }

        for(int i = aBunchOfString.length - 1; i >= 0; i--){
            assertEquals(aBunchOfString[i], mDequeue.removeLast());
        }
    }

    @Test
    public void testAddNull(){
        try {
            mDequeue.addFirst(null);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException npe){
            // Continue
        } catch (Exception e){
            fail("Wrong exception catched." + e);
        }

        try {
            mDequeue.addLast(null);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException npe){
            // Continue
        } catch (Exception e){
            fail("Wrong exception catched." + e);
        }
    }

    @Test
    public void testRemoveFirst() {
        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst( String.valueOf(i) );
            assertEquals(String.valueOf(i), mDequeue.removeFirst());
        }

        mDequeue = new Deque<String>();

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addLast( String.valueOf(i) );
            assertEquals(String.valueOf(i), mDequeue.removeFirst());
        }

        mDequeue = new Deque<String>();

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addLast( String.valueOf(i) );
        }

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            assertEquals(String.valueOf(i), mDequeue.removeFirst());
        }

    }

    @Test
    public void testRemoveLast() {
        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst( String.valueOf(i) );
            assertEquals(String.valueOf(i), mDequeue.removeLast());
        }

        mDequeue = new Deque<String>();

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addLast( String.valueOf(i) );
            assertEquals(String.valueOf(i), mDequeue.removeLast());
        }

        mDequeue = new Deque<String>();

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst( String.valueOf(i) );
        }

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            assertEquals(String.valueOf(i), mDequeue.removeLast());
        }
    }

    @Test
    public void testRemoveEmpty() {
        try {
            assertTrue(mDequeue.isEmpty());
            mDequeue.removeFirst();
            fail("Expected a NoSuchElementException");
        } catch ( NoSuchElementException nsee){
            // Continue
        } catch ( Exception e ){
            fail( "Unexpected exception : " + e );
        }

        try {
            assertTrue(mDequeue.isEmpty());
            mDequeue.removeLast();
            fail("Expected a NoSuchElementException");
        } catch ( NoSuchElementException nsee){
            // Continue
        } catch ( Exception e ){
            fail( "Unexpected exception : " + e );
        }

        try {
            assertTrue(mDequeue.isEmpty());

            for(int i = 0; i < MAX_PROBLEM_SIZE; i ++ ){
                mDequeue.addLast( String.valueOf(i) );
            }
            for(int i = 0; i < MAX_PROBLEM_SIZE; i ++ ){
                mDequeue.removeLast();
            }
            mDequeue.removeLast();
            fail("Expected a NoSuchElementException");
        } catch ( NoSuchElementException nsee){
            // Continue
        } catch ( Exception e ){
            fail( "Unexpected exception : " + e );
        }
    }

    @Test
    public void testIterator() {

        Iterator<String> anIterator = mDequeue.iterator();
        assertFalse( anIterator.hasNext() );

        for(int i = 0; i < MAX_PROBLEM_SIZE; i++){
            mDequeue.addFirst( String.valueOf(i) );
        }

        anIterator = mDequeue.iterator();

        assertTrue( anIterator.hasNext() );

        int i = MAX_PROBLEM_SIZE - 1;
        for(String aString : mDequeue){
            assertEquals( String.valueOf(i), aString);
            i--;
        }

        anIterator = mDequeue.iterator();

        assertTrue( anIterator.hasNext() );

        int j = MAX_PROBLEM_SIZE - 1;
        while( anIterator.hasNext() ){
            assertEquals( String.valueOf(j), anIterator.next());
            j--;
        }
    }

    @Test
    public void testIteratorNoMoreItem() {
        Iterator<String> anIterator = mDequeue.iterator();
        while( anIterator.hasNext() ){
            anIterator.next();
        }
        try {
            anIterator.next();
            fail( "Should have thrown a NoSuchElementException.");
        } catch( NoSuchElementException nsee ){
            // Continue
        } catch( Exception e ){
            fail( "Should have thrown a NoSuchElementException, but received" +
                    " : " + e);
        }
    }

    @Test
    public void testIteratorRemoveNotSupported() {
        Iterator<String> anIterator = mDequeue.iterator();
        try {
            anIterator.remove();
            fail("Should have thrown an UnsupportedOperationException");
        } catch ( UnsupportedOperationException uoe ){
            // Continue
        } catch ( Exception e ){
            fail( "Unexpected exception : " + e);
        }
    }

    @Test
    public void testMultipleIterator(){
        for(int i = 0; i < MAX_PROBLEM_SIZE/1000; i++){
            
            mDequeue = new Deque<String>();
            for(int j = 0; j < i; j++){
                mDequeue.addLast( String.valueOf(j) );
            }
            
            @SuppressWarnings("rawtypes")
            Iterator[] someIterators = {
                    mDequeue.iterator(),
                    mDequeue.iterator(),
                    mDequeue.iterator(),
                    mDequeue.iterator(),
                    mDequeue.iterator(),
                    mDequeue.iterator()
            };
            
            @SuppressWarnings("unchecked")
            Iterator<String>[] manyStringIterators = 
            (Iterator<String>[]) someIterators;
            
            for(int iterID = 0; iterID < manyStringIterators.length; iterID++){
                int index = 0;
                while( manyStringIterators[iterID].hasNext() ){
                    assertEquals( "Iterator #" + iterID + " failed:\n",
                            String.valueOf(index), 
                            manyStringIterators[iterID].next());
                    index++;
                }
            }
            
        }
    }
    
    @Test
    public void testQueueBehavior(){

        String[] aBunchOfString = {
                "One", "Two", "Three", "Four"
        };

        for(String aString : aBunchOfString){
            mDequeue.addFirst(aString);
        }

        for(String aString : aBunchOfString){
            assertEquals(aString, mDequeue.removeLast());
        }
    }

    @Test
    public void testStackBehavior(){

        String[] aBunchOfString = {
                "One", "Two", "Three", "Four"
        };

        for(String aString : aBunchOfString){
            mDequeue.addFirst(aString);
        }

        for(int i = aBunchOfString.length - 1; i >= 0; i--){
            assertEquals(aBunchOfString[i], mDequeue.removeFirst());
        }
    }
}