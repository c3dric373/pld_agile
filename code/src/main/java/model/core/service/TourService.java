package model.core.service;


import model.data.*;
import org.apache.commons.lang.Validate;

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
        List<DeliveryProcess> oldDeliveryProcesses = tour.
                getDeliveryProcesses();
        final OptionalInt optionalOldIndexDP = DeliveryProcessService.
                findActionPoint(oldDeliveryProcesses, oldPoint);
        if (optionalOldIndexDP.isEmpty()) {
            throw new IllegalArgumentException("oldPoint not in "
                    + "delivery Processes");
        }

        // Finding the journeys from and to the old point
        final boolean IS_ENDPOINT = true;
        final List<Journey> oldJourneys = tour.getJourneys();
        final OptionalInt optOldPredecessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys, oldPoint.getLocation(),
                        IS_ENDPOINT);
        final OptionalInt optOldSuccessorJ = JourneyService.
                findIndexPointInJourneys(oldJourneys, oldPoint.getLocation(),
                        !IS_ENDPOINT);
        if (optOldPredecessorJ.isEmpty()) {
            throw new IllegalArgumentException("Point isn't endPoint of any "
                    + "Journey");
        }
        if (optOldSuccessorJ.isEmpty()) {
            throw new IllegalArgumentException("Point isn't startPoint of any "
                    + "Journey");
        }
        // Find the actionPoints before and after the point to be moved

        final List<ActionPoint> actionPoints = tour.getActionPoints();
        final int oldPointIndex = actionPoints.indexOf(oldPoint);
        if (oldPointIndex == -1) {
            throw new IllegalArgumentException("oldPoint not in list");
        }
        final ActionPoint predecessorPoint = actionPoints.
                get(oldPointIndex - 1);
        final ActionPoint successorPoint = actionPoints.
                get(oldPointIndex - 1);
        // Create a new actionPoint and replace the old one in the list of
        // delivery processes

        int oldIndexDP = optionalOldIndexDP.getAsInt();
        final DeliveryProcess oldDeliveryProcess = oldDeliveryProcesses.
                get(oldIndexDP);
        final ActionPoint newActionPoint = new ActionPoint(oldPoint.getTime(),
                newPoint, oldPoint.getActionType());
        final DeliveryProcess newDeliveryProcess = DeliveryProcessService.
                replacePoint(oldDeliveryProcess, newActionPoint);
        oldDeliveryProcesses.set(oldIndexDP, newDeliveryProcess);

        // Calculate shortest path between predecessor and new Point and between
        // the successor and the new Point

        final Journey newPredecessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);
        final Journey newSuccessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);
        //Replacing the old Journeys with the newly calculated ones

        tour.getJourneys().set(optOldPredecessorJ.getAsInt(),
                newPredecessorJourney);
        tour.getJourneys().set(optOldSuccessorJ.getAsInt(),
                newSuccessorJourney);
        return tour;
    }

    /**
     * Adds 2 new action points to an ordered ActionPoint list.
     * The 2 points represent a deliveryProcess.
     * @param actionPointList Current ordered ActionPoint list.
     * @param pickUpPoint New PickUp point to add.
     * @param deliveryPoint New DeliveryPoint to add.
     * @return Returns the new ActionPoint list with the new DeliveryProcess
     * added.
     */
    static List<ActionPoint> addNewDeliveryProcess(
            final List<ActionPoint> actionPointList,
            final ActionPoint pickUpPoint, final ActionPoint deliveryPoint){

        Validate.notNull(actionPointList,"ActionPoints list is null");
        Validate.notNull(pickUpPoint,"Pickup Point is null");
        Validate.notNull(deliveryPoint,"Delivery Point is null");

        List<ActionPoint> newActionPointList = actionPointList;
        newActionPointList.add(pickUpPoint);
        newActionPointList.add(pickUpPoint);
        return newActionPointList;

    }


}
