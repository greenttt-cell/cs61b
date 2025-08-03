package gh2;

import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    private static final int CAPACITY = 37;
    public static void main(String[] args) {
        GuitarString[] keyboardArray = (GuitarString[]) new Object[CAPACITY];
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        for (int i = 0; i < CAPACITY; i += 1) {
            GuitarString key = new GuitarString(440 * Math.pow(2, ((float) (i - 24) / 12)));
            keyboardArray[i] = key;
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int x = keyboard.indexOf(key);
                keyboardArray[x].pluck();
            }
        }
    }
}
