package model.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ComputingTest {

    private final int PREV_TEST = 0;
    private final double DIST_TEST = 10;
    private final int PREV_NEG = -1;
    private final double DIST_NEG = -1;
    private final Computing.tuple TUPLE_TEST = new Computing.tuple(PREV_TEST,DIST_TEST);


    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testCTOR_prevNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("prev is negative");

        // Act
        new Computing.tuple(PREV_NEG,DIST_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_distNegative_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dist is negative");

        // Act
        new Computing.tuple(PREV_TEST,DIST_NEG);

        // Assert via annotation
    }

    @Test
    public void testCTOR_tupleCorrect_throwsIllegalArgumentException() {

        // Arrange

        // Act
        new Computing.tuple(PREV_TEST,DIST_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_getterPrev_throwsIllegalArgumentException() {

        // Arrange

        // Act
        TUPLE_TEST.getPrev();

        // Assert via annotation
    }

    @Test
    public void testCTOR_getterDist_throwsIllegalArgumentException() {

        // Arrange

        // Act
        TUPLE_TEST.getDist();

        // Assert via annotation
    }

    @Test
    public void testCTOR_setterPrev_throwsIllegalArgumentException() {

        // Arrange

        // Act
        TUPLE_TEST.setPrev(PREV_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_setterPrevNeg_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("prev is negative");

        // Act
        TUPLE_TEST.setPrev(PREV_NEG);

        // Assert via annotation
    }

    @Test
    public void testCTOR_setterDist_throwsIllegalArgumentException() {

        // Arrange

        // Act
        TUPLE_TEST.setDist(DIST_TEST);

        // Assert via annotation
    }

    @Test
    public void testCTOR_setterDistNeg_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dist is negative");

        // Act
        TUPLE_TEST.setDist(DIST_NEG);

        // Assert via annotation
    }

}
