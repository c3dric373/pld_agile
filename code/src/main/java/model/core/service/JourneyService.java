package model.core.service;

import lombok.Setter;
import model.data.ActionPoint;
import model.data.Journey;
import model.data.Point;
import model.data.Tour;
import org.apache.commons.lang.Validate;

import javax.swing.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JourneyService {

    void calculateTime(final Journey journey) {
    /**
     * Number of Seconds in a Hour.
     */
    static final int NB_SEC_IN_HOUR = 3600;
    /**
     * Number of Seconds in a minute.
     */
    static final int NB_SEC_IN_MIN = 60;

    final double TRAVEL_SPEED = 15.0/3.6;

    public List<Journey> calculateTime(final List<Journey> journeys,
                                final List<ActionPoint> actionPoints,
                                final Time startTime) {
        Validate.notNull(journeys, "journeys is null");
        Validate.noNullElements(journeys, "journeys of the List can't be null");
        Validate.notEmpty(journeys, "journeys can't be empty");
        Validate.notNull(actionPoints, "actionPoints is null");
        Validate.noNullElements(actionPoints, "actionPoints of the List can't be null");
        Validate.notEmpty(actionPoints, "actionPoints can't be empty");
        Validate.notNull(startTime, "startTime can't be null");

        Time journeyStartTime = startTime;
        ArrayList<Journey> newJourneyList = new ArrayList<Journey>();
        Time referenceTime =Time.valueOf("0:0:0");
        for(Journey journey:journeys){

            List<Point> points = journey.getPoints();
            Point arrivalPoint = points.get(0);
            ActionPoint arrivalActionPoint = findActionPoint(arrivalPoint,
                    actionPoints);
            Time actionTime = arrivalActionPoint.getTime();
            double length = journey.getMinLength();
            int travelTimeInSec = (int)(length/TRAVEL_SPEED);
            System.out.println("-----------------");
            System.out.println("startTime "+ journeyStartTime);
            System.out.println ("travelTime " +travelTimeInSec+ " in hhmm " +travelTimeInSec/60 + ": " + travelTimeInSec%60 );
            LocalTime journeyLocalTime = journeyStartTime.toLocalTime();
            System.out.println("action time " +actionTime);
            LocalTime StartPlusTravelTime = journeyLocalTime.plusSeconds(travelTimeInSec);
            long actionTimeInSec = (actionTime.getTime() - referenceTime.getTime())/1000l;
            LocalTime StartPlusTravelPlusActionTime = StartPlusTravelTime.plusSeconds(actionTimeInSec);
            Time arrivalTime = Time.valueOf(StartPlusTravelPlusActionTime);
            journeyStartTime = arrivalTime;
            System.out.println("arrival time "+arrivalTime);
            journey.setFinishTime(arrivalTime);

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
        if (endPoint) {
            for (Journey journey : journeys) {
                if (journey.getStartPoint() == point) {
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
        } else {
            for (Journey journey : journeys) {
                if (journey.getArrivePoint() == point) {
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
        }
        return OptionalInt.empty();
    }
    private ActionPoint findActionPoint(Point point,
                                        List<ActionPoint> actionPoints){
        long id = point.getId();
        ActionPoint correspondingActionPoint=null;
        for(ActionPoint actionPoint : actionPoints){
            if(id == actionPoint.getLocation().getId()){
                correspondingActionPoint = actionPoint;
                break;
            }
        }
        if(correspondingActionPoint == null){
            throw new IllegalArgumentException("This is not an ActionPoint");
        }
        return correspondingActionPoint;
    }


}