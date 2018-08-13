package mincut;

import graph.Graph;

public class MixedMinCut implements IMinCut {

  private int k;
  private Karger karger;
  private FordFulkerson fordFulkerson;

  public MixedMinCut(int k, int minNodes) {
    this.k = k;
    karger = new Karger(k, minNodes);
    fordFulkerson = new FordFulkerson();
  }

  @Override
  public double minCut(Graph graph) {
    double ans = Double.MAX_VALUE;

    for (int i = 0; i < k; ++i) {
      Graph nGraph = karger.algorithm(graph);
      ans = Math.min(ans, fordFulkerson.minCut(nGraph));
    }

    return ans;
  }
}
