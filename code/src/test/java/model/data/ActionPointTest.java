package model.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.*;


public class ActionPointTest {
    private int TEST_TIME = 0;
    private List<Segment> TEST_SEGMENTS = new ArrayList<Segment>();
    private Point TEST_LOCATION;
    private ActionType TEST_ACTION_TYPE = ActionType.PICK_UP;

    private ActionPoint subject;

    @Before
    public void setUp() {
        TEST_SEGMENTS.add(new Segment(25175791, 25175778, 69.979805, "Rue Danton"));
        TEST_SEGMENTS.add(new Segment(25175791, 2117622723, 136.00636, "Rue de l'Abondance\""));
        TEST_LOCATION = new Point(1,0,3);
        subject = new ActionPoint(TEST_TIME, TEST_LOCATION, TEST_ACTION_TYPE);
    }

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

    @Test
    public void testEqualsAndHashCode_allCases_noErrors() {

        // Arrange

        // Act
        EqualsVerifier.forClass(DeliveryProcess.class).suppress(Warning.STRICT_INHERITANCE).verify();

        // Assert
    }

}
