package model.data;

import model.genData.ActionPoint;
import model.genData.ActionType;
import model.genData.DeliveryProcess;
import model.genData.Point;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import view.DashBoardController;

import java.sql.Time;

import static org.junit.Assert.assertEquals;


public class DeliveryProcessTest {
    private Time TEST_TIME = new Time(2234);
    private Point TEST_LOCATION = new Point(1, 0, 0);
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
    private ActionType TEST_ACTION_TYPE2 = ActionType.DELIVERY;
    private ActionPoint TEST_PICK_UP = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
    private ActionPoint TEST_DELIVERY = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE2);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_pickUpNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("pickUp is null");

        // Act
        new DeliveryProcess(null, TEST_DELIVERY);

        // Assert via annotation

    }

    @Test
    public void test() {
        System.out.println(DashBoardController.class.getResource("map_style.txt").getFile());
    }

    @Test
    public void testCTOR_deliveryNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("delivery is null");

        // Act
        new DeliveryProcess(TEST_PICK_UP, null);

        // Assert via annotation
    }

    @Test
    public void testCTOR_pickupIsDelivery_throwsIllegalArgumentException() {

        // Arrange
        ActionPoint TEST_PICKUP_IS_DELIVERY = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("delivery is pickup");

        // Act
        new DeliveryProcess(TEST_PICKUP_IS_DELIVERY, TEST_PICKUP_IS_DELIVERY);

        // Assert via annotation
    }

    @Test
    public void testCTOR_correctCall_validCall() {
        // Arrange

        // Act
        DeliveryProcess deliveryProcess = new DeliveryProcess(TEST_PICK_UP, TEST_DELIVERY);
        ActionPoint delivery = deliveryProcess.getDelivery();
        ActionPoint pickUp = deliveryProcess.getPickUP();


        // Assert
        assertEquals(TEST_DELIVERY, delivery);
        assertEquals(TEST_PICK_UP, pickUp);
    }

}
