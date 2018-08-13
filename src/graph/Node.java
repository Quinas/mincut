package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
  private List<Edge> adj;
  private int index;

  public Node(int index) {
    this.index = index;
    adj = new ArrayList<>();
  }

  public void addEdge(Node node, double weight) {
    adj.add(new Edge(this, node, weight));
  }

  public int size() {
    return adj.size();
  }

  public int index() {
    return index;
  }

  public List<Edge> getAdj() {
    return adj;
  }

  public List<Edge> getEdges(Node node) {
    List<Edge> ans = new ArrayList<>();
    for (Edge edge : adj) {
      if (edge.getTo() == node) {
        ans.add(edge);
      }
    }
    return ans;
  }

  public void removeEdge(Node node, double weight) {
    for (Edge edge : adj) {
      if (edge.getTo() == node && edge.getWeight() == weight) {
        adj.remove(edge);
        break;
      }
    }
  }
}
