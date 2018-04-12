package ws.skif.graph.utils;

import ws.skif.graph.models.Graph;

import java.util.*;

public class Tarjan {
    private Set<Integer> visited;
    private Deque<Integer> pointStack;
    private Deque<Integer> markedStack;
    private Set<Integer> markedSet;
    private Graph graph;

    /**
     * Constructor
     *
     * @param graph Selected graph
     */
    public Tarjan(Graph graph) {
        this.graph = graph;

        visited = new HashSet<>();
        pointStack = new LinkedList<>();
        markedStack = new LinkedList<>();
        markedSet = new HashSet<>();
    }

    public List<List<Integer>> findAllCycles() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < graph.getCount(); i++) {
            findAllCycles(i, i, result);
            visited.add(i);

            while (!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }
        return result;
    }

    private boolean findAllCycles(Integer start, Integer current, List<List<Integer>> result) {
        boolean hasCycle = false;
        pointStack.offerFirst(current);
        markedSet.add(current);
        markedStack.offerFirst(current);

        for (Integer w : graph.getVertexAdjacent(current)) {
            if (visited.contains(w)) {
                continue;
            } else if (w.equals(start)) {
                hasCycle = true;
                pointStack.offerFirst(w);
                List<Integer> cycle = new ArrayList<>();
                Iterator<Integer> itr = pointStack.descendingIterator();
                while (itr.hasNext()) {
                    cycle.add(itr.next());
                }
                pointStack.pollFirst();
                result.add(cycle);
            } else if (!markedSet.contains(w)) {
                hasCycle = findAllCycles(start, w, result) || hasCycle;
            }
        }

        if (hasCycle) {
            while (!markedStack.peekFirst().equals(current)) {
                markedSet.remove(markedStack.pollFirst());
            }
            markedSet.remove(markedStack.pollFirst());
        }

        pointStack.pollFirst();
        return hasCycle;
    }
}
