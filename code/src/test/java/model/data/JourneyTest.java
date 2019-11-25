package model.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for Journey
 */
public class JourneyTest {

    private final long ID_START_TEST = 25175778;
    private final long ID_ARRIVE_TEST = 25175791;
    private final List<Long> IDS_TEST = new ArrayList<>();
    private final double MIN_LENGTH_TEST = 100;
    private final long ID_START_NEG = -1;
    private final long ID_ARRIVE_NEG = -1;
    private final double MIN_LENGTH_NEG = -1;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_Id_startNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id_start is negative");

        // Act
        new Journey(ID_START_NEG,ID_ARRIVE_TEST,IDS_TEST,MIN_LENGTH_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_Id_arriveNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("id_arrive is negative");

        // Act
        new Journey(ID_START_TEST,ID_ARRIVE_NEG,IDS_TEST,MIN_LENGTH_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_IdsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ids is null");

        // Act
        new Journey(ID_START_TEST,ID_ARRIVE_TEST,null,MIN_LENGTH_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_Min_lengthNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("min_length is negative");

        // Act
        new Journey(ID_START_TEST,ID_ARRIVE_TEST,IDS_TEST,MIN_LENGTH_NEG);

        // Assert via annotation
    }

    @Test
    public void testCTOR_JourneyCorrect_throwsIllegalArgumentException() {

        // Arrange

        // Act
        new Journey(ID_START_TEST,ID_ARRIVE_TEST,IDS_TEST,MIN_LENGTH_TEST);

        // Assert via annotation
    }
}
