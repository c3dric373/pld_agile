package model.service;

import model.data.ActionPoint;
import model.data.ActionType;
import model.data.DeliveryProcess;
import model.data.Point;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static model.core.service.DeliveryProcessService.addNewDeliveryProcess;
import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProcessServiceTest {


    private Time TEST_TIME = new Time(2234);
    private Point TEST_LOCATION = new Point(1, 0, 0);
    private Point TEST_LOCATION2 = new Point(2, 0, 0);
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
    private ActionType TEST_ACTION_TYPE2 = ActionType.DELIVERY;
    private ActionPoint TEST_PICK_UP,TEST_PICK_UP2;
    private ActionPoint TEST_DELIVERY,TEST_DELIVERY2;
    private List<DeliveryProcess> TEST_DELIVERYPROCESS_LIST;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        TEST_DELIVERY =
                new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE2);
        TEST_PICK_UP =
                new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
        TEST_DELIVERY2 =
                new ActionPoint(TEST_TIME, TEST_LOCATION2, TEST_ACTION_TYPE2);
        TEST_PICK_UP2 =
                new ActionPoint(TEST_TIME, TEST_LOCATION2, TEST_ACTION_TYPE);
        TEST_DELIVERYPROCESS_LIST =
                new ArrayList<DeliveryProcess>();

    }

    @Test
    public void
    addNewDeliveryProcessTest_PickUpPointNull_throwsIllegalArgumentException(){
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("pickUpPoint is null");

        // Act
        addNewDeliveryProcess(TEST_DELIVERYPROCESS_LIST,
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
        addNewDeliveryProcess(TEST_DELIVERYPROCESS_LIST,
                TEST_PICK_UP,null);

        // Assert via annotation
    }

    @Test
    public void addNewDeliveryProcessTest_validCall_validAnswer()
    {
        // Arrange
        final DeliveryProcess TEST_DELIVERY_PROCESS =
                new DeliveryProcess(TEST_PICK_UP,TEST_DELIVERY);
        final DeliveryProcess TEST_DELIVERY_PROCESS2 =
                new DeliveryProcess(TEST_PICK_UP2,TEST_DELIVERY2);
        final int indexDeliveryProcess1 = 0;
        final int indexDeliveryProcess2 = 1;
        List<DeliveryProcess> TEST_NEWDELIVERYPROCESS_LIST =
                new ArrayList<DeliveryProcess>();

        // Act
        TEST_NEWDELIVERYPROCESS_LIST = addNewDeliveryProcess(
                TEST_NEWDELIVERYPROCESS_LIST, TEST_PICK_UP,TEST_DELIVERY);
        TEST_NEWDELIVERYPROCESS_LIST = addNewDeliveryProcess(
                TEST_NEWDELIVERYPROCESS_LIST, TEST_PICK_UP2,TEST_DELIVERY2);


        // Assert via annotation
        assertEquals("1st index ok", indexDeliveryProcess1,
                TEST_NEWDELIVERYPROCESS_LIST.indexOf(TEST_DELIVERY_PROCESS),0);
        assertEquals("2nd index ok", indexDeliveryProcess2,
                TEST_NEWDELIVERYPROCESS_LIST.indexOf(TEST_DELIVERY_PROCESS2),0);
    }
}
