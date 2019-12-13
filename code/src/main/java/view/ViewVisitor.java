package view;

import lombok.Getter;
import model.data.*;

@Getter
public class ViewVisitor implements GenDataVisitor {

    /**
     * The {@link DashBoardController} to notify about changes from the model.
     */
    private DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        dashBoardController.setTour(tour);
        if (tour.getJourneyList() == null) {
            dashBoardController.displayTourWhenNotCalculated();
        } else {
            dashBoardController.clearAll();
            dashBoardController.setActionPoints(tour);
            dashBoardController.drawFullTour();
        }
    }

    @Override
    public void visit(final Graph graph) {
        dashBoardController.displayMap(graph.getPoints().get(0));
    }

    @Override
    public void visit(final Point point) {
    }

    @Override
    public void visit(final DeliveryProcess deliveryProcess) {
        this.dashBoardController.showDeliveryProcess(deliveryProcess);
    }

    @Override
    public void visit(final ActionPoint actionPoint) {
        System.out.println("ViewVisitor action Point");
        dashBoardController.drawAndSaveNewActionPoint(actionPoint);
    }

    @Override
    public void visit(final ListJourneyFromDeliveryProcess
                              listJourneyFromDeliveryProcess) {
        dashBoardController.displayMap(dashBoardController.
                getSelectedActionPoint().getLocation());
        dashBoardController.drawAllActionPoints();
        dashBoardController.drawFullTour();
        dashBoardController.drawPolyline(dashBoardController.
                getMCVPathFormJourneyList(listJourneyFromDeliveryProcess.
                        getJourneyList()), 0.9, 4);
    }

    @Override
    public void visit(final ErrorMessage error) {
        dashBoardController.showAlert("error",
                "An error has occured", error.getMessage());
    }

    void addController(final DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
