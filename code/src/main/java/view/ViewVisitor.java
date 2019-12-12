package view;

import model.data.*;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        System.out.println(tour.getBase().toString());
        dashBoardController.setTour(tour);
        if (tour.getActionPoints() == null) {
            dashBoardController.displayLoadedDeliveryProcess();
        }else {
            dashBoardController.setActionPoints(tour);
            dashBoardController.drawFullTour();
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

    @Override
    public void visit(final DeliveryProcess deliveryProcess) {
        this.dashBoardController.showDeliveryProcess(deliveryProcess);


    }

    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
