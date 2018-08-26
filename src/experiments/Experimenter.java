package experiments;

import graph.Graph;
import graph.GraphUtils;
import mincut.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Experimenter {
    private static int nProbs = 5;
    private static int nMeasures = 13;
    private static int maxK = 7;
    private static int iStart = 5;
    private static int iEnd = 10;

    private static JSONObject doMeasures(IMinCut impl, Graph graph, int solution) {
        List<Double> timeMeasures = new ArrayList<>();
        List<Double> probErrorMeasures = new ArrayList<>();
        for (int i=0; i<nMeasures; i++) {
            System.out.printf("measure %d/%d\n", i+1, nMeasures);
            System.gc();
            Timer timer = new Timer();
            int answer = impl.minCut(graph);
            double time = timer.elapsed();
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

        JSONArray maxFlowResults = new JSONArray();
        JSONArray kargerResults = new JSONArray();
        JSONArray mixedResults = new JSONArray();

        for (int i=iStart; i<=iEnd; i++) {
            int n = 1 << i;
            System.out.printf("i: %d, n: %d\n", i, n);

            List<Double> probs = new ArrayList<>();
            for (int r=0; r<nProbs; r++) {
                probs.add(RandomUtils.randContinuous(random, 1 / Math.sqrt(n), 1));
            }

            List<Integer> tees = generateTees(random, n);

            for (double p : probs) {
                System.out.printf("p: %f\n", p);

                Graph graph = GraphUtils.generate(n, p);

                IMinCut maxFlow = new Dinic();
                int solution = maxFlow.minCut(graph);
                JSONObject measures = doMeasures(maxFlow, graph, solution);
                measures.put("n", n);
                measures.put("p", p);
                maxFlowResults.add(measures);

                Double timeMeasures[][] = new Double[maxK + 1][nMeasures];
                Double errMeasures[][] = new Double[maxK + 1][nMeasures];
                for (int m=0; m<nMeasures; m++) {
                    System.out.printf("measure %d/%d\n", m+1, nMeasures);

                    int finalM = m;
                    Timer timer = new Timer();
                    IMinCut karger = new Karger(maxK, o -> onPartialResult(solution, timeMeasures, errMeasures, finalM,
                            timer, (PartialResult) o));
                    System.gc();
                    karger.minCut(graph);
                }
                for (int k=1; k<=maxK; k++) {
                    System.out.printf("k: %d\n", k);

                    Measure time = new Measure(Arrays.asList(timeMeasures[k]));
                    Measure probError = new Measure(Arrays.asList(errMeasures[k]));

                    measures = makeMeasure(time, probError, n, p, k);
                    kargerResults.add(measures);
                }

                for (int t : tees) {
                    System.out.printf("t: %d\n", t);

                    for (int m=0; m<nMeasures; m++) {
                        System.out.printf("measure %d/%d\n", m+1, nMeasures);

                        int finalM = m;
                        Timer timer = new Timer();
                        IMinCut mixed = new MixedMinCut(maxK, t, o -> onPartialResult(solution, timeMeasures,
                                errMeasures, finalM, timer, (PartialResult) o));
                        System.gc();
                        mixed.minCut(graph);
                    }
                    for (int k=1; k<=maxK; k++) {
                        System.out.printf("k: %d\n", k);

                        Measure time = new Measure(Arrays.asList(timeMeasures[k]));
                        Measure probError = new Measure(Arrays.asList(errMeasures[k]));

                        measures = makeMeasure(time, probError, n, p, k);
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

    private static JSONObject makeMeasure(Measure time, Measure probError, int n, double p, int k) {
        JSONObject measures;
        measures = new JSONObject();
        measures.put("time", time.toJson());
        measures.put("probError", probError.toJson());
        measures.put("n", n);
        measures.put("p", p);
        measures.put("k", k);
        return measures;
    }

    private static Object onPartialResult(int solution, Double[][] timeMeasures, Double[][] errMeasures,
                                          int finalM, Timer timer, PartialResult o) {
        int k = o.k;
        int cut = o.cut;

        timeMeasures[k][finalM] = timer.elapsed();
        errMeasures[k][finalM] = cut == solution ? 0. : 1.;

        return null;
    }
}
