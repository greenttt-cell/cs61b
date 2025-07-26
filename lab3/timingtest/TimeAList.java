package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    //public AList<Integer> Ns;
   // public AList<Double> times;
    //public AList<Integer> opCounts;
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opCounts = new AList<Integer>();
        timeAListConstruction(Ns, times, opCounts);
        printTimingTable(Ns, times, opCounts);
    }

    public static void timeAListConstruction(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        // TODO: YOUR CODE HERE
        // need to calculate Ns, times, opCounts
        //N = 32000 and 64000
        Ns.addLast(1000);
        Ns.addLast(2000);
        Ns.addLast(4000);
        Ns.addLast(8000);
        Ns.addLast(16000);
        Ns.addLast(32000);
        Ns.addLast(64000);
        //Ns.addLast(100000000);
        for(int i = 0; i < Ns.size(); i += 1){
            opCounts.addLast(Ns.get(i));
            AList<Integer> tmp = new AList<Integer>();
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < Ns.get(i); j += 1){
                tmp.addLast(j);
            }double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
    }
}
