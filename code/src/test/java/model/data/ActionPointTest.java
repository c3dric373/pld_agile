package model.data;

import model.genData.ActionPoint;
import model.genData.ActionType;
import model.genData.Point;
import model.genData.Segment;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.*;

import static org.junit.Assert.assertEquals;


public class ActionPointTest {
    private Time TEST_TIME = new Time(2243L);
    private List<Segment> TEST_SEGMENTS = new ArrayList<Segment>();
    private Point TEST_LOCATION;
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;

    private ActionPoint subject;

    @Before
    public void setUp() {
        TEST_SEGMENTS.add(new Segment(25175791, 25175778, 69.979805, "Rue Danton"));
        TEST_SEGMENTS.add(new Segment(25175791, 2117622723, 136.00636, "Rue de l'Abondance\""));
        TEST_LOCATION = new Point(1, 0, 3);
        subject = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();


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
    public void testCTOR_timeNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("time is null");

        // Act
        new ActionPoint(null, TEST_LOCATION, TEST_ACTION_TYPE);

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

    @Test
    public void testCTOR_correctCall_validCall() {
        // Arrange


        // Act
        final ActionType subjectActionType = subject.getActionType();
        final Point subjectLocation = subject.getLocation();
        final Time subjectTime = subject.getTime();


        // Assert
        assertEquals(TEST_TIME, subjectTime);
        assertEquals(TEST_LOCATION, subjectLocation);
        assertEquals(TEST_ACTION_TYPE, subjectActionType);

    }


}
