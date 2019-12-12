package view;

import model.data.*;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        dashBoardController.setTour(tour);
        if (tour.getActionPoints() == null) {
            dashBoardController.displayLoadedDeliveryProcess();
        }else {
            dashBoardController.drawFullTour();
        }
    }


    @Override
    public void visit(final Graph graph) {
        dashBoardController.displayMap();
    }

    @Override
    public void visit(final Point point) {
    }

    @Override
    public void visit(final DeliveryProcess deliveryProcess) {
        //TODO
    }

    @Override
    public void visit(ActionPoint actionPoint) {
        System.out.println("ViewVisitor action Point");
        dashBoardController.showAndSaveNewActionPoint(actionPoint);
    }

    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
