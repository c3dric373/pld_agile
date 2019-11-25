package model.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.List;

/**
 * Test for Point
 */
public class PointTest {

    private final int ID_NEG = -1;
    private final int ID_TEST = 20;
    private final double LAT_SMALL = -91;
    private final double LAT_BIG = 91;
    private final double LAT_TEST = 45;
    private final double LNG_SMALL = -181;
    private final double LNG_BIG = 181;
    private final double LNG_TEST = 45;
    private List<Segment> SEGMENTS_TEST;

    private Point TEST_POINT;

    @Before
    private void setUp() {
        SEGMENTS_TEST = new ArrayList<Segment>();
        SEGMENTS_TEST.add(new Segment(25175791, 25175778, 69.979805, "Rue Danton"));
        SEGMENTS_TEST.add(new Segment(25175791, 2117622723, 136.00636, "Rue de l'Abondance\""));
        TEST_POINT = new Point(25175791, 45.75406, 4.857418, SEGMENTS_TEST);


    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetLengthTo_correctInput_validCalculation() {

        // Arrange
        final double CORRECT_LENGTH = 69.979805;

        // Act
        final double test_length = TEST_POINT.getLengthTo(25175778);

        // Assert
        assertEquals("wrong length", CORRECT_LENGTH, test_length,0);

    }

    @Test
    public void testCTOR_idNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id is negative");

        // Act
        new Point(ID_NEG, LAT_TEST, LNG_TEST, SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too small");

        // Act
        new Point(ID_TEST, LAT_SMALL, LNG_TEST, SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too great");

        // Act
        new Point(ID_TEST, LAT_BIG, LNG_TEST, SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too small");

        // Act
        new Point(ID_TEST, LAT_TEST, LNG_SMALL, SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too great");

        // Act
        new Point(ID_TEST, LAT_TEST, LNG_BIG, SEGMENTS_TEST);

        // Assert via annotation
    }
}
