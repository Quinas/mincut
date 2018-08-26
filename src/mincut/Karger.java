package mincut;

import graph.Edge;
import graph.Graph;
import util.Lambda;

import java.util.Random;

public class Karger implements IMinCut {

  private int k;
  private int minNodes;
  private Lambda onPartialResult;

  public Karger(int k, int minNodes, Lambda onPartialResult) {
    this.k = k;
    this.minNodes = minNodes;
    this.onPartialResult = onPartialResult;
  }

  public Karger(int k, Lambda onPartialResult) {
    this(k, 2, onPartialResult);
  }

  @Override
  public int minCut(Graph graph) {
    int ans = Integer.MAX_VALUE;
    for (int i = 1; i <= k; ++i) {
      Graph nGraph = algorithm(graph);

      int cur = 0;

      for (int u = 0; u < graph.size(); ++u) {
        for (int v = u + 1; v < graph.size(); ++v) {
          cur += nGraph.getAdjMatrix()[u][v];
        }
      }

      ans = Math.min(ans, cur);
      if (onPartialResult != null) {
        onPartialResult.apply(new PartialResult(i, ans));
      }
    }
    return ans;
  }

  private void merge(Graph graph, Edge edge, int[] numEdgesRow) {
    int u = edge.getU();
    int v = edge.getV();
    int merged = graph.getAdjMatrix()[u][v];

    numEdgesRow[u] -= merged;
    numEdgesRow[v] -= merged;
    graph.getAdjMatrix()[u][v] = 0;
    graph.getAdjMatrix()[v][u] = 0;
    for (int i = 0; i < graph.size(); ++i) {
      graph.getAdjMatrix()[u][i] += graph.getAdjMatrix()[v][i];
      numEdgesRow[u] += graph.getAdjMatrix()[v][i];
      numEdgesRow[v] -= graph.getAdjMatrix()[v][i];
      graph.getAdjMatrix()[i][u] += graph.getAdjMatrix()[i][v];
      
      graph.getAdjMatrix()[v][i] = 0;
      graph.getAdjMatrix()[i][v] = 0;
    }
  }

  public Graph algorithm(Graph graph) {
    Graph nGraph = new Graph(graph);
    int n = graph.size();
    int[] numEdgesRow = new int[n];

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        numEdgesRow[i] += graph.getAdjMatrix()[i][j];
      }
    }

    for (int i = 0; i < n - minNodes; ++i) {
      Edge edge = randomEdge(nGraph, numEdgesRow);
      merge(nGraph, edge, numEdgesRow);
    }

    return nGraph;
  }

  private Edge randomEdge(Graph graph, int[] numEdgesRow) {
    int total = 0;
    int n = graph.size();

    for (int i = 0; i < n; ++i) {
      total += numEdgesRow[i];
    }

    int r = new Random().nextInt(total);

    for (int i = 0; i < n; ++i) {
      if (r < numEdgesRow[i]) {
        for (int j = 0; j < n; ++j) {
          if (r < graph.getAdjMatrix()[i][j]) {
            return new Edge(i, j);
          }
          r -= graph.getAdjMatrix()[i][j];
        }
      }
      r -= numEdgesRow[i];
    }
    return null;
  }
}
