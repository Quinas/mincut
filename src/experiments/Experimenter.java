package experiments;

import graph.Graph;
import graph.GraphUtils;
import mincut.FordFulkerson;
import mincut.IMinCut;
import mincut.Karger;
import mincut.MixedMinCut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Experimenter {
    private static int nProbs = 5;
    private static int nMeasures = 13;
    private static int maxK = 7;
    private static int iStart = 3;
    private static int iEnd = 5;

    private static JSONObject doMeasures(IMinCut impl, Graph graph, int solution) {
        List<Double> timeMeasures = new ArrayList<>();
        List<Double> probErrorMeasures = new ArrayList<>();
        for (int i=0; i<nMeasures; i++) {
            System.out.printf("measure %d/%d\n", i+1, nMeasures);
            System.gc();
            Timer timer = new Timer();
            int answer = impl.minCut(graph);
            double time = timer.ellapsed();
            double probError = answer == solution ? 0 : 1;

            timeMeasures.add(time);
            probErrorMeasures.add(probError);
        }
        Measure time = new Measure(timeMeasures);
        Measure probError = new Measure(probErrorMeasures);

        JSONObject measures = new JSONObject();
        measures.put("time", time.toJson());
        measures.put("probError", probError.toJson());
        return measures;
    }

    private static List<Integer> generateTees(Random random, int n) {
        int a = 3, b = n-1;
        List<Integer> tees = RandomUtils.chooseK(random, b-a+1, 5);
        for (int i=0; i<tees.size(); i++) {
            tees.set(i, tees.get(i) + a);
        }
        return tees;
    }

    public static void main(String[] args) {
        Random random = new Random();

        List<Double> probs = new ArrayList<>();
        for (int i=0; i<nProbs; i++) {
            probs.add(RandomUtils.randContinuous(random, 0.5, 1));
        }

        JSONArray maxFlowResults = new JSONArray();
        JSONArray kargerResults = new JSONArray();
        JSONArray mixedResults = new JSONArray();

        for (int i=iStart; i<=iEnd; i++) {
            int n = 1 << i;
            System.out.printf("i: %d, n: %d\n", i, n);

            List<Integer> tees = generateTees(random, n);

            for (double p : probs) {
                System.out.printf("p: %f\n", p);

                Graph graph = GraphUtils.generate(n, p);

                FordFulkerson maxFlow = new FordFulkerson();
                int solution = maxFlow.minCut(graph);
                JSONObject measures = doMeasures(maxFlow, graph, solution);
                measures.put("n", n);
                measures.put("p", p);
                maxFlowResults.add(measures);

                for (int k=1; k<=maxK; k++) {
                    System.out.printf("k: %d\n", k);

                    Karger karger = new Karger(k);
                    measures = doMeasures(karger, graph, solution);
                    measures.put("n", n);
                    measures.put("p", p);
                    measures.put("k", k);
                    kargerResults.add(measures);

                    for (int t : tees) {
                        System.out.printf("t: %d\n", t);

                        MixedMinCut mixed = new MixedMinCut(k, t);
                        measures = doMeasures(mixed, graph, solution);
                        measures.put("n", n);
                        measures.put("p", p);
                        measures.put("k", k);
                        measures.put("t", t);
                        mixedResults.add(measures);
                    }
                }
            }
        }

        JSONArray allResults = new JSONArray();
        allResults.add(maxFlowResults);
        allResults.add(kargerResults);
        allResults.add(mixedResults);

        System.out.println(allResults);
        String saveTo = "allResults.json";
        try {
            PrintWriter out = new PrintWriter(saveTo, "UTF-8");
            out.println(allResults);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
