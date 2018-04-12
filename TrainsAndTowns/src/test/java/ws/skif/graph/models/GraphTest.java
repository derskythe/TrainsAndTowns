package ws.skif.graph.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @BeforeAll
    public static void beforeAll() throws IOException {
        List<String> str = new ArrayList<>();
        str.add("0,5.0,0,5.0,7.0,");
        str.add("0,0,4.0,0,0,");
        str.add("0,0,0,8.0,2.0,");
        str.add("0,0,8.0,0,6.0,");
        str.add("0,3.0,0,0,0,");

        Path file = Paths.get("matrix.txt");
        Files.write(file, str, Charset.defaultCharset());

        str = new ArrayList<>();
        str.add("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");

        file = Paths.get("list.txt");
        Files.write(file, str, Charset.defaultCharset());
    }

    @Test
    void fromMatrix() throws IOException {
        String value =
                "0,5.0,0,5.0,7.0," + System.lineSeparator()
                        + "0,0,4.0,0,0," + System.lineSeparator()
                        + "0,0,0,8.0,2.0," + System.lineSeparator()
                        + "0,0,8.0,0,6.0," + System.lineSeparator()
                        + "0,3.0,0,0,0," + System.lineSeparator();

        Path file = Paths.get("matrix.txt");
        Graph graph = Graph.fromMatrix(file.toAbsolutePath().toString());

        Assertions.assertTrue(graph != null);
        Assertions.assertTrue(!graph.isNegativeEdge());
        Assertions.assertTrue(graph.getCount() == 5);
        Assertions.assertTrue(graph.toString().equals(value));
    }

    @Test
    void fromList() throws IOException {
        String value =
                "5.0,5.0,7.0," + System.lineSeparator()
                        + "4.0," + System.lineSeparator()
                        + "8.0,2.0," + System.lineSeparator()
                        + "8.0,6.0," + System.lineSeparator()
                        + "3.0," + System.lineSeparator();

        Path file = Paths.get("list.txt");
        Graph graph = Graph.fromList(file.toAbsolutePath().toString());

        Assertions.assertTrue(graph != null);
        Assertions.assertTrue(!graph.isNegativeEdge());
        Assertions.assertTrue(graph.getCount() == 5);
        Assertions.assertTrue(graph.toString().equals(value));
    }

    @Test
    void fromString() {
        String value =
                "0,5.0,0,5.0,7.0," + System.lineSeparator()
                        + "0,0,4.0,0,0," + System.lineSeparator()
                        + "0,0,0,8.0,2.0," + System.lineSeparator()
                        + "0,0,8.0,0,6.0," + System.lineSeparator()
                        + "0,3.0,0,0,0," + System.lineSeparator();
        StringBuilder str = new StringBuilder();
        str.append("0,5.0,0,5.0,7.0,\n");
        str.append("0,0,4.0,0,0,\n");
        str.append("0,0,0,8.0,2.0,\n");
        str.append("0,0,8.0,0,6.0,\n");
        str.append("0,3.0,0,0,0,\n");

        Graph graph = Graph.fromString(str.toString());

        Assertions.assertTrue(graph != null);
        Assertions.assertTrue(!graph.isNegativeEdge());
        Assertions.assertTrue(graph.getCount() == 5);
        Assertions.assertTrue(graph.toString().equals(value));
    }
}