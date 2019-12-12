package view;

import model.data.*;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        dashBoardController.setTour(tour);
        if (tour.getJourneyList() == null) {
            dashBoardController.displayLoadedDeliveryProcess();
        } else {
            dashBoardController.clearAll();
            dashBoardController.setActionPoints(tour);
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
        this.dashBoardController.showDeliveryProcess(deliveryProcess);
    }

    @Override
    public void visit(ActionPoint actionPoint) {
        System.out.println("ViewVisitor action Point");
        dashBoardController.drawAndSaveNewActionPoint(actionPoint);
    }

    @Override
    public void visit(ListJourneyFromDeliveryProcess listJourneyFromDeliveryProcess) {
        dashBoardController.displayMap();
        dashBoardController.drawAllActionPoints();
        dashBoardController.drawFullTour();
        //dashBoardController.drawPolyline(dashBoardController.getMCVPathFormJourneyListe(listJourneyFromDeliveryProcess.getJourneyList()),dashBoardController.pointToColour(listJourneyFromDeliveryProcess.getJourneyList().get(0).getPoints().get(0)),0.4);
        dashBoardController.drawPolyline(dashBoardController.getMCVPathFormJourneyListe(listJourneyFromDeliveryProcess.getJourneyList()), "red", 0.4);
    }


    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
