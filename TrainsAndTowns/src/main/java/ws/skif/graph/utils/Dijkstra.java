package ws.skif.graph.utils;

import ws.skif.graph.models.Edge;
import ws.skif.graph.models.Graph;

import java.util.Arrays;

public class Dijkstra {

    private final Graph graph;                  // Graph for which calculations are performed

    private int sourceVertex = -1;              // Source vertex
    private int totalVertex;                    // Total number of vertexes in graph

    private double[] distances;                 // Array of distances
    private int[] tree;                         // Bypass tree by minimal paths

    private int[] positions;                    // Vertex indexes in heap
    private Pair[] binHeap;                     // Binary heap
    private int heapSize;                       // Size of heap
    private boolean[] passed;                   // Array of passed vertexes

    public Dijkstra(Graph g) {
        graph = g;
        totalVertex = g.getCount();
    }

    /**
     * Returns minimal path of tree
     * If tree is not constructed, run Dijkstra algo
     *
     * @param seekVertex Original vertex number
     * @return Tree in array of edges
     */
    public int[] getTree(int seekVertex) {
        if (seekVertex < 0 || seekVertex >= totalVertex) {
            return null;
        }
        if (seekVertex != sourceVertex) {
            dijkstra(sourceVertex = seekVertex);
        }
        return tree;
    }

    /**
     * Get minimal distance
     * If tree is not constructed, run Dijkstra algo
     *
     * @param seekVertex Vertex to seek
     * @return Array of distances to this vertex
     */
    public double[] getDistances(int seekVertex) {
        if (seekVertex < 0 || seekVertex >= totalVertex) {
            return null;
        }
        if (seekVertex != sourceVertex) {
            dijkstra(seekVertex);
        }
        return distances;
    }

    /**
     * Dijkstra algo
     *
     * @param sourceVertex 0 vertex
     */
    public void dijkstra(int sourceVertex) {
        this.sourceVertex = sourceVertex;
        this.distances = new double[totalVertex];
        this.tree = new int[totalVertex];
        this.positions = new int[totalVertex];
        this.binHeap = new Pair[totalVertex];
        this.passed = new boolean[totalVertex];

        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(tree, -1);
        Arrays.fill(positions, -1);

        heapSize = 0;
        distances[sourceVertex] = 0;

        // Init heap
        addToHeap(new Pair(sourceVertex, 0));

        while (!emptyHeap()) {
            // Greedy algorithm take nearest vertex
            Pair minPair = extractHeap();
            int currentVertex = minPair.vertex;
            passed[currentVertex] = true;

            // Relaxing edges
            for (Edge edge : graph.getListEdge(currentVertex)) {
                int end = edge.to();
                if (!passed[end]) {
                    double newDist = distances[currentVertex] + edge.weight();
                    if (positions[end] == -1) {
                        // New vertex - add to heap
                        addToHeap(new Pair(end, newDist));
                        tree[end] = currentVertex;
                        distances[end] = newDist;
                    } else {
                        // Heap already in heap. Relaxing edges
                        Pair p = getFromHeap(positions[end]);
                        if (newDist < p.distance) {
                            changeHeap(positions[end], newDist);
                            tree[end] = currentVertex;
                            distances[end] = newDist;
                        }
                    }
                }
            }
        }
    }

    /**
     * Change position in heap
     *
     * @param i       Position in heap
     * @param newDist New Distance
     */
    private void changeHeap(int i, double newDist) {
        binHeap[i].distance = newDist;
        heapUp(i);
    }

    /**
     * Доступ к элементу кучи по индексу.
     *
     * @param i Индекс элемента
     * @return
     */
    private Pair getFromHeap(int i) {
        return binHeap[i];
    }

    /**
     * Extract from heap of element with minimum distance to it.
     *
     * @return The element with highest priority (smallest distance).
     */
    private Pair extractHeap() {
        Pair minPair = binHeap[0];
        positions[minPair.vertex] = -1;
        if (--heapSize > 0) {
            binHeap[0] = binHeap[heapSize];
            binHeap[heapSize] = null;
            positions[binHeap[0].vertex] = 0;
            heapDown(0);
        }
        return minPair;
    }

    /**
     * Adding new item to heap.
     *
     * @param pair New item
     */
    private void addToHeap(Pair pair) {
        binHeap[positions[pair.vertex] = heapSize] = pair;
        heapUp(heapSize++);
    }

    /**
     * Check if heap is empty.
     *
     * @return
     */
    private boolean emptyHeap() {

        return heapSize == 0;
    }

    /**
     * Dragging a heap element at a specified index up the heap
     *
     * @param i Index of element
     */
    private void heapUp(int i) {
        Pair pair = binHeap[i];
        int prev = (i - 1) / 2;
        while (prev >= 0 && pair.compareTo(binHeap[prev]) < 0) {
            positions[binHeap[prev].vertex] = i;
            binHeap[i] = binHeap[prev];
            i = prev;
            if (prev == 0) break;
            prev = (i - 1) / 2;
        }
        positions[pair.vertex] = i;
        binHeap[i] = pair;
    }

    /**
     * Dragging a heap element with a specified index down the heap
     *
     * @param i Index of element
     */
    private void heapDown(int i) {
        Pair pair = binHeap[i];
        int next = 2 * i + 1;
        while (next < heapSize) {
            if (next + 1 < heapSize && binHeap[next + 1].compareTo(binHeap[next]) < 0) {
                next++;
            }
            if (pair.compareTo(binHeap[next]) <= 0) {
                break;
            }
            positions[binHeap[next].vertex] = i;
            binHeap[i] = binHeap[next];
            i = next;
            next = 2 * i + 1;
        }
        positions[pair.vertex] = i;
        binHeap[i] = pair;
    }

    /**
     * The pair from number of vertex and distance to it are heap element
     * Comparison by distance.
     */
    private static class Pair implements Comparable<Pair> {
        int vertex;
        double distance;

        public Pair(int v, double d) {
            vertex = v;
            distance = d;
        }

        @Override
        public int compareTo(Pair p) {
            return
                    distance < p.distance ? -1 :
                            distance == p.distance ? vertex - p.vertex : 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null && !(o instanceof Pair)) {
                return false;
            }
            Pair other = (Pair) o;
            return vertex == other.vertex && distance == other.distance;
        }

        @Override
        public int hashCode() {
            return vertex ^ new Double(distance).hashCode();
        }

        @Override
        public String toString() {
            return "(" + vertex + "," + distance + ")";
        }
    }
}
