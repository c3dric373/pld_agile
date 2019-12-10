package model.service;

import model.data.*;
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
    private List<DeliveryProcess> TEST_DELIVERY_PROCESS_LIST;
    private Tour TEST_TOUR;

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
        TEST_ACTIONPOINT_LIST.add(TEST_PICK_UP);
        TEST_ACTIONPOINT_LIST.add(TEST_DELIVERY);
        TEST_DELIVERY_PROCESS_LIST = new ArrayList<DeliveryProcess>();
        TEST_DELIVERY_PROCESS_LIST.add(new DeliveryProcess(TEST_PICK_UP,
                TEST_DELIVERY));
        TEST_TOUR = new Tour(TEST_DELIVERY_PROCESS_LIST,TEST_LOCATION,
                TEST_TIME);
        TEST_TOUR.setActionPoints(TEST_ACTIONPOINT_LIST);

    }

    @Test
    public void
    addNewDeliveryProcessTest_PickUpPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("pickUpPoint is null");

        // Act
        addNewDeliveryProcess(TEST_TOUR,
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
        addNewDeliveryProcess(TEST_TOUR,
                TEST_PICK_UP,null);

        // Assert via annotation
    }

    @Test
    public void addNewDeliveryProcessTest_validCall_validAnswer()
    {
        // Arrange
        final int indexActionPoint3 = 2;
        final int indexActionPoint4 = 3;
        Tour NEW_TEST_TOUR;
        NEW_TEST_TOUR = new Tour(TEST_DELIVERY_PROCESS_LIST,TEST_LOCATION,
                TEST_TIME);
        NEW_TEST_TOUR.setActionPoints(TEST_ACTIONPOINT_LIST);

        // Act
        NEW_TEST_TOUR = addNewDeliveryProcess(
                NEW_TEST_TOUR, TEST_PICK_UP2,TEST_DELIVERY2);


        // Assert via annotation
        assertEquals("3rd index ok", indexActionPoint3,
                NEW_TEST_TOUR.getActionPoints().indexOf(TEST_PICK_UP2),0);
        assertEquals("4th index ok", indexActionPoint4,
                NEW_TEST_TOUR.getActionPoints().indexOf(TEST_DELIVERY2),0);
    }
}
