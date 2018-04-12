package ws.skif.graph.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ws.skif.graph.models.Graph;

import java.util.List;

class TarjanTest {

    private static Graph graph;

    @BeforeAll
    public static void beforeAll() {
        StringBuilder str = new StringBuilder();
        str.append("0,5.0,0,5.0,7.0,\n");
        str.append("0,0,4.0,0,0,\n");
        str.append("0,0,0,8.0,2.0,\n");
        str.append("0,0,8.0,0,6.0,\n");
        str.append("0,3.0,0,0,0,\n");

        graph = Graph.fromString(str.toString());
    }

    @Test
    void findAllCycles() {
        Tarjan tarjan = new Tarjan(graph);
        List<List<Integer>> list = tarjan.findAllCycles();
        Assertions.assertTrue(list != null);
        Assertions.assertTrue(list.size() == 3);
    }
}