package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        /*System.out.println("Make sure to uncomment the lines below (and delete this print statement).");*/

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addLast("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

        lld1.addFirst("have");
        lld1.removeFirst();
		System.out.println("Printing out deque: ");
		lld1.printDeque();
        //lld1.removeLast();
        System.out.println("Printing out deque: ");
        lld1.printDeque();
        lld1.addLast("west");
        lld1.addLast("east");
        lld1.addLast("south");
        lld1.removeLast();

        assertEquals("middle", lld1.get(1));
        assertEquals("east", lld1.get(4));
        assertEquals(null, lld1.get(5));

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {


        /*
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		//lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.addLast(7);
        //lld1.removeLast();
        //assertEquals(Integer.valueOf(2), lld1.get(1));
        */

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        /*System.out.println("Make sure to uncomment the lines below (and delete this print statement).");*/

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        /*System.out.println("Make sure to uncomment the lines below (and delete this print statement).");*/

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }


    @Test
    /* test max method of maxarraydeque */
    public void maxTest(){
        

        /* comparator is abs function*/
         class absComparator implements Comparator<Integer>{
            @Override
            public int compare(Integer a, Integer b){
                return Math.abs(a) - Math.abs(b);
            }
        }
        /* comparator is to find the length of string*/
        class strLenComparator implements Comparator<String>{
            @Override
            public int compare(String a, String b){
                return a.length() - b.length();
            }
        }
        /* find the sum_digit max */
        class MaxsumDigitComparator implements Comparator<Integer>{
            @Override
            public int compare(Integer a, Integer b){
                return sumdigit(a) - sumdigit(b);
            }
        }

        Comparator<Integer> abscom = new absComparator();
        Comparator<Integer> sumcom = new MaxsumDigitComparator();
        MaxArrayDeque<Integer> M = new MaxArrayDeque<>(abscom);
        M.addLast(-1);
        M.addLast(1);
        M.addLast(2);
        M.addLast(3);
        M.addLast(-4);
        M.addLast(17); M.addLast(21);
        M.addLast(22);
        System.out.println(M.max());
        //System.out.println(M.max(absComparator));
        System.out.println(M.max(sumcom));
    }
    public int sumdigit(int a){
        int x = Math.abs(a);
        if(x < 10){
            return x;
        }else{
            return x % 10 + sumdigit(x / 10);
        }
    }
}
