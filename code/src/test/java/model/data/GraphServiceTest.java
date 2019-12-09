package model.data;

import model.core.service.GraphService;
import model.io.XmlToGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GraphServiceTest {

    private XmlToGraph xmlToGraph = new XmlToGraph();
    private GraphService graphService = new GraphService();
    private List<Point> points = new ArrayList<Point>();
    private Graph TEST_GRAPH = new Graph(points);
    private List<DeliveryProcess> deliveryProcess = new ArrayList<DeliveryProcess>();
    private Point point = new Point(123486, 12.8, 2.0);
    private Point point2 = new Point(123487, 15, 6.0);
    private Point point3 = new Point(123488, 10, 4.0);
    private Point point4 = new Point(123489, 11, 5.0);
    private Time time = Time.valueOf("0:0:0");
    private Tour TEST_TOUR = new Tour(deliveryProcess, point, time);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCalculateTour_TourIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("tour can't be null");

        // Act
        Tour tourRes = graphService.calculateTour( null, TEST_GRAPH);

        // Assert via annotation

    }

    @Test
    public void testCalculateTour_GraphIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("graph can't be null");

        // Act
        Tour tourRes = graphService.calculateTour( TEST_TOUR, null);

        // Assert via annotation

    }

    @Test
    public void testCalculateTour_GoodResults() {

        // Arrange

        // Act

        List<Point> points = xmlToGraph.getGraphFromXml("resource/petitPlan.xml");
        Tour tour = xmlToGraph.getDeliveriesFromXml("resource/demandePetit1.xml");
        Graph graph =new Graph(points);
        Tour tourRes = graphService.calculateTour( tour, graph);
        List<Journey> journeyList = tourRes.getJourneyList();

        // Assert via annotation
        assertEquals(journeyList.get(0).getFinishTime(), Time.valueOf("08:04:25"));
        assertEquals(journeyList.get(0).getArrivePoint().getId(),208769039l);
        assertEquals(journeyList.get(0).getStartPoint().getId(),342873658l );
    }

    @Test
    public void testAddGraphCenter_GraphIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("graph can't be null");

        // Act
        Point point = graphService.addGraphCenter(null);

        // Assert via annotation
    }

    @Test
    public void testAddGraphCenter_GoodResult() {

        // Arrange
        points.add(point);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        Graph testGraph = new Graph(points);
        // Act
        Point point = graphService.addGraphCenter(testGraph);

        // Assert via annotation
        assertEquals(point.getId(), 1);
        assertEquals(point.getLatitude(),12.5,0);
        assertEquals(point.getLongitude(),4 ,0);
    }
}
