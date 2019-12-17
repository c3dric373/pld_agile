package controller.service;

import controller.tsp.TSP;
import controller.tsp.TSP3;
import controller.io.XmlToGraph;
import model.genData.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    private XmlToGraph xmlToGraph;
    private GraphService graphService;
    private List<Point> points;
    private Tour tour;
    private Graph graph;
    private TSP tsp;

    @BeforeEach
    void setUp() {
        xmlToGraph = new XmlToGraph();
        graphService = new GraphService();
        points = xmlToGraph.getGraphFromXml("resource/petitPlan.xml");
        tour = xmlToGraph.getDeliveriesFromXml("resource/demandePetit1.xml");
        graph = new Graph(points);
        tsp = new TSP3();
    }

    @Nested
    class findNearestPoint {
        @Test
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> graphService.findNearestPoint(null, 4.8629594, 45.759098));
        }

        @Test
        void correctUsage() {
            Point point = new Point(26086124, 45.759098, 4.8629594);
            Point nearestPoint = graphService.findNearestPoint(graph, 4.8629594, 45.759098);
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
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> graphService.addGraphCenter(null));
        }

        @Test
        void correctUsage() {
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
    void isInMap() {
        // TODO
        assertFalse(graphService.isInMap(new Point(1, 40, 40)));
    }

    @Nested
    class dijkstra {
        @Test
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> graphService.dijkstra(null, 208769039));
        }

        @Test
        void pointNotInGraph() {
            assertThrows(IllegalArgumentException.class, () -> graphService.dijkstra(graph, 1));
        }

        @Test
        void correctUsage() {
            assertNotNull(graphService.dijkstra(graph, 208769039),
                    "dijkstra should return List<Tuple>");
        }
    }

    @Nested
    class getShortestPath {
        @Test
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> graphService.getShortestPath(null, 208769039, 25173820, null));
        }

        @Test
        void pointNotInGraph() {
            assertThrows(IllegalArgumentException.class, () -> graphService.getShortestPath(graph, 1, 25173820, null));
            assertThrows(IllegalArgumentException.class, () -> graphService.getShortestPath(graph, 208769039, 1, null));
        }

        @Test
        void correctUsage() {
            assertNotNull(graphService.getShortestPath(graph, 208769039, 25173820, null),
                    "getShortestPath should return a journey");
        }
    }

    @Nested
    class applyDijkstraToTour {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.applyDijkstraToTour(null, graph)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.applyDijkstraToTour(tour, null))
            );
        }

        @Test
        void correctUsage() {
            assertNotNull(graphService.applyDijkstraToTour(tour, graph),
                    "applyDijkstraToTour should return List<List<Tuple>>");
        }
    }

    @Nested
    class getCost {
        @Test
        void nullParameter() {
            List<List<GraphService.Tuple>> resDijkstra = graphService.applyDijkstraToTour(tour, graph);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getCost(null, graph, resDijkstra)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getCost(tour, null, resDijkstra))
            );
        }

        @Test
        void correctUsage() {
            int[][] cost = new int[3][3];
            cost[0][1] = 85;
            cost[0][2] = 573;
            cost[1][0] = 55;
            cost[1][2] = 570;
            cost[2][0] = 659;
            cost[2][1] = 642;
            List<List<GraphService.Tuple>> resDijkstra = graphService.applyDijkstraToTour(tour, graph);
            int[][] res = graphService.getCost(tour, graph, resDijkstra);
            assertAll(
                    () -> assertEquals(cost[0][0], res[0][0], "getCost should return the cost between points"),
                    () -> assertEquals(cost[0][1], res[0][1], "getCost should return the cost between points"),
                    () -> assertEquals(cost[0][2], res[0][2], "getCost should return the cost between points"),
                    () -> assertEquals(cost[1][0], res[1][0], "getCost should return the cost between points"),
                    () -> assertEquals(cost[1][1], res[1][1], "getCost should return the cost between points"),
                    () -> assertEquals(cost[1][2], res[1][2], "getCost should return the cost between points"),
                    () -> assertEquals(cost[2][0], res[2][0], "getCost should return the cost between points"),
                    () -> assertEquals(cost[2][1], res[2][1], "getCost should return the cost between points"),
                    () -> assertEquals(cost[2][2], res[2][2], "getCost should return the cost between points")
            );
        }
    }

    @Nested
    class getDuration {
        @Test
        void nullParameter() {
            assertThrows(IllegalArgumentException.class, () -> graphService.getDuration(null));
        }

        @Test
        void correctUsage() {
            int[] duration = new int[3];
            duration[1] = 180;
            duration[2] = 240;
            int[] res = graphService.getDuration(tour);
            assertAll(
                    () -> assertEquals(duration[0], res[0], "getCost should return the cost between points"),
                    () -> assertEquals(duration[1], res[1], "getCost should return the cost between points"),
                    () -> assertEquals(duration[2], res[2], "getCost should return the cost between points")
            );
        }
    }

    @Nested
    class getListJourney {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getListJourney(null, graph, tsp, Integer.MAX_VALUE)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getListJourney(tour, null, tsp, Integer.MAX_VALUE)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getListJourney(tour, graph, null, Integer.MAX_VALUE))
            );
        }

        @Test
        void correctUsage() {
            assertNotNull(graphService.getListJourney(tour, graph, tsp, Integer.MAX_VALUE),
                    "getListJourney should return a list of journeys");
        }
    }

    @Nested
    class calculateTour {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.calculateTour(null, graph)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.calculateTour(tour, null))
            );
        }

        @Test
        void incompatibleParameter() {
            // here we test incompatibility between large graph and small demand,
            // incompatibility between small graph and large demand should be tested in XmlToGraphTest
            points = xmlToGraph.getGraphFromXml("resource/grandPlan.xml");
            graph = new Graph(points);
            graphService.calculateTour(tour, graph);
            //assertThrows(IllegalArgumentException.class, () -> graphService.calculateTour(tour, graph));
        }

        @Test
        void correctUsage() {
            Tour res = graphService.calculateTour(tour, graph);
            assertNotNull(res, "calculateTour should return a tour");
        }
    }

    @Nested
    class getJourneysForDeliveryProcess {
        @Test
        void nullParameter() {
            DeliveryProcess deliveryProcess = tour.getDeliveryProcesses().get(0);
            List<Journey> journeys = graphService.getListJourney(tour, graph, tsp, Integer.MAX_VALUE);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getJourneysForDeliveryProcess(null, deliveryProcess)),
                    () -> assertThrows(IllegalArgumentException.class, () -> graphService.getJourneysForDeliveryProcess(journeys, null))
            );
        }

        @Test
        void correctUsage() {
            DeliveryProcess deliveryProcess = tour.getDeliveryProcesses().get(0);
            List<Journey> journeys = graphService.getListJourney(tour, graph, tsp, Integer.MAX_VALUE);
            assertNotNull(graphService.getJourneysForDeliveryProcess(journeys, deliveryProcess),
                    "getListJourney should return a list of journeys");
        }
    }
}