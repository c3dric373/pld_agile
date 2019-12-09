package model.service;

import model.core.service.TourService;
import model.data.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static model.core.service.TourService.addNewDeliveryProcess;
import static org.junit.Assert.assertEquals;

public class TourServiceTest {


    private Time TEST_TIME = new Time(2234);
    private Point TEST_LOCATION = new Point(1, 0, 0);
    private Point TEST_LOCATION2 = new Point(2, 0, 0);
    private Point TEST_LOCATION3 = new Point(3, 0, 0);
    private Point TEST_LOCATION4 = new Point(4, 0, 0);

    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
    private ActionType TEST_ACTION_TYPE2 = ActionType.DELIVERY;
    private ActionPoint TEST_PICK_UP,TEST_PICK_UP2;
    private ActionPoint TEST_DELIVERY,TEST_DELIVERY2;
    private List<ActionPoint> TEST_ACTIONPOINT_LIST;
    private DeliveryProcess DELIVERY_PROCESS_TEST;
    private Tour TOUR_TEST;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        TEST_DELIVERY =
                new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE2);
        TEST_PICK_UP =
                new ActionPoint(TEST_TIME, TEST_LOCATION2, TEST_ACTION_TYPE);
        TEST_DELIVERY2  = new ActionPoint(TEST_TIME, TEST_LOCATION3,
                TEST_ACTION_TYPE2);
        TEST_PICK_UP2 = new ActionPoint(TEST_TIME, TEST_LOCATION4,
                TEST_ACTION_TYPE);
        TEST_ACTIONPOINT_LIST =
                new ArrayList<ActionPoint>();

    }

    @Test
    public void
    addNewDeliveryProcessTest_PickUpPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("pickUpPoint is null");

        // Act
        addNewDeliveryProcess(TEST_ACTIONPOINT_LIST,
                null,TEST_DELIVERY);

        // Assert via annotation
    }

    @Test
    public void
    addNewDeliveryProcessTest_DeliveryPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryPoint is null");

        // Act
        addNewDeliveryProcess(TEST_ACTIONPOINT_LIST,
                TEST_PICK_UP,null);

        // Assert via annotation
    }

    @Test
    public void addNewDeliveryProcessTest_validCall_validAnswer()
    {
        // Arrange
        final int indexActionPoint1 = 0;
        final int indexActionPoint2 = 1;
        final int indexActionPoint3 = 2;
        final int indexActionPoint4 = 3;
        List<ActionPoint> TEST_NEWACTIONPOINTS_LIST =
                new ArrayList<ActionPoint>();

        // Act
        TEST_NEWACTIONPOINTS_LIST = addNewDeliveryProcess(
                TEST_NEWACTIONPOINTS_LIST, TEST_PICK_UP,TEST_DELIVERY);
        TEST_NEWACTIONPOINTS_LIST = addNewDeliveryProcess(
                TEST_NEWACTIONPOINTS_LIST, TEST_PICK_UP2,TEST_DELIVERY2);


        // Assert via annotation
        assertEquals("1st index ok", indexActionPoint1,
                TEST_NEWACTIONPOINTS_LIST.indexOf(TEST_PICK_UP),0);
        assertEquals("2nd index ok", indexActionPoint2,
                TEST_NEWACTIONPOINTS_LIST.indexOf(TEST_DELIVERY),0);
        assertEquals("3rd index ok", indexActionPoint3,
                TEST_NEWACTIONPOINTS_LIST.indexOf(TEST_PICK_UP2),0);
        assertEquals("4th index ok", indexActionPoint4,
                TEST_NEWACTIONPOINTS_LIST.indexOf(TEST_DELIVERY2),0);
    }

    @Test
    public void deleteDeliveryProcess_DeliveryProcessIsNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryProcess is null");
        List<DeliveryProcess> deliveryProcesses = new ArrayList<DeliveryProcess>();
        deliveryProcesses.add(DELIVERY_PROCESS_TEST);
        TOUR_TEST = new Tour(deliveryProcesses, TEST_LOCATION, TEST_TIME);

        // Act
        TourService.deleteDeliveryProcess(TOUR_TEST, null);

        // Assert via annotation
    }

    @Test
    public void deleteDeliveryProcess_TourIsNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("tour is null");
        ActionPoint pickupPoint = new ActionPoint(TEST_TIME, TEST_LOCATION, ActionType.PICK_UP );
        ActionPoint deliveryPoint = new ActionPoint(TEST_TIME, TEST_LOCATION2, ActionType.DELIVERY );
        DELIVERY_PROCESS_TEST = new DeliveryProcess(
                deliveryPoint, pickupPoint);

        // Act
        TourService.deleteDeliveryProcess(null, DELIVERY_PROCESS_TEST);

        // Assert via annotation
    }

    @Test
    public void deleteDeliveryProcess_GoodResults(){
        // Arrange
        List<DeliveryProcess> deliveryProcesses = new ArrayList<DeliveryProcess>();
        Point p1 = new Point(100, 10.0, 12.0);
        Point p2 = new Point(101, 1.0, 2.0);
        Point p3 = new Point(110, 15.0, 82.0);
        Point p4 = new Point(111, 13.0, 4.0);
        ActionPoint pickUp1 = new ActionPoint(TEST_TIME, p1, ActionType.PICK_UP);
        ActionPoint pickUp2 = new ActionPoint(TEST_TIME, p2, ActionType.PICK_UP);
        ActionPoint delivery1 = new ActionPoint(TEST_TIME, p3, ActionType.DELIVERY);
        ActionPoint delivery2 = new ActionPoint(TEST_TIME, p4, ActionType.DELIVERY);
        DeliveryProcess deliveryProcess1 = new DeliveryProcess(pickUp1, delivery1);
        DeliveryProcess deliveryProcess2 = new DeliveryProcess(pickUp2, delivery2);
        deliveryProcesses.add(deliveryProcess1);
        deliveryProcesses.add(deliveryProcess2);
        List<ActionPoint> actionpointList = new ArrayList<ActionPoint>();
        actionpointList.add(pickUp1);
        actionpointList.add(pickUp2);
        actionpointList.add(delivery1);
        actionpointList.add(delivery2);
        Tour tour = new Tour(deliveryProcesses, TEST_LOCATION, TEST_TIME);
        tour.setActionPoints(actionpointList);

        // Act
        Tour newTour = TourService.deleteDeliveryProcess(tour, deliveryProcess1);
        List<DeliveryProcess> deliveryProcessesList = newTour.getDeliveryProcesses();
        int listSize = deliveryProcessesList.size();
        DeliveryProcess deliProcess = deliveryProcessesList.get(0);

        // Assert via annotation
        Assert.assertEquals(listSize, 1);
        Assert.assertEquals(deliProcess, deliveryProcess2);
    }

    @Test
    public void deleteDeliveryProcess_NoActionPoints(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryProcess doesn't exist in the tour");
        List<DeliveryProcess> deliveryProcesses = new ArrayList<DeliveryProcess>();
        Point p1 = new Point(100, 10.0, 12.0);
        Point p2 = new Point(101, 1.0, 2.0);
        Point p3 = new Point(110, 15.0, 82.0);
        Point p4 = new Point(111, 13.0, 4.0);
        ActionPoint pickUp1 = new ActionPoint(TEST_TIME, p1, ActionType.PICK_UP);
        ActionPoint pickUp2 = new ActionPoint(TEST_TIME, p2, ActionType.PICK_UP);
        ActionPoint delivery1 = new ActionPoint(TEST_TIME, p3, ActionType.DELIVERY);
        ActionPoint delivery2 = new ActionPoint(TEST_TIME, p4, ActionType.DELIVERY);
        DeliveryProcess deliveryProcess1 = new DeliveryProcess(pickUp1, delivery1);
        DeliveryProcess deliveryProcess2 = new DeliveryProcess(pickUp2, delivery2);
        deliveryProcesses.add(deliveryProcess1);
        deliveryProcesses.add(deliveryProcess2);
        Tour tour = new Tour(deliveryProcesses, TEST_LOCATION, TEST_TIME);

        // Act
        Tour newTour = TourService.deleteDeliveryProcess(tour, deliveryProcess1);

        // Assert via annotation
    }

    @Test
    public void deleteDeliveryProcess_DeliveryProcessDoesntExist(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryProcess doesn't exist in the tour");
        List<DeliveryProcess> deliveryProcesses = new ArrayList<DeliveryProcess>();
        Point p1 = new Point(100, 10.0, 12.0);
        Point p2 = new Point(101, 1.0, 2.0);
        Point p3 = new Point(110, 15.0, 82.0);
        Point p4 = new Point(111, 13.0, 4.0);
        Point p5 = new Point(112, 13.9, 8.0);
        ActionPoint pickUp1 = new ActionPoint(TEST_TIME, p1, ActionType.PICK_UP);
        ActionPoint pickUp2 = new ActionPoint(TEST_TIME, p2, ActionType.PICK_UP);
        ActionPoint delivery1 = new ActionPoint(TEST_TIME, p3, ActionType.DELIVERY);
        ActionPoint delivery2 = new ActionPoint(TEST_TIME, p4, ActionType.DELIVERY);
        ActionPoint delivery3 = new ActionPoint(TEST_TIME, p5, ActionType.DELIVERY);
        DeliveryProcess deliveryProcess1 = new DeliveryProcess(pickUp1, delivery1);
        DeliveryProcess deliveryProcess2 = new DeliveryProcess(pickUp2, delivery2);
        DeliveryProcess deliveryProcess3 = new DeliveryProcess(pickUp2, delivery3);
        deliveryProcesses.add(deliveryProcess1);
        deliveryProcesses.add(deliveryProcess2);
        Tour tour = new Tour(deliveryProcesses, TEST_LOCATION, TEST_TIME);

        // Act
        Tour newTour = TourService.deleteDeliveryProcess(tour, deliveryProcess3);

        // Assert via annotation
    }
}
