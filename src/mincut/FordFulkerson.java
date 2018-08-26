package mincut;

import graph.Graph;

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
      for (int v = 0; v < graph.size(); ++v) {
        if (graph.getAdjMatrix()[u][v] > 0 && !visited[v]) {
          visited[v] = true;
          parent[v] = u;
          queue.add(v);
        }
      }
    }

    return visited[t];
  }

  private static int minCut(Graph graph, int s, int t) {
    int u, v;

    Graph residual = new Graph(graph);

    int[] parent = new int[residual.size()];

    int maxFlow = 0;

    while (bfs(residual, s, t, parent)) {

      int pathFlow = Integer.MAX_VALUE;
      for (v = t; v != s; v = parent[v]) {
        u = parent[v];
        pathFlow = Math.min(pathFlow, residual.getAdjMatrix()[u][v]);
      }

      for (v = t; v != s; v = parent[v]) {
        u = parent[v];
        residual.getAdjMatrix()[u][v] -= pathFlow;
        residual.getAdjMatrix()[v][u] += pathFlow;
      }
      maxFlow += pathFlow;
    }

    return maxFlow;
  }

  @Override
  public int minCut(Graph graph) {
    int s = 0;
    boolean[] vis = new boolean[graph.size()];
    for (int i = 0; i < graph.size(); ++i) {
      for (int j = 0; j < graph.size(); ++j) {
        if (graph.getAdjMatrix()[i][j] > 0) {
          vis[i] = true;
          vis[j] = true;
          s = i;
        }
      }
    }

    System.out.println("yey");

    int ans = Integer.MAX_VALUE;
    for (int i = 0; i < graph.size(); ++i) {
      System.out.printf("s: %d, t: %d\n", s, i);
      if (i != s && vis[i]) {
        ans = Math.min(ans, minCut(graph, s, i));
      }
    }
    return ans;
  }
}
