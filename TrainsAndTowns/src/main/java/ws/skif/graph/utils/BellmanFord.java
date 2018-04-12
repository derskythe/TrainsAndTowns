package ws.skif.graph.utils;

import ws.skif.graph.models.Edge;
import ws.skif.graph.models.Graph;

import java.util.*;

public class BellmanFord {
    private final Graph graph;

    private int src = -1;                   // Source vertex
    private int totalVertex;                // Total number of vertexes in graph

    private double[] distances;             // Graph distances
    private int[] tree;                     // Min path tree


    public BellmanFord(Graph g) {
        graph = g;
        totalVertex = g.getCount();
        distances = new double[totalVertex];
        tree = new int[totalVertex];
    }

    /**
     * Get min distances
     * If tree is not constructed, run Bellman-Ford algo
     *
     * @param vertex Number of selected vertex
     * @return Array of distances
     */
    public double[] getDistances(int vertex) {
        if (vertex < 0 || vertex >= totalVertex) {
            return null;
        }
        if (vertex != src) {
            bellmanFord(vertex);
        }
        return distances;
    }

    /**
     * Relaxing edge
     *
     * @param from   Vertex number
     * @param fromTo Edge and destination vertex
     * @return TRUE, if relaxing change distance, otherwise FALSE
     */
    private boolean relax(int from, Edge fromTo) {
        int to = fromTo.to();
        double newDist = distances[from] + fromTo.weight();
        if (newDist < distances[to]) {
            distances[to] = newDist;
            tree[to] = from;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Bellman-Ford algo for min path and find cycles with negative distance
     *
     * @param s
     * @return
     */
    private boolean bellmanFord(int s) {
        src = s;
        // Init arrays
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(tree, -1);

        distances[s] = 0;

        // Has changes?
        boolean changed = true;

        // If after (n+1) step there is not changes
        // then graph has cycle with negative distance
        for (int step = 0; step <= totalVertex && changed; step++) {
            changed = false;

            // Loop for all edges from all vertexes
            for (int i = 0; i < totalVertex; i++) {
                if (distances[i] != Double.POSITIVE_INFINITY) {
                    for (Edge edge : graph.getListEdge(i)) {
                        // Relaxing edge
                        if (relax(i, edge)) {
                            changed = true;
                        }
                    }
                }
            }
        }
        return !changed;
    }
}