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
    private List<Segment> TEST_SEGMENTS;
    private long TEST_ID = 25175791L;
    private double TEST_LATITUDE = 45.75406;
    private double TEST_LONGITUDE = 4.857418;


    private Point subject;

    @Before
    public void setUp() {
        TEST_SEGMENTS = new ArrayList<Segment>();
        TEST_SEGMENTS.add(new Segment(25175791, 25175778, 69.979805, "Rue Danton"));
        TEST_SEGMENTS.add(new Segment(25175791, 2117622723, 136.00636, "Rue de l'Abondance\""));
        subject = new Point(TEST_ID, TEST_LATITUDE, TEST_LONGITUDE, TEST_SEGMENTS);


    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetLengthTo_correctInput_validCalculation() {

        // Arrange
        final double CORRECT_LENGTH = 69.979805;

        // Act
        final double test_length = subject.getLengthTo(25175778);

        // Assert
        assertEquals("wrong length", CORRECT_LENGTH, test_length, 0);

    }

    @Test
    public void testGetLengthTo_pointNotReachable_IllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("point not reachable via one segment");
        final long  wrong_id = 2365376415L;

        // Act
        final double test_length = subject.getLengthTo(wrong_id);

        // Assert -> via annotation

    }


    @Test
    public void testCTOR_idNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id is negative");

        // Act
        new Point(ID_NEG, LAT_TEST, LNG_TEST, TEST_SEGMENTS);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too small");

        // Act
        new Point(ID_TEST, LAT_SMALL, LNG_TEST, TEST_SEGMENTS);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too great");

        // Act
        new Point(ID_TEST, LAT_BIG, LNG_TEST, TEST_SEGMENTS);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too small");

        // Act
        new Point(ID_TEST, LAT_TEST, LNG_SMALL, TEST_SEGMENTS);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too great");

        // Act
        new Point(ID_TEST, LAT_TEST, LNG_BIG, TEST_SEGMENTS);

        // Assert via annotation
    }

    @Test
    public void testGetId_validCall_correctId() {

        // Arrange

        // Act
        final long id = subject.getId();

        // Assert
        assertEquals("Wrong id", id, TEST_ID);
    }
    @Test
    public void testGetLatitude_validCall_correctId() {

        // Arrange

        // Act
        final double latitude = subject.getLatitude();

        // Assert
        assertEquals("Wrong id", latitude, TEST_LATITUDE,0);
    }

    @Test
    public void testGetLongitude_validCall_correctId() {

        // Arrange

        // Act
        final double longitude = subject.getLongitude();

        // Assert
        assertEquals("Wrong id", longitude, TEST_LONGITUDE,0);
    }

    @Test
    public void testGetSegments_validCall_correctId() {

        // Arrange

        // Act
        final List segments = subject.getSegments();

        // Assert
        assertEquals("Wrong segments", segments, TEST_SEGMENTS);
    }


}
