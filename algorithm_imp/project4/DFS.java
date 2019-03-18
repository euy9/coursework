package project4;

public class DFS {
    private boolean[] marked;  // marked[v] = true if v is reachable from s
    private int[] edgeTo;      // edgeTo[v] = last edge on path from s to v
    private final int s;       // source vertex

    public DFS(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Graph G, int v) { 
        marked[v] = true;
        for (Edge w : G.adj(v)) {
            if (w.to() >= G.V())
                return;
            if (!marked[w.to()]) {
                edgeTo[w.to()] = v;
                dfs(G, w.to());
            }
        }
    }

    // Is there a directed path from the source vertex s to vertex v
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    
    // Returns a directed path from the source vertex s to vertex v
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }
}