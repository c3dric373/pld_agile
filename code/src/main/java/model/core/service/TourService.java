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

    public Tour changePointPosition(final Graph graph, final Tour tour,
                                    final Point oldPoint,
                                    final Point newPoint) {
        // Get the Point just before oldPoint in the tour

        final List<ActionPoint> actionPoints = tour.getActionPoints();
        final int oldPointIndex = getIndexOfPoint(oldPoint, actionPoints);
        final ActionPoint predecessorPoint = actionPoints.
                get(oldPointIndex - 1);

        // Calculate shortest path between predecessor and new Point

        final Journey newJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);

        // Modify Location in DeliveryProcesses List

        final ActionPoint oldActionPoint = actionPoints.get(oldPointIndex);
        DeliveryProcess oldDeliveryProcess;
        List<DeliveryProcess> oldDeliveryProcesses = tour.
                getDeliveryProcesses();
        int oldIndexDP = -1;
        DeliveryProcess newDeliveryProcess = null;

        for (DeliveryProcess deliveryProcess : oldDeliveryProcesses) {
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

    private int getIndexOfPoint(final Point oldPoint,
                                final List<ActionPoint> actionPoints) {
        ListIterator<ActionPoint> it = actionPoints.listIterator();
        while (it.hasNext() && oldPoint == it.next().getLocation()) {
        }
        return it.nextIndex();
    }
}
