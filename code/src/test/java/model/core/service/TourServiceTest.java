package model.core.service;

import model.data.*;
import model.io.XmlToGraph;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TourServiceTest {

    private TourService tourService;
    private GraphService graphService;
    private XmlToGraph xmlToGraph;
    private XmlToGraph xmlToGraph1;
    private List<Point> points;
    private Graph graph;
    private Tour tourBeforeCalculate;
    private Tour tourAfterCalculate;
    private ActionPoint base;
    private ActionPoint actionPoint;
    private ActionPoint actionPoint1;
    private ActionPoint end;
    private ActionPoint notInTour;

    @BeforeAll
    void setUp() {
        tourService = new TourService();
        graphService = new GraphService();
        xmlToGraph = new XmlToGraph();
        xmlToGraph1 = new XmlToGraph();
        points = xmlToGraph.getGraphFromXml("resource/petitPlan.xml");
        tourBeforeCalculate = xmlToGraph.getDeliveriesFromXml("resource/demandePetit1.xml");
        tourAfterCalculate = xmlToGraph1.getDeliveriesFromXml("resource/demandePetit1.xml");
        graph = new Graph(points);
        tourAfterCalculate = graphService.calculateTour(tourAfterCalculate, graph);
        base = new ActionPoint(xmlToGraph.durationToTime(0), XmlToGraph.getPointById(342873658), ActionType.BASE);
        actionPoint = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(208769039), ActionType.PICK_UP);
        actionPoint1 = new ActionPoint(xmlToGraph.durationToTime(240), XmlToGraph.getPointById(25173820), ActionType.DELIVERY);
        end = new ActionPoint(xmlToGraph.durationToTime(0), XmlToGraph.getPointById(342873658), ActionType.END);
        notInTour = new ActionPoint(xmlToGraph.durationToTime(0), new Point(1, 10, 10), ActionType.END);
    }

    @Nested
    class calculateTimeAtPoint {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.calculateTimeAtPoint(tourBeforeCalculate, null)),
                    () -> assertEquals("", tourService.calculateTimeAtPoint(null, actionPoint)),
                    () -> assertEquals("", tourService.calculateTimeAtPoint(tourBeforeCalculate, actionPoint))
            );
        }

        @Test
        void correctUsage() {
            assertAll(
                    () -> assertEquals("08:00:00", tourService.calculateTimeAtPoint(tourAfterCalculate, base)),
                    () -> assertEquals("08:04:25", tourService.calculateTimeAtPoint(tourAfterCalculate, actionPoint)),
                    () -> assertEquals("08:17:55", tourService.calculateTimeAtPoint(tourAfterCalculate, actionPoint1)),
                    () -> assertEquals("08:28:54", tourService.calculateTimeAtPoint(tourAfterCalculate, end)),
                    () -> assertEquals("", tourService.calculateTimeAtPoint(tourAfterCalculate, notInTour))
            );
        }
    }

    @Test
    void changeDeliveryOrder() {
    }

    @Test
    void changePointPosition() {
    }

    @Test
    void addNewDeliveryProcess() {
    }

    @Test
    void deleteDeliveryProcess() {
    }
}