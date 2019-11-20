package model.data;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;


public class ActionPointTest {
    private int TEST_TIME = 0;
    private Point TEST_LOCATION = new Point(5,3,3);
    private ActionType TEST_ACTION_TYPE = new ActionType();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_timeNegative_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_TIME = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("time is negative");

        // Act
        new ActionPoint(NEGATIVE_TIME, TEST_LOCATION, TEST_ACTION_TYPE);

        // Assert via annotation
    }


    @Test
    public void testCTOR_timeToGreat_throwsIllegalArgumentException() {

        // Arrange
        int BIG_TIME =2400;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("time is too great");

        // Act
        new ActionPoint(BIG_TIME, TEST_LOCATION, TEST_ACTION_TYPE);

        // Assert via annotation
    }

    @Test
    public void testCTOR_locationNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("location is null");

        // Act
        new ActionPoint(TEST_TIME, null, TEST_ACTION_TYPE);

        // Assert via annotation
    }

    @Test
    public void testCTOR_actionTypeNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("actionType is null");

        // Act
        new ActionPoint(TEST_TIME, TEST_LOCATION, null);

        // Assert via annotation
    }

}
