package mincut;

import graph.Graph;

public class MixedMinCut implements IMinCut {

  private int k;
  private Karger karger;
  private Deterministic deterministic;

  public MixedMinCut(int k, int minNodes) {
    this.k = k;
    karger = new Karger(k, minNodes);
    deterministic = new Deterministic();
  }

  @Override
  public int minCut(Graph graph) {
    int ans = Integer.MAX_VALUE;

    for (int i = 0; i < k; ++i) {
      Graph nGraph = karger.algorithm(graph);
      ans = Math.min(ans, deterministic.minCut(nGraph));
    }

    return ans;
  }
}
