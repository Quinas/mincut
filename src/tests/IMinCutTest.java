package tests;

import graph.Graph;
import mincut.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class IMinCutTest {

  List<IMinCut> mincut;
  List<Graph> graphs;
  List<Integer> answer;

  @Before
  public void setUp() throws Exception {
    mincut = new ArrayList<>();
    graphs = new ArrayList<>();
    answer = new ArrayList<>();

    mincut.add(new Deterministic());
    mincut.add(new Karger(100, null));
    mincut.add(new MixedMinCut(100, 5, null));

    graphs.add(new Graph(2));
    graphs.get(0).addEdge(0, 1);
    answer.add(1);

    graphs.add(new Graph(4));
    graphs.get(1).addEdge(0, 1);
    graphs.get(1).addEdge(0, 2);
    graphs.get(1).addEdge(1, 3);
    graphs.get(1).addEdge(1, 2);
    answer.add(1);
  }

  @Test
  public void minCut() {
    for (IMinCut alg : mincut) {
      for (int i = 0; i < graphs.size(); ++i) {
        assertEquals(answer.get(i).intValue(), alg.minCut(graphs.get(i)));
      }
    }
  }

  @Test
  public void randomMinCut() {
    for (int i = 0; i < 100; ++i) {
      double r = 0.6;
      Graph graph = Graph.randomGraph(20, r);
      int ans = new Deterministic().minCut(graph);
      for (IMinCut alg : mincut) {
        assertEquals(ans, alg.minCut(graph));
      }
    }
  }
}