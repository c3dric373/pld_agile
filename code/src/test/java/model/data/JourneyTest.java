package model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JourneyTest {

    private Point startPoint;
    private Point arrivePoint;
    private List<Point> points;
    private double minLength;
    private Journey journey;

    @BeforeEach
    void setUp() {
        startPoint = new Point(12345, 40.5, 40.5);
        arrivePoint = new Point(54321, 50.4, 50.4);
        points = new ArrayList<>();
        points.add(arrivePoint);
        points.add(startPoint);
        minLength = 10.5;
        journey = new Journey(points, minLength);
    }

    @Nested
    class Constructor {
        @Test
        void nullPoints() {
            assertThrows(IllegalArgumentException.class, () -> new Journey(null, minLength));
        }

        @Test
        void notEnoughPoints() {
            List<Point> testPoints = new ArrayList<>();
            testPoints.add(startPoint);
            assertThrows(IllegalArgumentException.class, () -> new Journey(testPoints, minLength));
        }

        @Test
        void hasNullPoint() {
            List<Point> testPoints = new ArrayList<>();
            testPoints.add(startPoint);
            testPoints.add(null);
            assertThrows(IllegalArgumentException.class, () -> new Journey(testPoints, minLength));
        }

        @Test
        void negativeMinLength() {
            assertThrows(IllegalArgumentException.class, () -> new Journey(points, -10));
        }
    }

    @Test
    void getStartPoint() {
        assertEquals(startPoint, journey.getStartPoint(),
                () -> "getStartPoint should return the start point of the journey");
    }

    @Test
    void getArrivePoint() {
        assertEquals(arrivePoint, journey.getArrivePoint(),
                () -> "getArrivePoint should return the arrival point of the journey");
    }

    @Test
    void getPoints() {
        assertEquals(points, journey.getPoints(),
                () -> "getPoints should return the list of points of the journey");
    }

    @Test
    void getMinLength() {
        assertEquals(minLength, journey.getMinLength(),
                () -> "getMinLength should return the min length of the journey");
    }
}