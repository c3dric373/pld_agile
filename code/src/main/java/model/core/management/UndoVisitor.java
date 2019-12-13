package model.core.management;

import model.data.*;

/**
 * Visits all possible {@link GenData} that have the possiblility to generate
 * an undo.
 */
public class UndoVisitor implements GenDataVisitor {

    /**
     * The model to which it should pass on the information.
     */
    private final UndoHandler model;

    /**
     * Constructor.
     *
     * @param applicationManager the model to communicate with.
     */
    UndoVisitor(final UndoHandler applicationManager) {
        this.model = applicationManager;
    }

    @Override
    public void visit(final Tour tour) {
        this.model.undoTour(tour);
    }

    @Override
    public void visit(final Graph graph) {
        this.model.undoGraph(graph);
    }

    @Override
    public void visit(final Point point) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(DeliveryProcess deliveryProcess) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void visit(final ActionPoint actionPoint) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void visit(final ListJourneyFromDeliveryProcess
                                  listJourneyFromDeliveryProcess) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void visit(final ErrorMessage error) {
        throw new UnsupportedOperationException();
    }
}
