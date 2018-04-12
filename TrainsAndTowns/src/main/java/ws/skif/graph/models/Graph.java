package ws.skif.graph.models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Graph
 */
public class Graph {

    private List<List<Edge>> graphList;                 // List of adjacent
    private int totalVertex;                            // Number of vertexes
    private boolean negativeEdge;                       // Graph has negative edges
    private Map<Integer, List<Integer>> vertexAdjacent;

    /**
     * Empty constructor
     */
    public Graph() {
        graphList = new ArrayList<>();
        vertexAdjacent = new HashMap<>();
    }

    /**
     * Number of vertex for graph
     *
     * @return
     */
    public int getCount() {
        return totalVertex;
    }

    /**
     * Graph has negative edges
     *
     * @return
     */
    public boolean isNegativeEdge() {
        return negativeEdge;
    }

    /**
     * Add edge to the graph.
     * It is assumed that earlier there was no such edge.
     *
     * @param from     Source vertex (start)
     * @param to       Destination vertex (end)
     * @param distance Distance
     */
    public void addEdge(int from, int to, double distance) {
        graphList.get(from).add(new Edge(to, distance));
    }

    /**
     * Add new adjacent
     *
     * @param vertex
     * @param sourceVertex
     */
    private void addAdjacent(int vertex, int sourceVertex) {
        if (vertexAdjacent.containsKey(vertex)) {
            vertexAdjacent.get(vertex).add(sourceVertex);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(sourceVertex);
            vertexAdjacent.put(vertex, list);
        }
    }

    /**
     * Add new vertex and return vertex index
     *
     * @return
     */
    public int addVertex() {
        graphList.add(new ArrayList<>());
        totalVertex++;
        return graphList.size() - 1;
    }

    /**
     * Add new vertex and return vertex index.
     * Or simply return index
     *
     * @param i
     * @return
     */
    public int addVertex(int i) {
        if (graphList.size() - 1 < i) {
            graphList.add(new ArrayList<>());
            totalVertex++;
            return graphList.size() - 1;
        }

        return i;
    }

    /**
     * Get list of adjacent
     *
     * @param vertex
     * @return
     */
    public List<Integer> getVertexAdjacent(int vertex) {
        if (vertexAdjacent.containsKey(vertex)) {
            return vertexAdjacent.get(vertex);
        }

        return new ArrayList<>();
    }

    /**
     * Removes vertex
     *
     * @param vertex
     */
    public void removeVertex(int vertex) {
        graphList.remove(vertex);
        totalVertex--;
        for (List<Edge> list : graphList) {
            for (Iterator<Edge> iEdge = list.iterator(); iEdge.hasNext(); ) {
                Edge edge = iEdge.next();
                if (edge.to == vertex) {
                    iEdge.remove();
                } else if (edge.to > vertex) {
                    edge.to--;
                }
            }
        }
    }


    /**
     * List of edges of current vertex
     *
     * @param vertex
     * @return List of edges
     */
    public List<Edge> getListEdge(int vertex) {
        return graphList.get(vertex);
    }

    /**
     * Read matrix from source path
     *
     * @param fileName
     * @throws IOException
     * @throws NumberFormatException
     * @throws IndexOutOfBoundsException
     */
    public static Graph fromMatrix(String fileName)
            throws IOException, NumberFormatException, IndexOutOfBoundsException {

        int i = 0;
        Graph graph = new Graph();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                                          StandardCharsets.US_ASCII))) {
            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                String[] values = line.split(",");

                String lastValue = values[values.length - 1].trim();
                int len = lastValue == null || lastValue.isEmpty() ? values.length - 1 : values.length;

                graph.addVertex(i);
                for (int j = 0; j < len; j++) {
                    Double edge = Double.valueOf(values[j].trim());

                    if (edge.doubleValue() < 0) {
                        graph.negativeEdge = true;
                    } else if (edge.doubleValue() == 0) {
                        edge = Double.MAX_VALUE;
                    } else {
                        graph.addAdjacent(i, j);
                    }

                    graph.addEdge(i, j, edge);
                }
                line = br.readLine();
                i++;
            }
        } catch (NumberFormatException | IOException exp) {
            throw exp;
        }

        return graph;
    }

    /**
     * Return pure matrix
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int u = 0; u < totalVertex; ++u) {
            for (Edge to : getListEdge(u)) {
                if (to.weight < Integer.MAX_VALUE) {
                    str.append(String.valueOf(to.weight)).append(",");
                } else {
                    str.append(String.valueOf(0)).append(",");
                }
            }
            str.append(System.lineSeparator());
        }

        return str.toString();
    }

    /**
     * Read list of vertexes from source path
     *
     * @param fileName
     * @throws IOException
     * @throws NumberFormatException
     * @throws IndexOutOfBoundsException
     */
    public static Graph fromList(String fileName)
            throws IOException, NumberFormatException, IndexOutOfBoundsException {
        Graph graph = new Graph();
        Pattern p = Pattern.compile("^([A-Z]{2})(\\-*[0-9]+)$");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                                          StandardCharsets.US_ASCII))) {
            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                String[] values = line.split(",");

                for (String item : values) {
                    Matcher m = p.matcher(item.trim().toUpperCase());
                    m.matches();
                    String vertexValue = m.group(1);

                    int v1 = (int) (vertexValue.charAt(0)) - 65;
                    int v2 = (int) (vertexValue.charAt(1)) - 65;

                    graph.addVertex(v1);
                    graph.addVertex(v2);

                    Double edge = Double.valueOf(m.group(2));

                    if (edge.doubleValue() < 0) {
                        graph.negativeEdge = true;
                    } else if (edge.doubleValue() == 0) {
                        edge = Double.MAX_VALUE;
                    } else {
                        graph.addAdjacent(v1, v2);
                    }

                    graph.addEdge(v1, v2, edge);
                }
                line = br.readLine();
            }
        } catch (NumberFormatException | IOException exp) {
            throw exp;
        }

        return graph;
    }

    /**
     * Read list of vertexes from string matrix
     *
     * @param matrix
     * @throws NumberFormatException
     * @throws IndexOutOfBoundsException
     */
    public static Graph fromString(String matrix)
            throws NumberFormatException, IndexOutOfBoundsException {
        int i = 0;
        Graph graph = new Graph();

        try {
            String[] lines = matrix.split("\n");

            for (String line : lines) {
                String[] values = line.split(",");

                String lastValue = values[values.length - 1].trim();
                int len = lastValue == null || lastValue.isEmpty() ? values.length - 1 : values.length;

                graph.addVertex(i);
                for (int j = 0; j < len; j++) {
                    Double edge = Double.valueOf(values[j].trim());

                    if (edge.doubleValue() < 0) {
                        graph.negativeEdge = true;
                    } else if (edge.doubleValue() == 0) {
                        edge = Double.MAX_VALUE;
                    } else {
                        graph.addAdjacent(i, j);
                    }

                    graph.addEdge(i, j, edge);
                }
                i++;
            }
        } catch (NumberFormatException exp) {
            throw exp;
        }

        return graph;
    }
}
