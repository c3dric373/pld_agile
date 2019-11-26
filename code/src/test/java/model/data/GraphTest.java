package model.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;


public class GraphTest {

    private static Graph test_Graph;

    @Before
    public void setUp() {

        test_Graph = new Graph(new ArrayList<Point>());
    }

    @Test
    public void testCTOR_validInput_correctNbPoints(){
        // Arrange
        final int CORRECT_NB_POINTS = 0;

        // Act
        final int nb_points = test_Graph.getNb_points();

        //Assert
        assertEquals("wrong nb_Points",CORRECT_NB_POINTS,nb_points);

    }


    @Test
    public void testCTOR_validInput_correctList_Points(){
        // Arrange
        final boolean isEmpty = true;
        final Map CORRECT_MAP = new HashMap<Integer, Integer>();

        // Act
        final boolean test_empty = test_Graph.getPoints().isEmpty();

        //Assert
        assertEquals("list_points not empty",isEmpty,test_empty);

    }

    @Test
    public void testCTOR_validInput_correctMap(){
        // Arrange
        final boolean isEmpty = true;
        final Map CORRECT_MAP = new HashMap<Long, Integer>();

        // Act
        final boolean test_empty = test_Graph.getMap().isEmpty();

        //Assert
        assertEquals("map not empty",isEmpty,test_empty);

    }





}
