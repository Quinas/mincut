package mincut;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Random;

public class Karger implements IMinCut {

  private int k;
  private int minNodes;

  public Karger(int k, int minNodes) {
    this.k = k;
    this.minNodes = minNodes;
  }

  public Karger(int k) {
    this(k, 2);
  }

  @Override
  public double minCut(Graph graph) {
    double ans = Double.MAX_VALUE;
    for (int i = 0; i < k; ++i) {
      Graph nGraph = algorithm(graph);

      double cur = 0.0;

      for (Node node : nGraph.getNodes()) {
        for (Edge edge : node.getAdj()) {
          cur += edge.getWeight();
        }
      }

      ans = Math.min(ans, cur);
    }
    return ans / 2;
  }

  private void merge(Graph graph, Edge edge) {
    Node u = edge.getFrom();
    Node v = edge.getTo();

    u.getAdj().removeAll(u.getEdges(v));

    for (Edge e : v.getAdj()) {
      if (e.getTo() == u) {
        continue;
      }
      graph.addEdge(u.index(), e.getTo().index());
      e.getTo().removeEdge(v, e.getWeight());
    }

    v.getAdj().clear();
  }

  public Graph algorithm(Graph graph) {
    Graph nGraph = new Graph(graph);

    for (int i = 0; i < nGraph.size() - minNodes; ++i) {
      Edge edge = randomEdge(nGraph);
      merge(nGraph, edge);
    }

    return nGraph;
  }

  private Edge randomEdge(Graph graph) {
    int total = 0;

    for (Node node : graph.getNodes()) {
      total += node.getAdj().size();
    }

    int r = new Random().nextInt(total);

    for (Node node : graph.getNodes()) {
      if (r < node.getAdj().size()) {
        return node.getAdj().get(r);
      }
      r -= node.getAdj().size();
    }
    return null;
  }
}
