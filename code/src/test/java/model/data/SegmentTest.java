package model.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for Segment
 */
public class SegmentTest {

    private int NEGATIVE_LENGTH = -1;
    private String EMPTY_STRING = "";
    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Test
    public void testCTOR_lengthNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("length is negative");

        // Act
        new Segment();

        // Assert via annotation
    }

    @Test
    public void testCTOR_StingNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name is null");

        // Act
        new Segment();

        // Assert via annotation
    }

    @Test
    public void testCTOR_StingEmpty_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name is empty");

        // Act
        new Segment();

        // Assert via annotation
    }







}
