package model.core.service;

import lombok.Setter;
import model.data.ActionPoint;
import model.data.Journey;
import model.data.Point;
import model.data.Tour;

import javax.swing.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class JourneyService {

    /**
     * Number of Seconds in a Hour.
     */
    static final int NB_SEC_IN_HOUR = 3600;
    /**
     * Number of Seconds in a minute.
     */
    static final int NB_SEC_IN_MIN = 60;

    final double TRAVEL_SPEED=15.0/3.6;

    public List<Journey> calculateTime(final List<Journey> journeys,
                                final List<ActionPoint> actionPoints,
                                Time startTime) {
        ArrayList<Journey> newJourneyList = new ArrayList<Journey>();
        for(Journey journey:journeys){

            List<Point> points = journey.getPoints();
            Point arrivalPoint = points.get(0);
            ActionPoint arrivalActionPoint = findActionPoint(arrivalPoint,
                    actionPoints);
            Time actionTime = arrivalActionPoint.getTime();
            double length = journey.getMinLength();
            int timeInSec = (int)(length/TRAVEL_SPEED);
            Time travellingTime = durationToTime(timeInSec);
            Time arrivalTime=null;
            arrivalTime.setTime(startTime.getTime() + actionTime.getTime() +
                    travellingTime.getTime());
            startTime = arrivalTime;
            System.out.println(arrivalTime);
            journey.setFinishTime(arrivalTime);

            newJourneyList.add(journey);
        }
        return newJourneyList;
    }

    /**
     * transform a duration in a time.
     *
     * @param durationSec duration in Seconds
     * @return time object corresponding to durationSec
     */
    private Time durationToTime(final int durationSec) {
        int nbHour = durationSec / NB_SEC_IN_HOUR;
        int nbMin = (durationSec % NB_SEC_IN_HOUR) / NB_SEC_IN_MIN;
        int nbSec = (durationSec % NB_SEC_IN_MIN);
        String durationString;
        durationString = String.format("%d:%02d:%02d", nbHour, nbMin, nbSec);
        Time duration = Time.valueOf(durationString);
        System.out.println("duration = " + duration);
        return duration;
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