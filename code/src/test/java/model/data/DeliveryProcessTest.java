package model.data;

import lombok.EqualsAndHashCode;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import java.util.*;


public class DeliveryProcessTest {
    private int TEST_TIME = 0;
    private Point TEST_LOCATION = new Point(1, 0, 0);
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;
    private ActionType TEST_ACTION_TYPE2 = ActionType.DELIVERY;
    private ActionPoint TEST_PICK_UP = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE );
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
    public void testCTOR_correctCall_validCall(){
        // Arrange

        // Act
        DeliveryProcess deliveryProcess = new DeliveryProcess(TEST_PICK_UP, TEST_DELIVERY);
        ActionPoint delivery =  deliveryProcess.getDelivery();
        ActionPoint pickUp = deliveryProcess.getPickUP();



        // Assert
        assertEquals(TEST_DELIVERY, delivery);
        assertEquals(TEST_PICK_UP, pickUp);
    }

    @Test
    public void testEqualsAndHashCode_allCases_noErrors() {

        // Arrange

        // Act
        EqualsVerifier.forClass(DeliveryProcess.class).suppress(Warning.STRICT_INHERITANCE).verify();

        // Assert
    }
}
