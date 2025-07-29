package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void TestArrayDeque() {
        StudentArrayDeque<Integer> buggy = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correct = new ArrayDequeSolution<>();
        String failure = "\n";
        int randomtimes = 1000;
        for (int i = 0; i < randomtimes; i += 1) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {
                int x = StdRandom.uniform(10);
                buggy.addFirst(x);
                correct.addFirst(x);
                failure += String.format("addFirst(%d)\n", x);
                assertEquals(failure, correct.get(0), buggy.get(0));
            } else if (operation == 1) {
                int x = StdRandom.uniform(10);
                buggy.addLast(x);
                correct.addLast(x);
                failure += String.format("addLast(%d)\n", x);
                assertEquals(failure, correct.get(correct.size() - 1), buggy.get(buggy.size() - 1));
            } else if (operation == 2) {
                if (buggy.isEmpty()) {
                    continue;
                }
                Integer x = buggy.removeFirst();
                Integer y = correct.removeFirst();
                failure += "removeFirst()\n";
                assertEquals(failure, y, x);
            } else {
                if (buggy.isEmpty()) {
                    continue;
                }
                Integer x = buggy.removeLast();
                Integer y = correct.removeLast();
                failure += "removeLast()\n";
                assertEquals(failure, y, x);
            }
        }
    }
}
