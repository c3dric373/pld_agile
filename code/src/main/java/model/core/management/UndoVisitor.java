package model.core.management;

import model.data.*;

public class UndoVisitor implements GenDataVisitor {
    private final UndoHandler model;

    @Override
    public void visit(final Tour tour) {
        this.model.undoTour(tour);
    }

    @Override
    public void visit(final Graph graph) {
        this.model.undoGraph(graph);
    }

    @Override
    public void visit(Point point) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(DeliveryProcess deliveryProcess) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void visit(ActionPoint actionPoint) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void visit(ListJourneyFromDeliveryProcess listJourneyFromDeliveryProcess) {
        throw new UnsupportedOperationException();

    }

    UndoVisitor(UndoHandler applicationManager) {
        this.model = applicationManager;
    }
}
