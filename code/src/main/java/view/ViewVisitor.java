package view;

import model.data.GenDataVisitor;
import model.data.Graph;
import model.data.Point;
import model.data.Tour;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        if (tour.getActionPoints() == null) {
            dashBoardController.displayLoadedDeliveryProcess(tour);
        } else {
            dashBoardController.displayMapActionPoints(tour.getActionPoints());
        }
    }

    @Override
    public void visit(final Graph graph) {
        System.out.println("View Visitor Graph");
        dashBoardController.displayMap();
        //dashBoardController.displayMapPoints(graph.getPoints());
    }

    @Override
    public void visit(final Point point) {
        //TODO
    }

    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
