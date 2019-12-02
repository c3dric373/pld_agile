package model.core.service;

import model.data.*;

import javax.swing.*;
import java.util.List;
import java.util.ListIterator;

public class TourService {


    //TODO
    void createActionPointList(List<Journey> journeys){

    }

    public Tour changeDeliveryOrder(final Tour tour,
                                    final List<ActionPoint> actionPoints) {
        //TODO
        return null;
    }

    public Tour changePointPosition(final Graph graph, final Tour tour, final Point oldPoint,
                                    final Point newPoint) {
        // Get the Point just before oldPoint in the tour

        final List<ActionPoint> actionPoints =  tour.getActionPoints();
        final int oldPointIndex = getIndexOfPoint(oldPoint,actionPoints);
        final ActionPoint predecessorPoint = actionPoints.get(oldPointIndex);

        // Calculate shortest path between predecessor and new Point

        final Journey newJourney = GraphService.shortestPath(graph,
                predecessorPoint.getLocation(), newPoint);

        // Add new Journey to tour and delete old information


        return null;
    }

    private int  getIndexOfPoint(final Point oldPoint, final List<ActionPoint> actionPoints) {
        ListIterator<ActionPoint> it = actionPoints.listIterator();
        while (it.hasNext()&&oldPoint ==  it.next().getLocation() ) {
        }
        return  it.nextIndex();
    }
}
