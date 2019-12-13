package model.service;

import controller.service.JourneyService;
import model.genData.ActionPoint;
import model.genData.ActionType;
import model.genData.Journey;
import model.genData.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class JourneyServiceTest {

    private List<Journey> JOURNEY_LIST = new ArrayList<Journey>();
    private List<Point> pointList = new ArrayList<Point>();
    private Point point1 = new Point (10, 20.0, 12.0);
    private Point point2 = new Point (110, 20.0, 5.0);
    private Point point3 = new Point (1110, 20.75, 14.0);
    private Journey journey;
    private List<ActionPoint> ACTION_POINT_LIST = new ArrayList<ActionPoint>();
    private ActionPoint actionPoint1 = new ActionPoint(Time.valueOf("0:2:0"),
            point1, ActionType.DELIVERY);
    private ActionPoint actionPoint2 = new ActionPoint(Time.valueOf("0:3:0"),
            point2, ActionType.PICK_UP);
    private ActionPoint actionPointNoTime = new ActionPoint(Time.valueOf("0:0:0"),
            point3, ActionType.PICK_UP);
    private Time TIME_TEST = Time.valueOf("0:0:0");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        pointList.add(point2);
        pointList.add(point1);
        journey = new Journey(pointList, 15.0);
        JOURNEY_LIST.add(journey);

        ACTION_POINT_LIST.add(actionPoint1);
        ACTION_POINT_LIST.add(actionPoint2);
        ACTION_POINT_LIST.add(actionPointNoTime);
    }

    @Test
    public void testCalculateTime_JourneysIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("journeys is null");

        // Act
        List<Journey> journeys = JourneyService.calculateTime(null, ACTION_POINT_LIST, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_JourneyIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("journeys of the List can't be null");
        List <Journey> journeys = new ArrayList<>();
        Journey journey = null;
        journeys.add(journey);

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(journeys, ACTION_POINT_LIST, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_JourneyIsEmpty_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("journeys can't be empty");
        List <Journey> journeys = new ArrayList<>();

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(journeys, ACTION_POINT_LIST, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_ActionPointsListIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("actionPoints is null");
        JOURNEY_LIST.add(journey);

        // Act
        List<Journey> journeys = JourneyService.calculateTime(JOURNEY_LIST, null, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_ActionPointIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("actionPoints of the List can't be null");
        List <ActionPoint> actionPoints = new ArrayList<>();
        ActionPoint actionPoint = null;
        actionPoints.add(actionPoint);

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(JOURNEY_LIST, actionPoints, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_ActionPointsListsIsEmpty_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("actionPoints can't be empty");
        List <ActionPoint> actionPoints = new ArrayList<>();

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(JOURNEY_LIST, actionPoints, TIME_TEST);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_TimeIsNull_throwsIllegalArgumentException() {

        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("startTime can't be null");

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(JOURNEY_LIST, ACTION_POINT_LIST, null);

        // Assert via annotation

    }

    @Test
    public void testCalculateTime_CorrectResultsNoActionTime_throwsIllegalArgumentException() {

        // Arrange
        List<Point> points = new ArrayList<>();
        points.add(point3);
        points.add(point1);
        //set length to 15 m to have a time of 3.6 sec
        Journey journeyNoTime = new Journey(points, 15.0);
        List<Journey> journeyListNoTime = new ArrayList<>();
        journeyListNoTime.add(journeyNoTime);

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(journeyListNoTime, ACTION_POINT_LIST, TIME_TEST);
        Journey journeyCalculated = journeysList.get(0);

        // Assert via annotation
        Assert.assertEquals(Time.valueOf("0:0:03"), journeyCalculated.getFinishTime());
    }

    @Test
    public void testCalculateTime_CorrectResultsNoLength_throwsIllegalArgumentException() {

        // Arrange
        //set length to 2 m to have a time of 0 sec
        Journey journeyNoTime = new Journey(pointList, 2.0);
        List<Journey> journeyListNoTime = new ArrayList<>();
        journeyListNoTime.add(journeyNoTime);

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(journeyListNoTime, ACTION_POINT_LIST, TIME_TEST);
        Journey journeyCalculated = journeysList.get(0);

        // Assert via annotation
        Assert.assertEquals(actionPoint2.getTime(), journeyCalculated.getFinishTime());
    }

    @Test
    public void testCalculateTime_CorrectResults_throwsIllegalArgumentException() {

        // Arrange

        // Act
        List<Journey> journeysList = JourneyService.calculateTime(JOURNEY_LIST,
                ACTION_POINT_LIST, TIME_TEST);
        Journey journeyCalculated = journeysList.get(0);

        // Assert via annotation
        Assert.assertEquals(Time.valueOf("0:03:03"), journeyCalculated.getFinishTime());
    }

}
