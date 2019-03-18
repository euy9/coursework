package project4;

import java.util.NoSuchElementException;

public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private int V;                      // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<Edge>[] adj;            // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v
    
    // Initializes a graph with V vertices and 0 edges.
    @SuppressWarnings("rawtypes")
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Edge>();
    }

    // Initializes a random graph with V vertices and E edges.
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            int capacity = 1 * StdRandom.uniform(100);
            Edge e = new Edge(v, w, capacity);
            addEdge(e);
        }
    }

    // Initializes a graph from the specified input stream.
    public Graph(In in) {
        this(in.readInt());     // init graph w/ V vertices
        while (in.hasNextChar()) {
            int v = 0;
            try {
                v = in.readInt();
            } catch (NoSuchElementException e){
                return;
            }
            int w = in.readInt();
            String type = in.readString();
            int capacity = in.readInt();
            int length = in.readInt();
            validateVertex(v);
            validateVertex(w);
            addEdge(new Edge(v, w, type, capacity, length));
        }
    }

    // Initializes a new graph that is a deep copy of G.
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++)
            this.indegree[v] = G.indegree(v);
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adj[v].add(e);
            }
        }
    }

    // returns number of vertices
    public int V() {
        return V;
    }

    // returns number of edges
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // adds edge e to this graph
    public void addEdge(Edge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }
    
    public void removeVertice (int v) {
        for (Edge e : adj[v]){
            indegree[e.to()]--;
        }
        E -= outdegree(v);
        adj[v] = null;
        V--;
    }


    // returns the edges incident from vertex v.
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    // returns the number of edges incident from vertex v.
    public int outdegree(int v) {
        if (v >= V)
            return 0;
        validateVertex(v);
        return adj[v].size();
    }

    // returns the number of edges incident to vertex v.
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    // returns list of all edges.
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            if (adj(v) == null){
                list = null;
                return list;
            }
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    // returns a string representation of this graph.
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    // reset flow of all edges of the graph to 0
    public void resetFlow() {
        Iterable<Edge> list = edges();
        for (Edge e: list){
            e.resetFlow();
        }
    }
}