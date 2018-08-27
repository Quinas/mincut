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

        boolean[] hasNeighbors = new boolean[nNodes];
        for (int u=0; u<nNodes; u++) {
            for (int v=u+1; v<nNodes; v++) {
                if (graph.getAdjMatrix()[u][v] > 0) {
                    hasNeighbors[u] = true;
                    break;
                }
            }
        }
        int s = -1;
        for (int u=0; u<nNodes; u++) {
            if (hasNeighbors[u]) {
                s = u;
                break;
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int t=0; t<nNodes; t++) {
            if (t == s || !hasNeighbors[t]) { continue; }

            int cand = MaxFlowDinic.maxFlow(g, s, t);
            ans = min(ans, cand);
        }
        return ans;
    }
}
