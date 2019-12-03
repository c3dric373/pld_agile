package model.core.service;

import model.data.Journey;
import model.data.Point;

import java.util.List;
import java.util.OptionalInt;

public class JourneyService {

    void calculateTime(final Journey journey) {
    }

    /**
     * Searches for a journey with a specific point as either arrival or start
     * point.
     * @param journeys The list of journeys to search in.
     * @param point the specific point.
     * @param endPoint boolean flag to indicate if the point should be an
     *                 endpoint or a start point if true the point should
     *                 be an endpoint
     * @return an Optional with the value if a journey was found and an empty
     * one if not.
     */
    static OptionalInt findIndexPointInJourneys(final List<Journey> journeys,
                                                final Point point,
                                                final boolean endPoint){
        if(endPoint){
            for (Journey journey : journeys){
                if(journey.getStartPoint() == point){
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
        }else{
            for (Journey journey : journeys){
                if(journey.getArrivePoint() == point){
                    return OptionalInt.of(journeys.indexOf(journey));
                }
            }
        }
        return OptionalInt.empty();
    }
}
