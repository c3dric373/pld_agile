package model.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestPoint {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private int TEST_ID = 78787;
    private double TEST_LONGITUDE = 45.75406;
    private double TEST_LATITUDE = 4.857418;


    Point(final int id, final float latitude, final float longitude) {
        this.id =id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.list_segments = new ArrayList<Segment>();
    }




    @Test
    public void testCTOR_idNegative_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_ID = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id is negative");

        // Act
        new Point(NEGATIVE_ID, TEST_LATITUDE, TEST_LONGITUDE);

        // Assert via annotation
    }

    @Test
    public void testCTOR_latitudeNegative_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_LATITUDE = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("latitude is negative");

        // Act
        new Point(TEST_ID, NEGATIVE_LATITUDE, TEST_LONGITUDE);

        // Assert via annotation
    }

    @Test
    public void testCTOR_longitudeNegative_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_LONGITUDE = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is negative");

        // Act
        new Point(TEST_ID, TEST_LATITUDE, NEGATIVE_LONGITUDE);

        // Assert via annotation
    }


    @Test
    public void testCTOR_longitudeTooBig_throwsIllegalArgumentException() {

        // Arrange
        int NEGATIVE_LONGITUDE = -1;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("longitude is negative");

        // Act
        new Point(TEST_ID, TEST_LATITUDE, NEGATIVE_LONGITUDE);

        // Assert via annotation
    }



}
