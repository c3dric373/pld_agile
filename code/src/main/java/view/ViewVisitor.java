package view;

import model.data.*;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        //TODO
    }

    @Override
    public void visit(final Graph graph) {
        System.out.println("View Visitor");
        dashBoardController.displayMapPoints(graph.getPoints());
    }

    @Override
    public void visit(final Point point) {
        //TODO
    }

    @Override
    public void visit(final DeliveryProcess deliveryProcess) {
        //TODO
    }

    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
