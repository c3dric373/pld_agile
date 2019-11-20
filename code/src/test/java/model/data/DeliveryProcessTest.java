package model.data;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;


public class DeliveryProcessTest {
    private ActionPoint TEST_PICK_UP = new ActionPoint();
    private ActionPoint TEST_DELIVERY = new ActionPoint();
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
        Point TEST_LOCATION = new Point(5,3,3);
        int TEST_TIME = 10;
        ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
        ActionPoint TEST_PICKUP_IS_DELIVERY = new ActionPoint(TEST_LOCATION, TEST_TIME, TEST_ACTION_TYPE);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("delivery is pickup");

        // Act
        new DeliveryProcess(TEST_PICKUP_IS_DELIVERY, TEST_PICKUP_IS_DELIVERY);

        // Assert via annotation
    }

}
