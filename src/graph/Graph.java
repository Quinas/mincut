package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
  private List<Node> nodes;

  public Graph(int n) {
    nodes = new ArrayList<>();
    for (int i = 0; i < n; ++i) {
      nodes.add(new Node(i));
    }
  }

  public Graph(Graph graph) {
    this(graph.size());
    for (Node node : graph.nodes) {
      for (Edge edge : node.getAdj()) {
        addEdge(edge.getFrom().index(), edge.getTo().index(), edge.getWeight());
      }
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

  public void addEdge(int u, int v, double weight) {
    nodes.get(u).addEdge(nodes.get(v), weight);
  }

  public void addEdge(int u, int v) {
    addEdge(u, v, 1);
    addEdge(v, u, 1);
  }

  public Node get(int v) {
    return nodes.get(v);
  }

  public int size() {
    return nodes.size();
  }

  public List<Node> getNodes() {
    return nodes;
  }
}
