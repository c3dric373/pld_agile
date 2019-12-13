package model.io;

import model.core.management.ApplicationManager;
import model.core.management.ApplicationManagerImpl;
import model.data.ErrorMessage;
import model.data.Point;
import model.data.Tour;
import model.io.XmlToGraph;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import view.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "com.sun.org.apache.xalan.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApplicationManagerImpl.class})
public class XmlToGraphTest {
    private String WRONG_FILE = "dddddddddd";
    private long WRONG_POINT = 123l;
    private long EXISTING_POINT = 2129259178l;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    XmlToGraph reader = new XmlToGraph();
    ApplicationManager model = new ApplicationManagerImpl();
    UserInterface view = new UserInterface(model);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setStream() {
        System.setOut(new PrintStream(outContent));
        model.setObserver(view);
    }

    @Test
    public void test_filePathNull_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        reader.getGraphFromXml(null);

        // Assert via annotation
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.PATH_NULL);
    }

    @Test
    public void test_filePathEmpty_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        reader.getGraphFromXml("");

        // Assert via annotation
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.PATH_EMPTY);
    }

    @Test
    public void test_fileNameNotExist_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        reader.getGraphFromXml(WRONG_FILE);

        // Assert via annotation
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.XML_LOAD_ERROR);
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
    public void test_filePathNullDeliveries_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        reader.getDeliveriesFromXml(null);

        // Assert via annotation
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.PATH_NULL);
    }

    @Test
    public void test_filePathEmptyDeliveries_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        reader.getDeliveriesFromXml("");

        // Assert via annotation*
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.PATH_EMPTY);
    }

    @Test
    public void test_PointDoesntExist_GetPoint_SendErrorMessage() {
        // Arrange
        PowerMockito.mockStatic(ApplicationManagerImpl.class);

        // Act
        ArrayList<Point> noeud = reader.getGraphFromXml("resource/moyenPlan.xml");
        Point p = reader.getPointById(WRONG_POINT);

        // Assert via annotation
        verifyStatic(ApplicationManagerImpl.class, times(1));
        ApplicationManagerImpl.sendMessage(ErrorMessage.POINT_DOESNT_EXIST);
    }

    @Test
    public void test_PointExist_GetPoint() {
        // Arrange

        // Act
        ArrayList<Point> noeud = reader.getGraphFromXml("resource/moyenPlan.xml");
        Point p = reader.getPointById(EXISTING_POINT);

        // Assert via annotation
        assertEquals(p.getId(), EXISTING_POINT);
        assertEquals(p.getLatitude(), 45.750404, 0);
        assertEquals(p.getLongitude(), 4.8744674, 0);
    }

    @Test
    public void test_PointExist_GetDeliveries() {
        // Arrange

        // Act
        ArrayList<Point> noeud = reader.getGraphFromXml("resource/moyenPlan.xml");
        Tour Deliver = reader.getDeliveriesFromXml("resource/demandeMoyen5.xml");

        // Assert via annotation
        Validate.notNull(Deliver);
    }
}
