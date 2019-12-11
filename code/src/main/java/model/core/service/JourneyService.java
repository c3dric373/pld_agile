package model.core.service;

import model.data.ActionPoint;
import model.data.Journey;
import model.data.Point;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class JourneyService {

    /**
     * Number of Seconds in a Hour.
     */
    static final int NB_SEC_IN_HOUR = 3600;
    /**
     * Number of Seconds in a minute.
     */
    static final int NB_SEC_IN_MIN = 60;

    /**
     * Travel speed in m/s
     */
    final static double TRAVEL_SPEED = 15.0 / 3.6;

    /**
     * Calculate the finish time for each ActionPoints in each journeys.
     * finish time = start time + travel time + Action time
     *
     * @param journeys     list of journeys
     * @param actionPoints list of ActionPoints
     * @param startTime    start Time of the tour
     * @return list of journeys
     */
    public static List<Journey> calculateTime(final List<Journey> journeys,
                                              final List<ActionPoint> actionPoints,
                                              final Time startTime) {
        Validate.notNull(journeys, "journeys is null");
        Validate.noNullElements(journeys,
                "journeys of the List can't be null");
        Validate.notEmpty(journeys, "journeys can't be empty");
        Validate.notNull(actionPoints, "actionPoints is null");
        Validate.noNullElements(actionPoints,
                "actionPoints of the List can't be null");
        Validate.notEmpty(actionPoints,
                "actionPoints can't be empty");
        Validate.notNull(startTime,
                "startTime can't be null");

        Time journeyStartTime = startTime;
        ArrayList<Journey> newJourneyList = new ArrayList<>();
        Time referenceTime = Time.valueOf("0:0:0");

        for (Journey journey : journeys) {
            List<Point> points = journey.getPoints();
            Point arrivalPoint = points.get(0);
            ActionPoint arrivalActionPoint = findActionPoint(arrivalPoint,
                    actionPoints);
            Time actionTime = arrivalActionPoint.getTime();
            double length = journey.getMinLength();

            //duration = length/speed
            int travelTimeInSec = (int) (length / TRAVEL_SPEED);

            LocalTime journeyLocalTime = journeyStartTime.toLocalTime();
            LocalTime StartPlusTravelTime;
            StartPlusTravelTime = journeyLocalTime.plusSeconds(travelTimeInSec);

            // get Action time in sec by getting the time in millisecond between
            // actionTime and the reference time
            long actionTimeInMillis = actionTime.getTime() - referenceTime.getTime();
            long actionTimeInSec = actionTimeInMillis / 1000L;

            LocalTime StartPlusTravelPlusActionTime = StartPlusTravelTime.plusSeconds(actionTimeInSec);
            Time arrivalTime = Time.valueOf(StartPlusTravelPlusActionTime);
            journeyStartTime = arrivalTime;
            journey.setFinishTime(arrivalTime);

            // add journey to the list of journeys
            newJourneyList.add(journey);
        }
        return newJourneyList;
    }

    /**
     * Searches for a journey with a specific point as either arrival or start
     * point.
     *
     * @param journeys The list of journeys to search in.
     * @param point    the specific point.
     * @param endPoint boolean flag to indicate if the point should be an
     *                 endpoint or a start point if true the point should
     *                 be an endpoint
     * @return an Optional with the value if a journey was found and an empty
     * one if not.
     */
    static OptionalInt findIndexPointInJourneys(final List<Journey> journeys,
                                                final Point point,
                                                final boolean endPoint) {
        Validate.notNull(journeys, "journeys can't be null");
        Validate.notEmpty(journeys, "journeys can't be empty");
        Validate.noNullElements(journeys, "journeys can't contain null element");
        Validate.notNull(point, "point can't be null");

        if (endPoint) {
            for (Journey journey : journeys)
                if (journey.getStartPoint() == point)
                    return OptionalInt.of(journeys.indexOf(journey));
        } else {
            for (Journey journey : journeys)
                if (journey.getArrivePoint() == point)
                    return OptionalInt.of(journeys.indexOf(journey));
        }
        return OptionalInt.empty();
    }

    /**
     * Find ActionPoint corresponding to the provided Point.
     *
     * @param point        Point
     * @param actionPoints list of Action Points
     * @return the corresponding actionPoint
     */
    private static ActionPoint findActionPoint(Point point,
                                               List<ActionPoint> actionPoints) {
        long id = point.getId();
        ActionPoint correspondingActionPoint = null;
        for (ActionPoint actionPoint : actionPoints) {
            if (id == actionPoint.getLocation().getId()) {
                correspondingActionPoint = actionPoint;
                break;
            }
        }
        if (correspondingActionPoint == null) {
            throw new IllegalArgumentException("This is not an ActionPoint");
        }
        return correspondingActionPoint;
    }


}