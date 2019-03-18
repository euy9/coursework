package project4;

import java.io.*;
import java.util.Scanner;

public class NetworkAnalysis {

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        // get info from file
        
        while(true){
            System.out.println("\nChoose an option: ");
            System.out.println("\t1. Find the lowest latency path");
            System.out.println("\t2. Determine whether the graph is copper-only connected");
            System.out.println("\t3. Find the maximum amount of data");
            System.out.println("\t4. Find the lowest latency spanning tree");
            System.out.println("\t5. Determine whether the graph would remain connected "
                                + "if any two vertices in the graph were to fail");
            System.out.println("\t6. Quit");

            Scanner s = new Scanner(System.in);
            int option = s.nextInt();

            switch(option){
                case 1:     lowestLatencyPath(G);        break;
                case 2:     copperOnly(G);               break;
                case 3:     maxData(G);                  break;
                case 4:     lowestAvgLatencyST(G);       break;
                case 5:     connected(G);                break;
                case 6:     System.exit(0);
            }
        }
    }
    
    public static void lowestLatencyPath(Graph G){
        System.out.println("\n------------ 1. Lowest latency path ------------");
        Scanner s = new Scanner(System.in);
        System.out.println("Choose the two vertices you wish to find the lowest latency path between");
        System.out.print("\tEnter the first vertex: ");
        int v = s.nextInt();
        System.out.print("\tEnter the second vertex: ");
        int w = s.nextInt();
        
        
        // output the edges that comprise this lowest latency path from v to w
        DijkstraAllPairsSP D = new DijkstraAllPairsSP(G);
        if (D.hasPath(v, w)){
            Iterable<Edge> list = D.path(v, w);
            for(Edge e: list){
                System.out.format("%d  %d  %s  %d  %d", e.from(), e.to(), e.type(), e.capacity(), e.length());
                System.out.println();
            }
        } else 
            System.out.println("There exists no path from vertex " + v + " to vertex " + w);
       
   }
   
    public static void copperOnly(Graph G) {
        System.out.println("\n------------ 2. Copper-only connected? ------------");

        // whether all edges are copper
        Iterable<Edge> list = G.edges();
        for (Edge e: list){
            if (!e.type().equals("copper")){
                System.out.println("The graph is NOT copper-only connected");
                return;
            }
        }
        System.out.println("The graph is copper-only connected");
    }

     public static void maxData(Graph G) {
        System.out.println("\n------------ 3. Maximum amount of data ------------");
        Scanner s = new Scanner(System.in);
        System.out.println("Choose the two vertices you wish to find the maximum bandwidth");
        System.out.print("\tEnter the first vertex: ");
        int v = s.nextInt();
        System.out.print("\tEnter the second vertex: ");
        int w = s.nextInt();

        // output the value of the max bandwidth transmitted from v to w
        G.resetFlow();
        FordFulkerson maxflow = new FordFulkerson(G, v, w);
        if (maxflow.value() == 0){
            System.out.println("There exists no path from vertex " + v + " to vertex " + w);
            return;
        }
        System.out.println(maxflow.value() + " gigabits per second");
        
    }

    public static void lowestAvgLatencyST(Graph G) {
        System.out.println("\n------------ 4. Lowest average latency spanning tree  ------------");
        /* TODO: output the spanning tree w/ the lowest average latency per edge */
        KruskalMST D = new KruskalMST(G);
        Iterable<Edge> list = D.edges();
        for(Edge e: list){
            System.out.format("%d  %d  %s  %d  %d", e.from(), e.to(), e.type(), e.capacity(), e.length());
            System.out.println();
        }
        
    }

    public static void connected(Graph G) {
        System.out.println("\n------------ 5. remain connected? ------------");
        /* TODO: det. whether removal of any two vertices would cause the 
                 graph to become disconnected */
        boolean connected = true;
        for (int i = 0; i < G.V(); i++){
            for (int j = 1; j< G.V(); j++){
                Graph copyG = new Graph(G);
                if (i == j)    
                    continue;
                copyG.removeVertice(i);
                copyG.removeVertice(j);
                Iterable<Edge> list = copyG.edges();
                if (list == null)
                    continue;
                for (Edge e : list){
                    for (int k = 0; k < copyG.V(); k++){
                        DFS dfs = new DFS(copyG, 0);
                        for (int h = 1; h < copyG.V(); h++){
                            if (dfs.hasPathTo(h))
                                connected = false;
                        }
                    }
                }
                
            }
        }
        
        if (connected)
            System.out.print("The graph will remain connected ");
        else    
            System.out.print("The graph will NOT remain connected ");
        
        System.out.println("if any two vertices in the graph were to fail.");
    }

 }
