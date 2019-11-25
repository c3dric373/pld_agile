package model.data;

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
    private String wrongFile = "dddddddddd";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    XmlToGraph reader = new XmlToGraph();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setStream() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void test_fileNameNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("fileName is null");

        // Act
        reader.getGraphFromXml(null);

        // Assert via annotation
    }

    @Test
    public void test_fileNameEmpty_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("fileName is empty");

        // Act
        reader.getGraphFromXml("");

        // Assert via annotation
    }

    @Test
    public void test_fileNameNotExist_throwsFileNotFoundException() {

        // Arrange

        // Act
        reader.getGraphFromXml(wrongFile);

        // Assert via annotation
    }

    @Test
    public void test_CorrectNumberOfResultsPetitPlan() {

        // Arrange

        // Act
        ArrayList<Point> result = reader.getGraphFromXml("petitPlan.xml");

        // Assert via annotation
        assertThat(outContent.toString(), containsString("nbNodes :308"));
        assertThat(outContent.toString(), containsString("nbRoad :616"));
    }

    @Test
    public void test_CorrectDataPetitPlan() {

        // Arrange

        // Act
        ArrayList<Point> result = reader.getGraphFromXml("petitPlan.xml");

        // Assert via annotation
        assertEquals(25175791l, result.get(0).getId());
        assertEquals(45.75406, result.get(0).getLatitude(),0);
        assertEquals(4.857418, result.get(0).getLongitude(),0);
    }
}
