package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    /*@Test
    public void testThreeAddThreeRemove(){
        BuggyAList<Integer> Bal = new BuggyAList<>();
        AListNoResizing<Integer> Anr = new AListNoResizing<>();
        for(int i = 0; i < 3; i += 1){
            Bal.addLast(i);
            Anr.addLast(i);
        }for(int j = 0; j < 3; j += 1){
            int x = Bal.removeLast();
            int y = Anr.removeLast();
            assertEquals(x, y);
        }
    }
    */
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> K = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                K.addLast(randVal);

                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                int sizeK = K.size();
                assertEquals(sizeL, sizeK);
                //System.out.println("size: " + sizeL);
            }else{
                if(L.size() > 0){
                    if(operationNumber == 2){
                        int x = L.getLast();
                        int y = K.getLast();
                        assertEquals(x, y);
                        //System.out.println("getLast(" + x +")");
                    }
                    else{
                        int x = L.removeLast();
                        int y = K.removeLast();
                        assertEquals(x, y);
                       // System.out.println("removeLast(" + x + ")");
                    }
                }
            }
        }
    }
}
