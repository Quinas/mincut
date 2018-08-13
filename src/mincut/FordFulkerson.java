package mincut;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.LinkedList;
import java.util.Queue;

public class FordFulkerson implements IMinCut {

  private static boolean bfs(Graph graph, int s, int t, int[] parent) {
    boolean[] visited = new boolean[graph.size()];
    Queue<Integer> queue = new LinkedList<>();
    queue.add(s);
    visited[s] = true;
    parent[s] = -1;

    while (!queue.isEmpty()) {
      int u = queue.remove();
      Node node = graph.get(u);
      for (Edge edge : node.getAdj()) {
        int v = edge.getTo().index();
        if (edge.getWeight() > 0 && !visited[v]) {
          visited[v] = true;
          parent[v] = u;
          queue.add(v);
        }
      }
    }

    return visited[t];
  }

  private static double minCut(Graph graph, int s, int t) {
    int u, v;

    Graph residual = new Graph(graph);

    int[] parent = new int[residual.size()];

    double maxFlow = 0.0;

    while (bfs(residual, s, t, parent)) {

      double pathFlow = Double.MAX_VALUE;
      for (v = t; v != s; v = parent[v]) {
        u = parent[v];
        pathFlow = Math.min(pathFlow, residual.get(u).getEdges(residual.get(v)).get(0).getWeight());
      }

      for (v = t; v != s; v = parent[v]) {
        u = parent[v];
        Edge e1 = residual.get(u).getEdges(residual.get(v)).get(0);
        Edge e2 = residual.get(v).getEdges(residual.get(u)).get(0);

        e1.setWeight(e1.getWeight() - pathFlow);
        e2.setWeight(e2.getWeight() + pathFlow);
      }
      maxFlow += pathFlow;
    }

    return maxFlow;
  }

  @Override
  public double minCut(Graph graph) {
    double ans = Double.MAX_VALUE;
    for (int i = 0; i < graph.size(); ++i) {
      for (int j = i + 1; j < graph.size(); ++j) {
        ans = Math.min(ans, minCut(graph, i, j));
      }
    }
    return ans;
  }
}
