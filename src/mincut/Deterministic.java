package mincut;

import graph.Graph;

public class Deterministic implements IMinCut {

    @Override
    public int minCut(Graph graph) {
        return new Dinic().minCut(graph);
    }
}
