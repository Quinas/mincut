package graph;

public class GraphUtils {
    public static void dfs(Graph graph, int v, int color, int[] vis) {
        vis[v] = color;
        for (int i = 0; i < graph.size(); ++i) {
            if (graph.getAdjMatrix()[v][i] > 0 && vis[i] == 0) {
                dfs(graph, i, color, vis);
            }
        }
    }

    private static boolean isConnected(Graph graph) {
        int n = graph.size();
        int[] vis = new int[n];
        GraphUtils.dfs(graph, 0, 1, vis);
        for (int node=0; node<n; node++) {
            if (vis[node] == 0) { return false; }
        }
        return true;
    }

    public static Graph generate(int nNodes, double prob) {
        Graph graph;
        for (int cTries=0; true; cTries++) {
            System.out.printf("try %d\n", cTries);
            graph = Graph.randomGraph(nNodes, prob);
            System.out.println("...");
            if (isConnected(graph)) { break; }
        }
        System.out.println("generated");
        return graph;
    }
}
