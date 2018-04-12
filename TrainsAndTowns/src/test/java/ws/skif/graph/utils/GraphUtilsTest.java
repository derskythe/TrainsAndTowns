package ws.skif.graph.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ws.skif.graph.models.Graph;

import static org.junit.jupiter.api.Assertions.*;

class GraphUtilsTest {
    private static GraphUtils utils;

    @BeforeAll
    public static void beforeAll() {
        StringBuilder str = new StringBuilder();
        str.append("0,5.0,0,5.0,7.0,\n");
        str.append("0,0,4.0,0,0,\n");
        str.append("0,0,0,8.0,2.0,\n");
        str.append("0,0,8.0,0,6.0,\n");
        str.append("0,3.0,0,0,0,\n");

        utils = new GraphUtils(Graph.fromString(str.toString()));
    }

    @Test
    void getPathLength() {
        Assertions.assertTrue(utils.getPathLength(0, 1) == 5d);
        Assertions.assertTrue(utils.getPathLength(0, 2) == 9d);
        Assertions.assertTrue(utils.getPathLength(0, 3) == 5d);
        Assertions.assertTrue(utils.getPathLength(0, 4) == 7d);
        Assertions.assertTrue(utils.getPathLength(4, 2) == 7d);
    }

    @Test
    void getPath() {
        Assertions.assertTrue(utils.getPath(0, 1).size() == 2);
        Assertions.assertTrue(utils.getPath(0, 2).size() == 3);
        Assertions.assertTrue(utils.getPath(0, 3).size() == 2);
        Assertions.assertTrue(utils.getPath(0, 4).size() == 2);
    }

    @Test
    void commisVoyageur() {
        Assertions.assertTrue(utils.commisVoyageur(2, 3, -1).getRoutes().size() == 2);
    }

    @Test
    void findAllRoutes() {
        Assertions.assertTrue(utils.findAllRoutes(0, 2, 4) == 3);
    }

    @Test
    void getAllRoutesCount() {
        Assertions.assertTrue(utils.getAllRoutesCount(2, 30) == 7);
    }

    @Test
    void hasExactPath() {
        Assertions.assertTrue(utils.hasExactPath(0, 1));
        Assertions.assertTrue(!utils.hasExactPath(0, 2));
        Assertions.assertTrue(utils.hasExactPath(0, 3));
        Assertions.assertTrue(utils.hasExactPath(0, 4));
    }
}