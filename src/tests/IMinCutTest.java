package tests;

import graph.Graph;
import mincut.FordFulkerson;
import mincut.IMinCut;
import mincut.Karger;
import mincut.MixedMinCut;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class IMinCutTest {

  List<IMinCut> mincut;
  List<Graph> graphs;
  List<Double> answer;

  @Before
  public void setUp() throws Exception {
    mincut = new ArrayList<>();
    graphs = new ArrayList<>();
    answer = new ArrayList<>();

    mincut.add(new FordFulkerson());
    mincut.add(new Karger(100));
    mincut.add(new MixedMinCut(100, 5));

    graphs.add(new Graph(2));
    graphs.get(0).addEdge(0, 1);
    answer.add(1.0);

    graphs.add(new Graph(4));
    graphs.get(1).addEdge(0, 1);
    graphs.get(1).addEdge(0, 2);
    graphs.get(1).addEdge(1, 3);
    graphs.get(1).addEdge(1, 2);
    answer.add(1.0);
  }

  @Test
  public void minCut() {
    for (IMinCut alg : mincut) {
      for (int i = 0; i < graphs.size(); ++i) {
        assertEquals(answer.get(i), alg.minCut(graphs.get(i)));
      }
    }
  }
}