package model.io;

import model.data.Point;
import model.data.Tour;
import model.io.XmlToGraph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class XmlToGraphTest {
    private String WRONG_FILE = "dddddddddd";
    private long WRONG_POINT = 123l;
    private long EXISTING_POINT = 2129259178l;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    XmlToGraph reader = new XmlToGraph();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setStream() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void test_filePathNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("path is null");

        // Act
        reader.getGraphFromXml(null);

        // Assert via annotation
    }

    @Test
    public void test_filePathEmpty_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("path is empty");

        // Act
        reader.getGraphFromXml("");

        // Assert via annotation
    }

    @Test
    public void test_fileNameNotExist_throwsFileNotFoundException() {

        // Arrange

        // Act
        reader.getGraphFromXml(WRONG_FILE);

        // Assert via annotation
    }

    @Test
    public void test_CorrectNumberOfResultsPetitPlan() {

        // Arrange

        // Act
        ArrayList<Point> result = reader.getGraphFromXml("resource/petitPlan.xml");

        // Assert via annotation
        assertThat(outContent.toString(), containsString("nbNodes :308"));
        assertThat(outContent.toString(), containsString("nbRoad :616"));
    }

    @Test
    public void test_CorrectDataPetitPlan() {

        // Arrange

        // Act
        ArrayList<Point> result = reader.getGraphFromXml("resource/petitPlan.xml");

        // Assert via annotation
        assertEquals(25175791l, result.get(0).getId());
        assertEquals(45.75406, result.get(0).getLatitude(), 0);
        assertEquals(4.857418, result.get(0).getLongitude(), 0);
    }

    @Test
    public void test_filePathNullDeliveries_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("path is null");

        // Act
        reader.getDeliveriesFromXml(null);

        // Assert via annotation
    }

    @Test
    public void test_filePathEmptyDeliveries_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("path is empty");

        // Act
        reader.getDeliveriesFromXml("");

        // Assert via annotation
    }

    @Test
    public void test_PointDoesntExist_GetPoint_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Point doesn't exist");

        // Act
        ArrayList<Point> noeud = reader.getGraphFromXml("resource/moyenPlan.xml");
        Point p = reader.getPointById(WRONG_POINT);

        // Assert via annotation
    }

    @Test
    public void test_PointExist_GetPoint_throwsIllegalArgumentException() {

        // Arrange

        // Act
        ArrayList<Point> noeud = reader.getGraphFromXml("resource/moyenPlan.xml");
        Point p = reader.getPointById(EXISTING_POINT);

        // Assert via annotation
        assertEquals(p.getId(), EXISTING_POINT);
        assertEquals(p.getLatitude(), 45.750404, 0);
        assertEquals(p.getLongitude(), 4.8744674, 0);
    }
}
