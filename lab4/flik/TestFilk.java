package flik;
import org.junit.Test;
import edu.princeton.cs.algs4.StdRandom;
import static flik.Flik.isSameNumber;
import static org.junit.Assert.assertTrue;

public class TestFilk {
    @Test
    public void FilkRandomTest(){
        assertTrue(isSameNumber(1, 1));
        assertTrue(!isSameNumber(2, 3));
        assertTrue((isSameNumber(2, 2)));
        /*for(int i = 0; i < 1000; i += 1){
            int x = StdRandom.uniform(100);
            assertTrue(isSameNumber(x, x));
        }*/
        boolean y = isSameNumber(127, 127);
        boolean x = isSameNumber(128, 128);
        assertTrue(x);
    }
}
