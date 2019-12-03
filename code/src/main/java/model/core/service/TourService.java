package model.core.service;

import model.data.*;

import java.util.List;
import java.util.OptionalInt;

public class TourService {


  

    public Tour changeDeliveryOrder(final Tour tour,
                                    final List<ActionPoint> actionPoints) {
        //TODO
        return null;
    }

    /**
     * Changes the position of an Action point on a Tour.
     *
     * @param graph    the map on which the tour takes place.
     * @param tour     the tour.
     * @param oldPoint the old point.
     * @param newPoint the new Point.
     * @return a tour with the old point being replaced by the new one.
     */
    public Tour changePointPosition(final Graph graph, final Tour tour,
                                    final ActionPoint oldPoint,
                                    final Point newPoint) {
        // If point not in delivery processes throw exception 
        // else get index of point in list of delivery processes and update the
        // new delivery process
        List<DeliveryProcess> oldDeliveryProcesses = tour.
                getDeliveryProcesses();
        final OptionalInt optionalOldIndexDP = DeliveryProcessService.
                findActionPoint(oldDeliveryProcesses, oldPoint);
        if (optionalOldIndexDP.isEmpty()) {
            throw new IllegalArgumentException("oldPoint not in "
                    + "delivery Processes");
        }
        int oldIndexDP = optionalOldIndexDP.getAsInt();
        final DeliveryProcess oldDeliveryProcess = oldDeliveryProcesses.
                get(oldIndexDP);
        final ActionPoint newActionPoint = new ActionPoint(oldPoint.getTime(),
                newPoint,oldPoint.getActionType());
        final DeliveryProcess newDeliveryProcess = DeliveryProcessService.
                replacePoint(oldDeliveryProcess,newActionPoint);
        // Replacing
        oldDeliveryProcesses.set(oldIndexDP, newDeliveryProcess);
        //

        // Find the actionPoints before and after the point to be moved
        final List<ActionPoint> actionPoints = tour.getActionPoints();
        final int oldPointIndex = actionPoints.indexOf(oldPoint);
        if (oldPointIndex == -1){
            throw new IllegalArgumentException("oldPoint not in list");
        }
        final ActionPoint predecessorPoint = actionPoints.
                get(oldPointIndex - 1);
        final ActionPoint successorPoint = actionPoints.
                get(oldPointIndex - 1);

        // Calculate shortest path between predecessor and new Point and between
        // the successor and the new Point

        final Journey newPredecessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);
        final Journey newSuccessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);

        // Finding the journeys from and to the old point replacing them with
        // the newly calculated journeys.
        final boolean IS_ENDPOINT = true;
        final List<Journey> oldJourneys = tour.getJourneys();
        final OptionalInt optOldPredecessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys,oldPoint.getLocation(),
                        IS_ENDPOINT);
        final OptionalInt optOldSuccessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys,oldPoint.getLocation(),
                        !IS_ENDPOINT);
        if(optOldPredecessorJ.isEmpty()){
            throw new IllegalArgumentException("Point isn't endPoint of any " 
                    + "Journey");
        }
        if(optOldSuccessorJ.isEmpty()){
            throw new IllegalArgumentException("Point isn't startPoint of any "
                    + "Journey");
        }
        tour.getJourneys().set(optOldPredecessorJ.getAsInt(),
                newPredecessorJourney);
        tour.getJourneys().set(optOldSuccessorJ.getAsInt(),newSuccessorJourney);
        return tour;
}


}
