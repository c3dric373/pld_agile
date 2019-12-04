package model.core.service;

import lombok.Setter;
import model.data.ActionPoint;
import model.data.Journey;
import model.data.Point;
import model.data.Tour;

import javax.swing.*;
import java.sql.Time;
import java.time.LocalTime;
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
            System.out.println("startTime "+ startTime);
            System.out.println ("travelTime " +travelTimeInSec+ " in hhmm " +travelTimeInSec/60 + ": " + travelTimeInSec%60 );
            LocalTime localTime = startTime.toLocalTime();
            System.out.println("action time " +actionTime);
            LocalTime StartPlusTravelTime = localTime.plusSeconds(travelTimeInSec);
            long actionTimeInSec = (actionTime.getTime() - referenceTime.getTime())/1000l;
            LocalTime StartPlusTravelPlusActionTime = StartPlusTravelTime.plusSeconds(actionTimeInSec);
            Time arrivalTime = Time.valueOf(StartPlusTravelPlusActionTime);
            startTime = arrivalTime;
            System.out.println("arrival time "+arrivalTime);
            journey.setFinishTime(arrivalTime);

            newJourneyList.add(journey);
        }
        return newJourneyList;
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