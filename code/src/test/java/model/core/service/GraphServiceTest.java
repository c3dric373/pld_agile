package model.core.service;

import model.data.Graph;
import model.data.Point;
import model.data.Tour;
import model.io.XmlToGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphServiceTest {

    private XmlToGraph xmlToGraph;
    private GraphService graphService;
    private List<Point> points;
    private Tour tour;
    private Graph graph;

    @BeforeAll
    void setUp() {
        xmlToGraph = new XmlToGraph();
        graphService = new GraphService();
        points = xmlToGraph.getGraphFromXml("resource/petitPlan.xml");
        tour = xmlToGraph.getDeliveriesFromXml("resource/demandePetit1.xml");
        graph = new Graph(points);
    }

    @Nested
    class findNearestPoint {
        @Test
        void nullPoints() {
            assertThrows(IllegalArgumentException.class, () -> graphService.findNearestPoint(null, 4.8629594, 45.759098));
        }

        @Test
        void correctNearestPoint() {
            Point point = new Point(26086124, 45.759098, 4.8629594);
            Point nearestPoint = graphService.findNearestPoint(points, 4.8629594, 45.759098);
            assertAll(
                    () -> assertEquals(point.getId(), nearestPoint.getId(),
                            "findNearestPoint should return the nearest point's Id"),
                    () -> assertEquals(point.getLatitude(), nearestPoint.getLatitude(),
                            "findNearestPoint should return the nearest point's Latitude"),
                    () -> assertEquals(point.getLongitude(), nearestPoint.getLongitude(),
                            "findNearestPoint should return the nearest point's longitude")
            );
        }
    }

    @Nested
    class addGraphCenter {
        @Test
        void nullGraph() {
            assertThrows(IllegalArgumentException.class, () -> graphService.addGraphCenter(null));
        }

        @Test
        void correctGraphCenter() {
            Point point = new Point(1, 45.7549175, 4.86801215);
            Point center = graphService.addGraphCenter(graph);
            assertAll(
                    () -> assertEquals(point.getId(), center.getId(),
                            "addGraphCenter should return the center point's Id"),
                    () -> assertEquals(point.getLatitude(), center.getLatitude(),
                            "addGraphCenter should return the center point's Latitude"),
                    () -> assertEquals(point.getLongitude(), center.getLongitude(),
                            "addGraphCenter should return the center point's longitude")
            );
        }
    }

    @Test
    void calculateTour() {
    }

    @Test
    void isInMap() {
    }

    @Test
    void dijkstra() {
    }

    @Test
    void getShortestPath() {
    }

    @Test
    void applyDijkstraToTour() {
    }

    @Test
    void getCost() {
    }

    @Test
    void getDuration() {
    }

    @Test
    void getListJourney() {
    }

    @Test
    void getJourneysForDeliveryProcess() {
    }

    @Test
    void main() {
    }
}