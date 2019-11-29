package model.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphTest {

    private List<Point> points;
    private  Graph graph;
    private Map<Long,Integer> map;

    @BeforeAll
    void setUp() {
        points = new ArrayList<>();
        points.add(new Point(12345,40.5,40.5));
        map = new HashMap<>();
        map.put((long) 12345,0);
        graph = new Graph(points);
    }

    @Nested
    class Constructor {
        @Test
        void nullPoints() {
            assertThrows(IllegalArgumentException.class, () -> new Graph(null));
        }

        @Test
        void hasNullPoint() {
            List<Point> testPoints = new ArrayList<>();
            testPoints.add(null);
            assertThrows(IllegalArgumentException.class, () -> new Graph(testPoints));
        }
    }

    @Test
    void getPoints() {
        assertEquals(points, graph.getPoints(),
                () -> "getPoints should return the list of points of the graph");
    }

    @Test
    void getMap() {
        assertEquals(map, graph.getMap(),
                () -> "getMap should return a map<Long, Integer>");
    }

    @Test
    void getNb_points() {
        assertEquals(1, graph.getNb_points(),
                () -> "getNb_points should return the number of points in the graph");
    }
}