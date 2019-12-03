package model.core.service;

import model.data.*;

import java.util.List;
import java.util.ListIterator;

public class TourService {


    //TODO
    void createActionPointList(List<Journey> journeys) {

    }

    public Tour changeDeliveryOrder(final Tour tour,
                                    final List<ActionPoint> actionPoints) {
        //TODO
        return null;
    }

    /**
     * Changes the position of an Action point on a Tour. 
     * @param graph the map on which the tour takes place.
     * @param tour the tour.
     * @param oldPoint the old point.
     * @param newPoint the new Point. 
     * @return a tour with the old point being replaced by the new one. 
     */
    public Tour changePointPosition(final Graph graph, final Tour tour,
                                    final ActionPoint oldPoint,
                                    final Point newPoint) {
        // Get the Point just before oldPoint in the tour

        final List<ActionPoint> actionPoints = tour.getActionPoints();
        final int oldPointIndex = getIndexOfPoint(oldPoint.getLocation(), actionPoints);
        final ActionPoint predecessorPoint = actionPoints.
                get(oldPointIndex - 1);

        // Calculate shortest path between predecessor and new Point and between
        // and the successor and the new Point

        final Journey newPredecessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);
        final Journey newSuccessorJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);

        // Modify Location in DeliveryProcesses List


        final ActionPoint oldActionPoint = actionPoints.get(oldPointIndex);
        DeliveryProcess oldDeliveryProcess;
        List<DeliveryProcess> oldDeliveryProcesses = tour.
                getDeliveryProcesses();
        int oldIndexDP = -1;
        DeliveryProcess newDeliveryProcess = null;

        for (final DeliveryProcess deliveryProcess : oldDeliveryProcesses) {
            if (deliveryProcess.getDelivery() == oldActionPoint
                    || deliveryProcess.getPickUP() == oldActionPoint) {
                oldDeliveryProcess = deliveryProcess;
                oldIndexDP = oldDeliveryProcesses.indexOf(oldDeliveryProcess);
                final ActionType oldActionType = oldActionPoint.getActionType();
                final ActionPoint newActionPoint = new ActionPoint(
                        oldActionPoint.getTime(), newPoint, oldActionType);
                if (oldActionType == ActionType.DELIVERY) {
                    newDeliveryProcess = new DeliveryProcess(
                            deliveryProcess.getPickUP(), newActionPoint);
                } else {
                    newDeliveryProcess = new DeliveryProcess(
                            newActionPoint, deliveryProcess.getDelivery());
                }
            }
        }
        tour.getDeliveryProcesses().set(oldIndexDP, newDeliveryProcess);

        // Add new Journey to tour
        final List<Journey> journeys = tour.getJourneys();


        return null;
    }

    /**
     * Gets the index of an Action Point that is at the same location than a
     * given point.
     *
     * @param oldPoint     the given point.
     * @param actionPoints the list of action Points/
     * @return the specific index.
     */
    private int getIndexOfPoint(final Point oldPoint,
                                final List<ActionPoint> actionPoints) {
        ListIterator<ActionPoint> it = actionPoints.listIterator();
        while (it.hasNext() && oldPoint == it.next().getLocation()) {
        }
        return it.nextIndex();
    }
}
