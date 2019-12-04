package view;

import model.data.GenDataVisitor;
import model.data.Graph;
import model.data.Point;
import model.data.Tour;

public class ViewVisitor implements GenDataVisitor {

    DashBoardController dashBoardController;
    private UserInterface userInterface;

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

    public void addController(DashBoardController controller,UserInterface userInterface) {
        this.dashBoardController = controller;
        this.userInterface = userInterface;
    }
}
