package model.core.service;

import model.data.*;
import model.io.XmlToGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TourServiceTest {

    private TourService tourService;
    private GraphService graphService;
    private XmlToGraph xmlToGraph;
    private XmlToGraph xmlToGraph1;
    private List<Point> points;
    private List<Point> points1;
    private Graph graph;
    private Graph graph1;
    private Tour tourBeforeCalculate;
    private Tour tourAfterCalculate;
    private ActionPoint base;
    private ActionPoint actionPoint;
    private ActionPoint actionPoint1;
    private ActionPoint end;
    private ActionPoint notInTour;

    @BeforeEach
    void setUp() {
        tourService = new TourService();
        graphService = new GraphService();
        xmlToGraph = new XmlToGraph();
        xmlToGraph1 = new XmlToGraph();
        points = xmlToGraph.getGraphFromXml("resource/petitPlan.xml");
        points1 = xmlToGraph1.getGraphFromXml("resource/petitPlan.xml");
        tourBeforeCalculate = xmlToGraph.getDeliveriesFromXml("resource/demandePetit1.xml");
        tourAfterCalculate = xmlToGraph1.getDeliveriesFromXml("resource/demandePetit1.xml");
        graph = new Graph(points);
        graph1 = new Graph(points1);
        tourAfterCalculate = graphService.calculateTour(tourAfterCalculate, graph1);
        base = new ActionPoint(xmlToGraph.durationToTime(0), XmlToGraph.getPointById(342873658), ActionType.BASE);
        actionPoint = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(208769039), ActionType.PICK_UP);
        actionPoint1 = new ActionPoint(xmlToGraph.durationToTime(240), XmlToGraph.getPointById(25173820), ActionType.DELIVERY);
        end = new ActionPoint(xmlToGraph.durationToTime(0), XmlToGraph.getPointById(342873658), ActionType.END);
        notInTour = new ActionPoint(xmlToGraph.durationToTime(100), new Point(1, 10, 10), ActionType.PICK_UP);
    }

    @Test
    void calculateTimeAtPoint() {
        tourService.calculateTimeAtPoint(null);
        tourService.calculateTimeAtPoint(tourAfterCalculate);
    }

    @Nested
    class getCompleteDistance {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.getCompleteDistance(null)),
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.getCompleteDistance(tourBeforeCalculate))
            );
        }

        @Test
        void correctUsage() {
            assertEquals(5477, tourService.getCompleteDistance(tourAfterCalculate),
                    "getCompleteDistance should return the right distance");
        }
    }

    @Nested
    class getCompleteTime {
        @Test
        void nullParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.getCompleteDistance(null)),
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.getCompleteDistance(tourBeforeCalculate))
            );
        }

        @Test
        void correctUsage() {
            Time res = tourService.getCompleteTime(tourAfterCalculate);
            assertEquals(-1866000.0, res.getTime(),
                    "getCompleteTime should return the right time");
        }
    }

    @Nested
    class deleteDpTourNotCalculated {
        @Test
        void nullParameter() {
            List<DeliveryProcess> deliveryProcesses = tourBeforeCalculate.getDeliveryProcesses();
            Tour withActionPointList = new Tour(deliveryProcesses, base.getLocation(), tourBeforeCalculate.getStartTime());
            withActionPointList.setActionPoints(tourAfterCalculate.getActionPoints());
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDpTourNotCalculated(null, tourBeforeCalculate.getDeliveryProcesses().get(0))),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDpTourNotCalculated(tourAfterCalculate, tourAfterCalculate.getDeliveryProcesses().get(0))),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDpTourNotCalculated(withActionPointList, withActionPointList.getDeliveryProcesses().get(0)))
            );
        }

        @Test
        void correctUsage() {
            tourService.deleteDpTourNotCalculated(tourBeforeCalculate, tourBeforeCalculate.getDeliveryProcesses().get(0));
            assertEquals(0, tourBeforeCalculate.getDeliveryProcesses().size(),
                    "deleteDpTourNotCalculated should delete the selected deliveryProcess");
        }
    }

    @Nested
    class addDpTourNotCalculated {
        @Test
        void nullParameter() {
            List<DeliveryProcess> deliveryProcesses = tourBeforeCalculate.getDeliveryProcesses();
            Tour withActionPointList = new Tour(deliveryProcesses, base.getLocation(), tourBeforeCalculate.getStartTime());
            withActionPointList.setActionPoints(tourAfterCalculate.getActionPoints());
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addDpTourNotCalculated(null, tourBeforeCalculate.getDeliveryProcesses().get(0))),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addDpTourNotCalculated(tourAfterCalculate, tourAfterCalculate.getDeliveryProcesses().get(0))),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addDpTourNotCalculated(withActionPointList, withActionPointList.getDeliveryProcesses().get(0)))
            );
        }

        @Test
        void correctUsage() {
            tourService.addDpTourNotCalculated(tourBeforeCalculate, tourBeforeCalculate.getDeliveryProcesses().get(0));
            assertEquals(2, tourBeforeCalculate.getDeliveryProcesses().size(),
                    "addDpTourNotCalculated should add the given deliveryProcess");
        }
    }

    @Nested
    class changeDeliveryOrder {
        @Test
        void nullParameter() {
            List<ActionPoint> newOrder = new ArrayList<>();
            newOrder.add(base);
            newOrder.add(actionPoint1);
            newOrder.add(actionPoint);
            newOrder.add(end);
            List<ActionPoint> nullElement = new ArrayList<>();
            nullElement.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(null, tourAfterCalculate, newOrder)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(graph1, null, newOrder)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(graph1, tourBeforeCalculate, newOrder)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(graph1, tourAfterCalculate, null)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(graph1, tourAfterCalculate, new ArrayList<>())),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changeDeliveryOrder(graph1, tourAfterCalculate, nullElement))
            );
        }

        @Test
        void incompatibleParameter() {
            List<ActionPoint> newOrder = new ArrayList<>();
            newOrder.add(base);
            points = xmlToGraph.getGraphFromXml("resource/grandPlan.xml");
            graph = new Graph(points);
            tourBeforeCalculate = xmlToGraph.getDeliveriesFromXml("resource/demandeGrand7.xml");
            Tour grandTour = graphService.calculateTour(tourBeforeCalculate, graph);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.changeDeliveryOrder(graph1, tourAfterCalculate, newOrder)),
                    () -> assertThrows(IllegalArgumentException.class, () -> tourService.changeDeliveryOrder(graph1, grandTour, grandTour.getActionPoints()))
            );
        }

        @Test
        void correctUsage() {
            List<ActionPoint> newOrder = new ArrayList<>();
            newOrder.add(base);
            newOrder.add(actionPoint1);
            newOrder.add(actionPoint);
            newOrder.add(end);
            Tour res = tourService.changeDeliveryOrder(graph1, tourAfterCalculate, newOrder);
            assertAll(
                    () -> assertEquals(base, res.getActionPoints().get(0),
                            "changeDeliveryOrder should return a tour with right actionPoint at index 0"),
                    () -> assertEquals(actionPoint1, res.getActionPoints().get(1),
                            "changeDeliveryOrder should return a tour with right actionPoint at index 1"),
                    () -> assertEquals(actionPoint, res.getActionPoints().get(2),
                            "changeDeliveryOrder should return a tour with right actionPoint at index 2"),
                    () -> assertEquals(end, res.getActionPoints().get(3),
                            "changeDeliveryOrder should return a tour with right actionPoint at index 3")
            );
        }
    }

    @Nested
    class changePointPosition {
        @Test
        void nullParameter() {
            Point newPoint = new Point(1, 10, 10);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changePointPosition(null, tourAfterCalculate, actionPoint, newPoint)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changePointPosition(graph1, null, actionPoint, newPoint)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changePointPosition(graph1, tourAfterCalculate, actionPoint, newPoint)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changePointPosition(graph1, tourAfterCalculate, null, newPoint)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.changePointPosition(graph1, tourAfterCalculate, actionPoint, null))
            );
        }

        @Test
        void incompatibleParameter() {
            Point newPoint = new Point(1, 10, 10);
            assertThrows(IllegalArgumentException.class, () -> tourService.changePointPosition(graph1, tourAfterCalculate, notInTour, newPoint));
        }

        @Test
        void correctUsage() {
            ActionPoint oldPoint = tourAfterCalculate.getDeliveryProcesses().get(0).getPickUP();
            ActionPoint oldPoint1 = tourAfterCalculate.getDeliveryProcesses().get(0).getDelivery();
            Point newPoint = XmlToGraph.getPointById(479185309);
            Point newPoint1 = XmlToGraph.getPointById(495424862);
            Tour res = tourService.changePointPosition(graph1, tourAfterCalculate, oldPoint, newPoint);
            Tour finalRes = tourService.changePointPosition(graph1, res, oldPoint1, newPoint1);
            assertAll(
                    () -> assertEquals(newPoint, finalRes.getDeliveryProcesses().get(0).getPickUP().getLocation(),
                            "changePointPosition should return a tour with right deliveryProcess at index 0"),
                    () -> assertEquals(newPoint1, finalRes.getDeliveryProcesses().get(0).getDelivery().getLocation(),
                            "changePointPosition should return a tour with right deliveryProcess at index 0"),
                    () -> assertEquals(base, finalRes.getActionPoints().get(0),
                            "changePointPosition should return a tour with right actionPoint at index 0"),
                    () -> assertEquals(newPoint, finalRes.getActionPoints().get(1).getLocation(),
                            "changePointPosition should return a tour with right actionPoint at index 1"),
                    () -> assertEquals(newPoint1, finalRes.getActionPoints().get(2).getLocation(),
                            "changePointPosition should return a tour with right actionPoint at index 2"),
                    () -> assertEquals(end, finalRes.getActionPoints().get(3),
                            "changePointPosition should return a tour with right actionPoint at index 3")
            );
        }
    }

    @Nested
    class addNewDeliveryProcess {
        @Test
        void nullParameter() {
            ActionPoint actionPoint2 = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(479185309), ActionType.PICK_UP);
            ActionPoint actionPoint3 = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(495424862), ActionType.DELIVERY);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addNewDeliveryProcess(null, tourAfterCalculate, actionPoint2, actionPoint3)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addNewDeliveryProcess(graph1, null, actionPoint2, actionPoint3)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addNewDeliveryProcess(graph1, tourAfterCalculate, null, actionPoint3)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addNewDeliveryProcess(graph1, tourAfterCalculate, actionPoint2, null)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.addNewDeliveryProcess(graph, tourBeforeCalculate, actionPoint2, actionPoint3))
            );
        }

        @Test
        void correctUsage() {
            ActionPoint actionPoint2 = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(479185309), ActionType.PICK_UP);
            ActionPoint actionPoint3 = new ActionPoint(xmlToGraph.durationToTime(180), XmlToGraph.getPointById(495424862), ActionType.DELIVERY);
            Tour res = tourService.addNewDeliveryProcess(graph1, tourAfterCalculate, actionPoint2, actionPoint3);
            assertAll(
                    () -> assertEquals(actionPoint.getLocation(), res.getDeliveryProcesses().get(0).getPickUP().getLocation(),
                            "addNewDeliveryProcess should return a tour with right deliveryProcess at index 0"),
                    () -> assertEquals(actionPoint1.getLocation(), res.getDeliveryProcesses().get(0).getDelivery().getLocation(),
                            "addNewDeliveryProcess should return a tour with right deliveryProcess at index 0"),
                    () -> assertEquals(actionPoint2, res.getDeliveryProcesses().get(1).getPickUP(),
                            "addNewDeliveryProcess should return a tour with right deliveryProcess at index 1"),
                    () -> assertEquals(actionPoint3, res.getDeliveryProcesses().get(1).getDelivery(),
                            "addNewDeliveryProcess should return a tour with right deliveryProcess at index 1"),
                    () -> assertEquals(base.getLocation(), res.getActionPoints().get(0).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 0"),
                    () -> assertEquals(actionPoint.getLocation(), res.getActionPoints().get(1).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 1"),
                    () -> assertEquals(actionPoint1.getLocation(), res.getActionPoints().get(2).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 2"),
                    () -> assertEquals(actionPoint2.getLocation(), res.getActionPoints().get(3).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 3"),
                    () -> assertEquals(actionPoint3.getLocation(), res.getActionPoints().get(4).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 4"),
                    () -> assertEquals(end.getLocation(), res.getActionPoints().get(5).getLocation(),
                            "addNewDeliveryProcess should return a tour with right actionPoint at index 5"),
                    () -> assertEquals(5, res.getJourneyList().size(),
                            "addNewDeliveryProcess should return a tour with journey list adapted")
            );
        }
    }

    @Nested
    class deleteDeliveryProcess {
        @Test
        void nullParameter() {
            DeliveryProcess deliveryProcess = tourAfterCalculate.getDeliveryProcesses().get(0);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDeliveryProcess(null, tourAfterCalculate, deliveryProcess)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDeliveryProcess(graph1, null, deliveryProcess)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> tourService.deleteDeliveryProcess(graph1, tourAfterCalculate, null))
            );
        }

        @Test
        void incompatibleParameter() {
            points = xmlToGraph.getGraphFromXml("resource/grandPlan.xml");
            graph = new Graph(points);
            tourBeforeCalculate = xmlToGraph.getDeliveriesFromXml("resource/demandeGrand7.xml");
            Tour grandTour = graphService.calculateTour(tourBeforeCalculate, graph);
            assertThrows(IllegalArgumentException.class,
                    () -> tourService.deleteDeliveryProcess(graph1, grandTour, grandTour.getDeliveryProcesses().get(0)));
        }

        @Test
        void correctUsage() {
            DeliveryProcess deliveryProcess = tourAfterCalculate.getDeliveryProcesses().get(0);
            Tour res = tourService.deleteDeliveryProcess(graph1, tourAfterCalculate, deliveryProcess);
            assertAll(
                    () -> assertEquals(0, res.getDeliveryProcesses().size(),
                            "deleteDeliveryProcess should delete the selected deliveryProcess"),
                    () -> assertEquals(2, res.getActionPoints().size(),
                            "deleteDeliveryProcess should delete the selected deliveryProcess"),
                    () -> assertEquals(base, res.getActionPoints().get(0),
                            "deleteDeliveryProcess should delete the selected deliveryProcess"),
                    () -> assertEquals(end, res.getActionPoints().get(1),
                            "deleteDeliveryProcess should delete the selected deliveryProcess")
            );
        }
    }
}