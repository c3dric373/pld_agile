package model.data;

import model.genData.DeliveryProcess;
import model.genData.Point;
import model.genData.Tour;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.*;


public class TourTest {
    private List<DeliveryProcess> TEST_DELIVERY_PROCESSES = new ArrayList<DeliveryProcess>();
    private Point TEST_BASE = new Point(5, 3, 3);
    private Time TEST_START_TIME = Time.valueOf("0:0:0");
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

    /**
     * @Test public void testCTOR_startTimeNegative_throwsIllegalArgumentException() {
     * <p>
     * // Arrange
     * int NEGATIVE_START_TIME = -1;
     * thrown.expect(IllegalArgumentException.class);
     * thrown.expectMessage("startTime is negative");
     * <p>
     * // Act
     * new Tour(TEST_DELIVERY_PROCESSES, TEST_BASE, NEGATIVE_START_TIME);
     * <p>
     * // Assert via annotation
     * }
     */
    @Test
    public void testCTOR_startTimeNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("startTime is null");

        // Act
        new Tour(TEST_DELIVERY_PROCESSES, TEST_BASE, null);

        // Assert via annotation
    }
/**
 @Test public void testCTOR_TourCorrect() {

 // Arrange

 // Act
 new Tour(TEST_DELIVERY_PROCESSES, TEST_BASE, TEST_START_TIME);

 // Assert via annotation
 }
 */


}
