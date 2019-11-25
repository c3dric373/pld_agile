package model.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    //private List<Segment> SEGMENTS_TEST = new ArrayList<Segment>();



    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testCTOR_idNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id is negative");

        // Act
        new Point(ID_NEG,LAT_TEST,LNG_TEST);//,SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too small");

        // Act
        new Point(ID_TEST,LAT_SMALL,LNG_TEST);//,SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is too great");

        // Act
        new Point(ID_TEST,LAT_BIG,LNG_TEST);//,SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooSmall_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too small");

        // Act
        new Point(ID_TEST,LAT_TEST,LNG_SMALL);//,SEGMENTS_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeTooGreat_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is too great");

        // Act
        new Point(ID_TEST,LAT_TEST,LNG_BIG);//,SEGMENTS_TEST);

        // Assert via annotation
    }
}
