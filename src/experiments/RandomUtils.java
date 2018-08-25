package experiments;

import java.util.*;

public class RandomUtils {
    public static int randExclusive(Random random, int start, int stop) {
        return start + random.nextInt(stop - start);
    }

    public static double randContinuous(Random random, double a, double b) {
        return a + (b - a) * random.nextDouble();
    }

    public static List<Integer> chooseK(Random random, int n, int k) {
        List<Integer> chosen = new ArrayList<>();
        Map<Integer, Integer> cands = new HashMap<>();
        int nCands = n;
        for (int i=0; i<k; i++) {
            int iCand = randExclusive(random, 0, nCands);
            int cand = cands.getOrDefault(iCand, iCand);
            int lastCand = cands.getOrDefault(nCands-1, nCands-1);
            cands.put(iCand, lastCand);
            nCands--;
            chosen.add(cand);
        }
        return chosen;
    }
}
