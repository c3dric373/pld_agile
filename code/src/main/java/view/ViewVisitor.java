package view;

import javafx.scene.control.Alert;
import model.core.service.TourService;
import model.data.*;
import model.data.Point;

import java.awt.*;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;

    @Override
    public void visit(final Tour tour) {
        dashBoardController.setTour(tour);
        if (tour.getJourneyList() == null) {
            System.out.println("---load tour without journey list---");
            dashBoardController.displayLoadedDeliveryProcess();
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
    public void visit(ListJourneyFromDeliveryProcess listJourneyFromDeliveryProcess) {
        dashBoardController.displayMap(dashBoardController.getSelectedActionPoint().getLocation());
        dashBoardController.drawAllActionPoints();
        dashBoardController.drawFullTour();
        dashBoardController.drawPolyline(dashBoardController.getMCVPathFormJourneyListe(listJourneyFromDeliveryProcess.getJourneyList()),0.9,4);
    }


    @Override
    public void visit(final ErrorMessage error) {
        dashBoardController.showAlert("error", "An error has occured",
                error.getMessage(), Alert.AlertType.ERROR);
        System.out.println(error.getMessage());
    }


    public void addController(DashBoardController controller) {
        this.dashBoardController = controller;
    }
}
