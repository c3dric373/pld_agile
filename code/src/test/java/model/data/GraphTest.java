package model.data;

import model.genData.Graph;
import model.genData.Point;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphTest {

    private List<Point> points;
    private Graph test_Graph;
    private Map<Long, Integer> map;

    @Before
    public void setUp() {

        test_Graph = new Graph(points);


    }
/*
    @Test
    public void testCTOR_validInput_correctNbPoints() {
        // Arrange
        final int CORRECT_NB_POINTS = 0;

        // Act
        final int nb_points = test_Graph.getNbPoints();

        //Assert
        assertEquals("wrong nb_Points", CORRECT_NB_POINTS, nb_points);

    }

*/

    @Test
    public void testCTOR_validInput_correctList_Points() {
        // Arrange
        final boolean isEmpty = true;
        final List<Point> points  = new ArrayList<>();
         final int TEST_ID = 25175791;
         final double TEST_LATITUDE = 2.2;
         final double TEST_LONGITUDE = 48.1;
        points.add(new Point(TEST_ID, TEST_LATITUDE, TEST_LONGITUDE));

        // Act
        final Graph testGraph2 = new Graph(points);
        final List<Point> test_points = testGraph2.getPoints();

        //Assert
        assertEquals("points not equal set points", points,
                test_points);

    }



}