package graph;

import java.util.Random;

public class Graph {
  private int[][] adjMatrix;

  public Graph(int n) {
    adjMatrix = new int[n][n];
  }

  public Graph(Graph graph) {
    this(graph.adjMatrix.length);
    int n = graph.adjMatrix.length;
    for (int i = 0; i < n; ++i) {
      System.arraycopy(graph.adjMatrix[i], 0, adjMatrix[i], 0, n);
    }
  }

  public static Graph randomGraph(int nodes, double p) {
    Graph graph = new Graph(nodes);
    Random r = new Random();
    for (int i = 0; i < nodes; ++i) {
      for (int j = i + 1; j < nodes; ++j) {
        double d = r.nextDouble();
        if (d <= p) {
          graph.addEdge(i, j);
        }
      }
    }
    return graph;
  }

  private void addEdge(int u, int v, int weight) {
    adjMatrix[u][v] += weight;
  }

  public void addEdge(int u, int v) {
    addEdge(u, v, 1);
    addEdge(v, u, 1);
  }

  public int size() {
    return adjMatrix.length;
  }

  public int[][] getAdjMatrix() {
    return adjMatrix;
  }
}
