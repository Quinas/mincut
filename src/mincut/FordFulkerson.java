package mincut;

import graph.Graph;
import graph.GraphUtils;

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
    int[] vis = new int[graph.size()];
    int color = 1;

    for (int i = 0; i < graph.size(); ++i) {
      if (vis[i] == 0) {
        GraphUtils.dfs(graph, i, color, vis);
        color++;
      }
    }

    int ans = Integer.MAX_VALUE;
    for (int i = 0; i < graph.size(); ++i) {
      for (int j = i + 1; j < graph.size(); ++j) {
        if (vis[i] == vis[j]) {
          ans = Math.min(ans, minCut(graph, i, j));
        }
      }
    }
    return ans;
  }
}
