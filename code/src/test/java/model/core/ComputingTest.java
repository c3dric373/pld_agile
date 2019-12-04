package model.core;

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
class ComputingTest {

    private Computing computing;
    private List<Point> points;
    private Graph graph;
    private Tour tour;

    @BeforeAll
    void setUp() {
        computing = new Computing();
        XmlToGraph xmlToGraph = new XmlToGraph();
        String file_graph = "/Users/noe/Desktop/ETUDE/semester 3/Agile/fichiersXML2019/petitPlan.xml";
        String file_tour = "/Users/noe/Desktop/ETUDE/semester 3/Agile/fichiersXML2019/demandePetit1.xml";
        points = xmlToGraph.getGraphFromXml(file_graph);
        tour = xmlToGraph.getDeliveriesFromXml(file_tour);
        graph = new Graph(points);
    }

    @Nested
    class Dijkstra {
        @Test
        void nullGraph() {
            assertThrows(IllegalArgumentException.class, () -> computing.dijkstra(null, 25175791));
        }
        @Test
        void notInGraph() {
            assertThrows(IllegalArgumentException.class, () -> computing.dijkstra(graph, 12345));
        }
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
    void getListJourney() {
    }

    @Test
    void getJourneysForDeliveryProcess() {
    }
}