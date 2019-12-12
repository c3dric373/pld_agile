package model.core.service;

import model.core.TSP;
import model.core.TSP3;
import model.data.*;
import model.io.XmlToGraph;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    private JourneyService journeyService;
    private Point point1;
    private Point point2;
    private Point point3;
    private List<Point> points;
    private ActionPoint actionPoint1;
    private ActionPoint actionPoint2;
    private ActionPoint actionPoint3;
    private List<ActionPoint> actionPoints;
    private Time time;
    private Journey journey;
    private List<Journey> journeys;

    @BeforeEach
    void setUp() {
        journeyService = new JourneyService();
        point1 = new Point(10, 20.0, 12.0);
        point2 = new Point(110, 20.0, 5.0);
        point3 = new Point(1110, 20.75, 14.0);
        points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        actionPoint1 = new ActionPoint(Time.valueOf("0:2:0"), point1, ActionType.DELIVERY);
        actionPoint2 = new ActionPoint(Time.valueOf("0:3:0"), point2, ActionType.PICK_UP);
        actionPoint3 = new ActionPoint(Time.valueOf("0:0:0"), point3, ActionType.PICK_UP);
        actionPoints = new ArrayList<>();
        actionPoints.add(actionPoint1);
        actionPoints.add(actionPoint2);
        actionPoints.add(actionPoint3);
        time = Time.valueOf("0:0:0");
        journey = new Journey(points, 15);
        journeys = new ArrayList<>();
        journeys.add(journey);
    }

    @Nested
    class calculateTime {
        @Test
        void nullParameter() {
            List<Journey> journeys1 = new ArrayList<>();
            journeys1.add(null);
            List<ActionPoint> actionPoints1 = new ArrayList<>();
            actionPoints1.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(null, actionPoints, time)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys1, actionPoints, time)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys, null, time)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys, actionPoints1, time)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys, actionPoints, null))
            );
        }

        @Test
        void emptyParameter() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(new ArrayList<>(), actionPoints, time)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys, new ArrayList<>(), time))
            );
        }

        @Test
        void incompatibleParameter() {
            List<Point> points1 = new ArrayList<>();
            points1.add(point1);
            points1.add(point2);
            Journey journey1 = new Journey(points1, 5);
            List<Point> points2 = new ArrayList<>();
            points2.add(point2);
            points2.add(point3);
            Journey journey2 = new Journey(points2, 5);
            List<Journey> journeys1 = new ArrayList<>();
            journeys1.add(journey1);
            journeys1.add(journey2);
            List<ActionPoint> actionPoints1 = new ArrayList<>();
            actionPoints1.add(actionPoint1);
            assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTime(journeys1, actionPoints1, time));
        }

        @Test
        void correctUsage() {
            List<Point> points1 = new ArrayList<>();
            points1.add(point3);
            points1.add(point1);
            Journey journey1 = new Journey(points1, 15);
            List<Journey> journeys1 = new ArrayList<>();
            journeys1.add(journey1);
            List<Journey> res = journeyService.calculateTime(journeys1, actionPoints, time);
            assertEquals(Time.valueOf("0:0:03"), res.get(0).getFinishTime(),
                    "calculateTime should return a list of journeys with finish time calculated");
        }
    }

    @Nested
    class findIndexPointInJourneys {
        @Test
        void nullParameter() {
            List<Journey> journeys1 = new ArrayList<>();
            journeys1.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.findIndexPointInJourneys(null, point1, false)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.findIndexPointInJourneys(new ArrayList<>(), point1, false)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.findIndexPointInJourneys(journeys1, point1, false)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.findIndexPointInJourneys(journeys, null, false))
            );
        }

        @Test
        void correctUsage() {
            OptionalInt res1 = journeyService.findIndexPointInJourneys(journeys, point1, true);
            OptionalInt res2 = journeyService.findIndexPointInJourneys(journeys, point3, false);
            OptionalInt res3 = journeyService.findIndexPointInJourneys(journeys, point2, true);
            OptionalInt res4 = journeyService.findIndexPointInJourneys(journeys, point2, false);
            assertAll(
                    () -> assertEquals(0, res1.getAsInt(), "findIndexPointInJourneys should return the journey index"),
                    () -> assertEquals(0, res2.getAsInt(), "findIndexPointInJourneys should return the journey index"),
                    () -> assertTrue(res3.isEmpty(), "findIndexPointInJourneys should return the journey index"),
                    () -> assertTrue(res4.isEmpty(), "findIndexPointInJourneys should return the journey index")
            );
        }
    }

    @Nested
    class getStartEndJourney {
        @Test
        void nullParameter() {
            List<Journey> nullElement = new ArrayList<>();
            nullElement.add(null);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.getStartEndJourney(null, point2, point1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.getStartEndJourney(journeys, null, point1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.getStartEndJourney(journeys, point2, null)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.getStartEndJourney(nullElement, point2, point1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> journeyService.getStartEndJourney(journeys, point2, point1))
            );
        }

        @Test
        void correctUsage() {
            journey.setFinishTime(Time.valueOf("08:00:00"));
            List<Integer> res = journeyService.getStartEndJourney(journeys, point3, point1);
            List<Integer> res1 = journeyService.getStartEndJourney(journeys, point2, point1);
            List<Integer> res2 = journeyService.getStartEndJourney(journeys, point3, point2);
            assertAll(
                    () -> assertEquals(0, res.get(0), "getStartEndJourney should return the right indexes"),
                    () -> assertEquals(0, res.get(1), "getStartEndJourney should return the right indexes"),
                    () -> assertEquals(-1, res1.get(0), "getStartEndJourney should return the right indexes"),
                    () -> assertEquals(0, res1.get(1), "getStartEndJourney should return the right indexes"),
                    () -> assertEquals(0, res2.get(0), "getStartEndJourney should return the right indexes"),
                    () -> assertEquals(-1, res2.get(1), "getStartEndJourney should return the right indexes")
            );
        }
    }

    @Nested
    class calculateTimePointToPoint {
        @Test
        void nullParameter() {
            journey.setFinishTime(Time.valueOf("08:00:00"));
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.calculateTimePointToPoint(journeys, point2, point1)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> journeyService.calculateTimePointToPoint(journeys, point3, point2))
            );
        }

        @Test
        void incompatibleParameter() {
            Point point4 = new Point(11, 20.0, 12.0);
            Point point5 = new Point(111, 20.0, 5.0);
            List<Point> points1 = new ArrayList<>();
            points1.add(point4);
            points1.add(point5);
            points1.add(point1);
            Journey journey1 = new Journey(points1, 10);
            journeys.add(journey1);
            journey.setFinishTime(Time.valueOf("08:00:00"));
            journey1.setFinishTime(Time.valueOf("08:10:00"));
            assertThrows(IllegalArgumentException.class, () -> journeyService.calculateTimePointToPoint(journeys, point1, point3));
        }

//        @Test
//        void correctUsage() {
//            Point point4 = new Point(11, 20.0, 12.0);
//            Point point5 = new Point(111, 20.0, 5.0);
//            List<Point> points1 = new ArrayList<>();
//            points1.add(point4);
//            points1.add(point5);
//            points1.add(point1);
//            Journey journey1 = new Journey(points1, 10);
//            journeys.add(journey1);
//            journey.setFinishTime(Time.valueOf("08:00:00"));
//            journey1.setFinishTime(Time.valueOf("08:10:00"));
//            Time res = journeyService.calculateTimePointToPoint(journeys, point3, point4);
//        }
    }
}