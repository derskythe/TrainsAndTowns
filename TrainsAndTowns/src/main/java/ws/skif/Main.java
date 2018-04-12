package ws.skif;

import ws.skif.graph.models.Graph;
import ws.skif.graph.models.Route;
import ws.skif.graph.models.RouteList;
import ws.skif.graph.models.VertexPair;
import ws.skif.graph.utils.GraphUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Parsing arguments
        HashMap<String, String> argsMap;
        try {
            argsMap = parseArguments(args);
        } catch (IllegalArgumentException exp) {
            System.out.println(exp.getMessage());
            printHelp();
            return;
        }

        // Search for filename argument
        if (!argsMap.containsKey("f")) {
            System.out.println("Argument -f <filename> is missing");
            printHelp();
            return;
        }

        // Search for type of file
        if (!argsMap.containsKey("t")) {
            System.out.println("Argument -t m or -t l is missing");
            printHelp();
            return;
        }

        // Read file and make matrix
        Graph graph;
        try {
            if (argsMap.get("t").equalsIgnoreCase("m")) {
                graph = Graph.fromMatrix(argsMap.get("f"));
            } else {
                graph = Graph.fromList(argsMap.get("f"));
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exp) {
            System.out.println("Invalid file format!");
            return;
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
            return;
        }

        // Init routes
        List<VertexPair> route1 = new ArrayList<>();
        route1.add(new VertexPair(0, 1));
        route1.add(new VertexPair(1, 2));

        List<VertexPair> route2 = new ArrayList<>();
        route2.add(new VertexPair(0, 3));

        List<VertexPair> route3 = new ArrayList<>();
        route3.add(new VertexPair(0, 3));
        route3.add(new VertexPair(3, 2));

        List<VertexPair> route4 = new ArrayList<>();
        route4.add(new VertexPair(0, 4));
        route4.add(new VertexPair(4, 1));
        route4.add(new VertexPair(1, 2));
        route4.add(new VertexPair(2, 3));

        List<VertexPair> route5 = new ArrayList<>();
        route5.add(new VertexPair(0, 4));
        route5.add(new VertexPair(4, 3));

        GraphUtils utils = new GraphUtils(graph);

        List<Route> routeList9 = utils.commisVoyageur(1, -1, -1).getRoutes();
        double minRoute = Integer.MAX_VALUE;
        for (Route list : routeList9) {
            if (list.getLength() < minRoute) {
                minRoute = list.getLength();
            }
        }

        // Showtime
        System.out.println("#1: " + utils.getExactRoute(route1));
        System.out.println("#2: " + utils.getExactRoute(route2));
        System.out.println("#3: " + utils.getExactRoute(route3));
        System.out.println("#4: " + utils.getExactRoute(route4));
        System.out.println("#5: " + utils.getExactRoute(route5));
        System.out.println("#6: " + utils.commisVoyageur(2, 3, -1).getRoutes().size());
        System.out.println("#7: " + utils.findAllRoutes(0, 2, 4));
        System.out.println("#8: " + utils.getPathLength(0, 2));
        System.out.println("#9: " + minRoute);
        System.out.println("#10: " + utils.getAllRoutesCount(2, 30));
    }


    /**
     * Print help
     */
    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("<Application> -f \"<Filename>\" -t l - for the list type");
        System.out.println("<Application> -f \"<Filename>\" -t m - for the matrix type");
    }

    /**
     * Method to parse arguments and returns HashMap with flag as key and value as option
     *
     * @param args
     * @return
     */
    private static HashMap<String, String> parseArguments(String[] args) throws IllegalArgumentException {
        HashMap<String, String> values = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2)
                        throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                    if (args[i].charAt(1) == '-') {
                        values.put(args[i].substring(2, args[i].length()), args[i + 1]);
                        i++;
                    } else {
                        if (args.length - 1 == i)
                            throw new IllegalArgumentException("Expected arg after: " + args[i]);
                        // -opt
                        values.put(args[i].substring(1), args[i + 1]);
                        i++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Not a valid argument: " + args[i]);
            }
        }

        return values;
    }
}
