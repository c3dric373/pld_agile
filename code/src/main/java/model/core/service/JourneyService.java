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
    private static final int NB_SEC_IN_HOUR = 3600;
    /**
     * Number of Seconds in a minute.
     */
    private static final int NB_SEC_IN_MIN = 60;

    /**
     * Travel speed in m/s.
     */
    static final double TRAVEL_SPEED = 15.0 / 3.6;

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
                                              final List<ActionPoint>
                                                      actionPoints,
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
            LocalTime startPlusTravelTime;
            startPlusTravelTime = journeyLocalTime.plusSeconds(travelTimeInSec);

            // get Action time in sec by getting the time in millisecond between
            // actionTime and the reference time
            long actionTimeInMillis =
                    actionTime.getTime() - referenceTime.getTime();
            long divisionFactor = 1000L;
            long actionTimeInSec = actionTimeInMillis / divisionFactor;

            LocalTime startPlusTravelPlusActionTime =
                    startPlusTravelTime.plusSeconds(actionTimeInSec);
            Time arrivalTime = Time.valueOf(startPlusTravelPlusActionTime);
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
        Validate.noNullElements(journeys, "journeys can't contain null"
                + "element");
        Validate.notNull(point, "point can't be null");

        if (endPoint) {
            for (Journey journey : journeys) {
                if (journey.getArrivePoint().equals(point)) {
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
        } else {
            for (Journey journey : journeys) {
                if (journey.getStartPoint().equals(point)) {
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
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
    static ActionPoint findActionPoint(final Point point,
                                       final List<ActionPoint> actionPoints) {
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

    /**
     * Finds the indices of the start journey and the end journey of the tour
     * from a to be.
     *
     * @param journeys   list of journeys
     * @param startPoint start point
     * @param endPoint   end point
     * @return list of Integers corresponding to the found indices, null if not
     * found.
     */
    static List<Integer> getStartEndJourney(final List<Journey> journeys,
                                            final Point startPoint,
                                            final Point endPoint) {
        Validate.notNull(journeys, "journeys can't be null");
        Validate.notNull(startPoint, "startPoint can't be null");
        Validate.notNull(endPoint, "endPoint can't be null");
        Validate.noNullElements(journeys, "journeys can't contain null "
                + "element");
        Journey startJourney = null;
        Journey endJourney = null;
        List<Integer> result = new ArrayList<>();
        for (final Journey journey : journeys) {
            Validate.notNull(journey.getFinishTime(), "journey finish "
                    + "time null");
            if (journey.getStartPoint().getId() == startPoint.getId()) {
                startJourney = journey;
            }
            if (journey.getArrivePoint().getId() == endPoint.getId()) {
                endJourney = journey;
            }
        }
        result.add(journeys.indexOf(startJourney));
        result.add(journeys.indexOf(endJourney));
        return result;

    }

    /**
     * Calculates the time between two points in the
     * {@link model.data.Tour}. It does so by iterating about all the
     * journeys between those 2 points ands sums the time of each
     * journey.
     *
     * @param journeys   the complete list of journeys representing a
     *                   {@link model.data.Tour}
     * @param startPoint startPoint
     * @param endPoint   endPoint
     * @return time between the start and end point
     */
    public static Time calculateTimePointToPoint(final List<Journey> journeys
            , final Point startPoint, final Point endPoint) {
        List<Integer> indices = getStartEndJourney(journeys, startPoint,
                endPoint);
        Validate.isTrue(indices.get(0) != -1 && indices.get(0) != null,
                "start journey null");
        Validate.isTrue(indices.get(1) != -1 && indices.get(1) != null,
                "end journey null");
        Validate.isTrue(indices.get(0) <= indices.get(1),
                "start journey index is bigger than end journey index");
        int sum = 0;
        int divisionFactor = 1000;
        for (int i = indices.get(0); i <= indices.get(1); i++) {
            long firstFinishTime = journeys.get(i).getFinishTime().getTime();
            long secondFinishTime =
                    journeys.get(i - 1).getFinishTime().getTime();
            System.out.println(firstFinishTime);
            System.out.println(secondFinishTime);
            long journeyTime = Math.abs(firstFinishTime - secondFinishTime);
            journeyTime = journeyTime / divisionFactor;
            sum += journeyTime;
        }
        return durationToTime(sum);
    }

    /**
     * Calculates the length between two points in the
     * {@link model.data.Tour}. It does so by iterating about all the
     * journeys between those 2 points ands sums the length of each journey.
     *
     * @param journeys   the complete list of journeys representing a
     *                   {@link model.data.Tour}
     * @param startPoint startPoint
     * @param endPoint   endPoint
     * @return distance between the start and end point
     */
    static int lengthPointToPoint(final List<Journey> journeys,
                                  final Point startPoint,
                                  final Point endPoint) {
        List<Integer> indices = getStartEndJourney(journeys, startPoint,
                endPoint);
        Journey startJourney = journeys.get(indices.get(0));
        Journey endJourney = journeys.get(indices.get(1));
        Validate.notNull(startJourney, "start journey null");
        Validate.notNull(endJourney, "end journey null");
        int sum = 0;
        for (int i = journeys.indexOf(startJourney);
             i < journeys.indexOf(endJourney) + 1; i++) {
            sum += journeys.get(i).getMinLength();
        }
        return sum;
    }

    /**
     * transform a duration in a time.
     *
     * @param durationSec duration in Seconds
     * @return time object corresponding to durationSec
     */
    public static Time durationToTime(final long durationSec) {
        long nbHour = durationSec / NB_SEC_IN_HOUR;
        long nbMin = (durationSec % NB_SEC_IN_HOUR) / NB_SEC_IN_MIN;
        long nbSec = (durationSec % NB_SEC_IN_MIN);
        String durationString;
        durationString = String.format("%d:%02d:%02d", nbHour, nbMin, nbSec);
        Time duration = Time.valueOf(durationString);
        return duration;
    }
}
