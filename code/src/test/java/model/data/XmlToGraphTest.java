package model.data;

import model.io.XmlToGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class XmlToGraphTest {
    private String wrongFile = "dddddddddd";
    XmlToGraph reader = new XmlToGraph();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
}
