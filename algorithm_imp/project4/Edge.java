package project4;

public class Edge { 
    private final int v;                // from
    private final int w;                // to
    private String type;                // optical or copper
    private final int capacity;         // capacity (weight)
    private int length;                 // length
    private int flow;                   // flow
    private double latency;             // latency
    
    double COPPER_SPEED = 230000000.0;
    double OPTICAL_SPEED = 200000000.0;

    // constructor
    public Edge (int v, int w, int capacity) {
        if (v < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (Double.isNaN(capacity)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.type = "";
        this.capacity = capacity;
        this.length = 0;
        this.flow = 0;
        this.latency = 0.0;
    }


    // constructor
    public Edge(int v, int w, String type, int capacity, int length) {
        if (v < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        this.v = v;
        this.w = w;
        this.type = type;
        this.capacity = capacity;
        this.length = length;
        this.flow = 0;
        
        if (type().equals("copper"))
            this.latency = length / COPPER_SPEED;
        else
            this.latency = length/ OPTICAL_SPEED;
    }
    
    // initialize an edge from another edge
    public Edge (Edge e){
        this.v = e.v;
        this.w = e.w;
        this.type = e.type;
        this.capacity = e.capacity;
        this.length = e.length;
        this.flow = e.flow;
        this.latency = e.latency;
    }

    // returns the tail vertex of the edge.
    public int from() {
        return v;
    }

    // returns the head vertex of the edge.
    public int to() {
        return w;
    }
    
    // returns the type of the edge
    public String type() {
        return type;
    }

    // returns the capacity of the edge.
    public int capacity() {
        return capacity;
    }
    
    // returns the length of the edge
    public int length() {
        return length;
    }
    
    public double latency() {
        return latency;
    }
    
    // returns the flow of the edge.
    public int flow() {
        return flow;
    }
    
    // resets the flow to 0
    public void resetFlow() {
        flow = 0;
    }
    
    // returns the other endpoint
    public int other (int vertex) {
        if (vertex == v)
            return w;
        else if (vertex == w)
            return v;
        else throw new IllegalArgumentException("invalid endpoint");
    }
    
    // returns residual capacity of the edge in the direction to the given vertex
    public int residualCapacityTo (int vertex) {
        if (vertex == v)
            return flow;                // backward edge
        else if (vertex == w) 
            return capacity - flow;    // forward edge
        else throw new IllegalArgumentException ("invalid endpoint");
    }
    
    // increase the flow in the direction to the given vertex
    // delta = amount to increase flow
     public void addResidualFlowTo(int vertex, int delta) {
        if (!(delta >= 0)) 
            throw new IllegalArgumentException("Delta must be nonnegative");

        if      (vertex == v) flow -= delta;           // backward edge
        else if (vertex == w) flow += delta;           // forward edge
        else throw new IllegalArgumentException("invalid endpoint");

        if (!(flow >= 0))     
            throw new IllegalArgumentException("Flow is negative");
        if (!(flow <= capacity)) 
            throw new IllegalArgumentException("Flow exceeds capacity");
    }
     
    // returns either endpoint of this edge
    public int either() {
        return v;
    }
    
     
}