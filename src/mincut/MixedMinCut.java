package mincut;

import graph.Graph;
import util.Lambda;

public class MixedMinCut implements IMinCut {

  private int k;
  private Karger karger;
  private Deterministic deterministic;
  private Lambda onPartialResult;

  public MixedMinCut(int k, int minNodes, Lambda onPartialResult) {
    this.k = k;
    karger = new Karger(k, minNodes, null);
    deterministic = new Deterministic();
    this.onPartialResult = onPartialResult;
  }

  @Override
  public int minCut(Graph graph) {
    int ans = Integer.MAX_VALUE;

    for (int i = 1; i <= k; ++i) {
      Graph nGraph = karger.algorithm(graph);
      ans = Math.min(ans, deterministic.minCut(nGraph));
      if (onPartialResult != null) {
        onPartialResult.apply(new PartialResult(i, ans));
      }
    }

    return ans;
  }
}
