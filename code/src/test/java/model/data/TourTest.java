package model.data;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;


public class TourTest {
    private List<DeliveryProcess> TEST_DELIVERY_PROCESSES = new ArrayList<DeliveryProcess>();
    private Point TEST_BASE = new Point(5);
    private int TEST_START_TIME = 0;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_deliveryProcessesNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("deliveryProcess is null");

        // Act
        new Tour(null, TEST_BASE, TEST_START_TIME);

        // Assert via annotation
    }

    @Test
    public void testCTOR_pointNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("base is null");

        // Act
        new Tour(TEST_DELIVERY_PROCESSES, null, TEST_START_TIME);

        // Assert via annotation
    }

    @Test
    public void testCTOR_startTimeNegative_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_START_TIME = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("startTime is negative");

        // Act
        new Tour(TEST_DELIVERY_PROCESSES, TEST_BASE, NEGATIVE_START_TIME);

        // Assert via annotation
    }


    @Test
    public void testCTOR_startTimeToGreat_throwsIllegalArgumentException() {

        // Arrange
        int BIG_START_TIME =2400;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("startTime is too great");

        // Act
        new Tour(TEST_DELIVERY_PROCESSES, TEST_BASE, BIG_START_TIME);

        // Assert via annotation
    }



}
