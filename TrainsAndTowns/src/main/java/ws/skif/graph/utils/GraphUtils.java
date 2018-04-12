package ws.skif.graph.utils;


import ws.skif.graph.models.*;

import java.util.*;

public class GraphUtils {
    private double[][] paths;               // Distance matrix
    private int[][] directions;             // Direction Matrix
    private double[][] correctedPaths;      // Total routes
    private List<List<Integer>> cycles;

    /**
     * Constuctor. Run Johnson algo
     *
     * @param graph
     */
    public GraphUtils(Graph graph) {
        johnson(graph);
    }

    /**
     * The length of the minimum path is taken from the computed path matrix
     *
     * @param from From vertex
     * @param to   To vertex
     * @return The length of the minimum path between the source and to vertex
     */
    public double getPathLength(int from, int to) {

        return correctedPaths[from][to];
    }

    /**
     * Constructs the path of the minimum length between the given two vertexes
     *
     * @param from From vertex
     * @param to   To vertex
     * @return An array of vertex numbers that specify the path from the source vertex to the target vertex
     */
    public LinkedList<Integer> getPath(int from, int to) {
        LinkedList<Integer> path = new LinkedList<>();
        do {
            path.addFirst(to);
            to = directions[from][to];
        } while (to != -1);
        return path;
    }

    /**
     * The Traveling Salesman Problem
     *
     * @param startVertex Start position
     * @param maxStops    Maximum stops. -1 to run without checks of this param
     * @param maxLength   Maximum length. -1 to run without checks of this param
     * @return List of routes. Unsorted by length
     */
    public RouteList commisVoyageur(int startVertex, int maxStops, int maxLength) {
        RouteList result = new RouteList();

        // Seek all routes cycles with start point
        for (List<Integer> route : cycles) {
            boolean found = false;
            for (Integer vertex : route) {
                if (vertex == startVertex) {
                    found = true;
                    break;
                }
            }

            // We found searched cycle in this operation
            if (found) {
                int i = -1;
                List<Integer> list = new ArrayList<>();
                double len = 0;

                for (Integer vertex : route) {
                    if (i > -1) {
                        if (!list.contains(vertex)) {
                            list.add(vertex);
                        }
                        len += getPathLength(i, vertex);
                    }
                    i = vertex;
                }

                if ((maxStops == -1 || list.size() <= maxStops) &&
                        (maxLength == -1 || len <= maxLength)) {
                    result.getRoutes().add(new Route(list.size(), list, len));
                }
            }
        }

        return result;
    }

    /**
     * Find all routes with exact number of of hops
     *
     * @param sourceVertex
     * @param dstVertex
     * @param hops
     * @return
     */
    public int findAllRoutes(int sourceVertex, int dstVertex, int hops) {
        int total = 0;

        List<Route> numberOfRoutes = new ArrayList<>();

        for (int i = 0; i < paths.length; i++) {
            if (i != sourceVertex
                    && getPathLength(sourceVertex, i) < Integer.MAX_VALUE
                    && getPathLength(sourceVertex, i) != 0
                    && getPathLength(i, dstVertex) < Integer.MAX_VALUE
                    && i != dstVertex) {
                LinkedList<Integer> path = getPath(sourceVertex, i);
                if (path.size() - 1 < hops) {
                    numberOfRoutes.add(new Route(path.size(), path, 0));
                    for (int j = 0; j < paths.length; j++) {
                        if (i != j
                                && j != dstVertex
                                && getPathLength(sourceVertex, j) < Integer.MAX_VALUE
                                && getPathLength(sourceVertex, j) != 0
                                && getPathLength(j, dstVertex) < Integer.MAX_VALUE
                                && hasExactPath(i, j)) {
                            LinkedList<Integer> anotherPath = getPath(i, j);
                            LinkedList<Integer> originalPath = (LinkedList<Integer>) path.clone();
                            if (path.size() + anotherPath.size() - 2 < hops) {
                                originalPath.addAll(anotherPath.subList(1, anotherPath.size()));
                                numberOfRoutes.add(new Route(originalPath.size(), originalPath, 0));
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < paths.length; i++) {
            if (i != dstVertex && getPathLength(dstVertex, i) < Integer.MAX_VALUE) {
                LinkedList<Integer> path = getPath(dstVertex, i);
                LinkedList<Integer> returnPath = getPath(i, dstVertex);

                path.addAll(returnPath.subList(1, returnPath.size())); // Delete first item in path

                for (int j = 0; j < numberOfRoutes.size(); j++) {
                    if (path.size() + numberOfRoutes.get(j).getRoute().size() - 1 == hops) {
                        total++;
                    }
                }
            }
        }


        return total;
    }

    /**
     * Return number of different routes to this vertex with a distance of less than maxLen
     *
     * @param dstVertex Desired vertex
     * @param maxLen    Maximum length. -1 to run without checks of this param
     * @return Number of routes
     */
    public int getAllRoutesCount(int dstVertex, int maxLen) {
        RouteList routeList = commisVoyageur(dstVertex, maxLen, -1);

        // We got list of basic cycles. Now we trying to got extended cycles
        int numRoutes = 0;
        List<Double> values = new ArrayList<>();
        for (Route route : routeList.getRoutes()) {
            double routeLen = route.getLength();
            values.add(routeLen);
            numRoutes++;

            while (routeLen + route.getLength() <= maxLen) {
                routeLen += route.getLength();
                numRoutes++;
            }
        }

        // We can make addition of cycles
        List<Double> output = new ArrayList<>();
        permutation(values, 0, 0d, new boolean[values.size()], output, maxLen);

        return numRoutes + output.size();
    }

    /**
     * Search all permutation
     *
     * @param items
     * @param item
     * @param permutation
     * @param used
     * @param output
     * @param maxLen
     * @return
     */
    private double permutation(List<Double> items, int item, double permutation, boolean[] used,
                               List<Double> output, final double maxLen) {
        for (int i = 0; i < items.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                permutation += items.get(i);

                if (item < (items.size() - 1)) {
                    permutation = permutation(items, item + 1, permutation, used, output, maxLen);
                } else {
                    if (permutation <= maxLen) {
                        output.add(permutation);
                    }
                    permutation = 0d;
                }

                used[i] = false;
            }
        }

        return permutation;
    }

    /**
     * Do we have direct route between 2 vertexes?
     *
     * @param from From this vertex
     * @param to   To this vertex
     * @return
     */
    public boolean hasExactPath(int from, int to) {

        return (paths[from][to] > 0 && paths[from][to] < Integer.MAX_VALUE);
    }

    /**
     * Get length of route for exact path
     *
     * @param routeList
     * @return
     */
    public String getExactRoute(List<VertexPair> routeList) {
        double length = 0f;
        for (VertexPair route : routeList) {
            if (hasExactPath(route.getFrom(), route.getTo())) {
                length += getPathLength(route.getFrom(), route.getTo());
            } else {
                return "NO SUCH ROUTE";
            }
        }

        return String.valueOf(length);
    }

    /**
     * Implementation of the Johnson algo
     */
    private void johnson(Graph graph) {
        int totalNumber = graph.getCount();

        // 1. Add a new vertex to the graph and draw edges
        //    with zero length from it to all other vertices - O(n)
        int newVertex = graph.addVertex();
        for (int i = 0; i < totalNumber; i++) {
            graph.addEdge(newVertex, i, 0);
        }

        // 2. Run the Bellman-Ford algo to calculate the lengths
        //    minimum paths from this vertex to all others - O(n*(n+m)).
        BellmanFord bf = new BellmanFord(graph);
        double[] f = bf.getDistances(newVertex);

        // 3. Delete the added vertex and correct the lengths of all edges with
        //    found lengths of paths so that all lengths become non negative - O(n+m).
        graph.removeVertex(newVertex);
        for (int i = 0; i < totalNumber; i++) {
            for (Edge edge : graph.getListEdge(i)) {
                edge.addWeight(f[i] - f[edge.to()]);
            }
        }

        // 4. Now for each vertex we run the Dijkstra algo - O(n*(n+m)*log n)
        Dijkstra dijkstra = new Dijkstra(graph);

        paths = new double[totalNumber][totalNumber];
        correctedPaths = new double[totalNumber][];
        directions = new int[totalNumber][];

        for (int i = 0; i < totalNumber; i++) {
            correctedPaths[i] = dijkstra.getDistances(i);
            directions[i] = dijkstra.getTree(i);
        }

        // 5. Restore the original edge lengths - O(n+m).
        for (int i = 0; i < totalNumber; i++) {
            for (Edge edge : graph.getListEdge(i)) {
                paths[i][edge.to()] = edge.weight();
                edge.addWeight(f[edge.to()] - f[i]);
            }
        }

        // 6. Correct the matrix of path lengths - O(n*n).
        for (int i = 0; i < totalNumber; i++) {
            for (int j = 0; j < totalNumber; ++j) {
                correctedPaths[i][j] += f[j] - f[i];
            }
        }

        // 7. Create a list of cycles - O(n+m).
        Tarjan tarjan = new Tarjan(graph);
        cycles = tarjan.findAllCycles();
    }
}
