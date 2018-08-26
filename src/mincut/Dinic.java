package mincut;

import graph.Graph;

import java.util.List;

import static java.lang.Math.min;

public class Dinic implements IMinCut {
    @Override
    public int minCut(Graph graph) {
        int nNodes = graph.size();
        List<MaxFlowDinic.Edge>[] g = MaxFlowDinic.createGraph(nNodes);
        for (int u=0; u<nNodes; u++) {
            for (int v=u+1; v<nNodes; v++) {
                int cap = graph.getAdjMatrix()[u][v];
                if (cap > 0) {
                    MaxFlowDinic.addEdge(g, u, v, cap);
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        int s = 0;
        for (int t=1; t<nNodes; t++) {
            int cand = MaxFlowDinic.maxFlow(g, s, t);
            ans = min(ans, cand);
        }
        return ans;
    }
}
