package model.core.service;


import model.data.*;
import org.apache.commons.lang.Validate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class TourService {

    /**
     * Changes the delivery order a Tour, re calculates the journeys for a new
     * list of action points. Does not optimize anything, only calculates the
     * shortest path from one point to another.
     * @param graph the map.
     * @param tour the tour.
     * @param actionPoints the new action points.
     * @return a new Tour with the modified journeys.
     */
    public Tour changeDeliveryOrder(final Graph graph, final Tour tour,
                                    final List<ActionPoint> actionPoints) {
        final List<ActionPoint> oldActionPoints = tour.getActionPoints();
        if (oldActionPoints.size() != actionPoints.size()) {
            throw new IllegalArgumentException("actonPoints list not "
                    + "of same size");
        }

        GraphService graphService= new GraphService();

        final List<Journey> newJourneys = new ArrayList<>();
        for (int i = 1; i < actionPoints.size(); i++) {
            final Point predecessorPoint = oldActionPoints.get(i - 1).getLocation();
            final Point successorPoint = oldActionPoints.get(i - 1).getLocation();
            final Journey newJourney = graphService.
                    getShortestPath(graph, predecessorPoint.getId(), successorPoint.getId(), null);
            newJourneys.add(newJourney);
        }
        final Time startTime = tour.getStartTime();
        final List<Journey> calculatedJourneys = JourneyService.
                calculateTime(newJourneys, actionPoints, startTime);
        tour.setJourneyList(calculatedJourneys);
        tour.setActionPoints(actionPoints);
        return tour;
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
        final List<Journey> oldJourneys = tour.getJourneyList();
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
                get(oldPointIndex + 1);
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
        GraphService graphService = new GraphService();

        final Journey newPredecessorJourney = graphService.getShortestPath(graph,
                predecessorPoint.getLocation().getId(), newPoint.getId(), null);
        final Journey newSuccessorJourney = graphService.getShortestPath(graph,
                predecessorPoint.getLocation().getId(), newPoint.getId(), null);
        //Replacing the old Journeys with the newly calculated ones

        tour.getJourneyList().set(optOldPredecessorJ.getAsInt(),
                newPredecessorJourney);
        tour.getJourneyList().set(optOldSuccessorJ.getAsInt(),
                newSuccessorJourney);

        // Calculate the time it takes to calculate new Journey
        final List<Journey> newJourneys = JourneyService.calculateTime(
                tour.getJourneyList(), tour.getActionPoints(),
                tour.getStartTime());
        tour.setJourneyList(newJourneys);
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
    public static List<ActionPoint> addNewDeliveryProcess(
            final List<ActionPoint> actionPointList,
            final ActionPoint pickUpPoint, final ActionPoint deliveryPoint){

        Validate.notNull(actionPointList,"actionPointList is null");
        Validate.notNull(pickUpPoint,"pickUpPoint is null");
        Validate.notNull(deliveryPoint,"deliveryPoint is null");

        List<ActionPoint> newActionPointList = actionPointList;
        newActionPointList.add(pickUpPoint);
        newActionPointList.add(deliveryPoint);
        return newActionPointList;

    }

    /**
     * delete the deliveryProcess from the tour
     *
     * @param tour Tour
     * @param deliveryProcess DeliveryProcess
     * @return the tour with the deliveryProcess removed
     */
    public static Tour deleteDeliveryProcess ( final Tour tour,
            final DeliveryProcess deliveryProcess){

        Validate.notNull(tour, "tour is null");
        Validate.notNull(deliveryProcess, "deliveryProcess is null");

        Tour newTour;
        try {
            List<DeliveryProcess> deliveryProcessesList = tour.getDeliveryProcesses();
            List<ActionPoint> actionPointList = tour.getActionPoints();
            ActionPoint pickupPoint = deliveryProcess.getPickUP();
            ActionPoint deliveryPoint = deliveryProcess.getDelivery();

            actionPointList.remove(pickupPoint);
            actionPointList.remove(deliveryPoint);
            deliveryProcessesList.remove(deliveryProcess);
            newTour = new Tour(deliveryProcessesList, tour.getBase(),
                    tour.getStartTime());

        } catch (Exception e){
            throw new IllegalArgumentException(
                    "deliveryProcess doesn't exist in the tour");
        }
        return newTour;
    }

}
