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
    /**
     * Calls the right methods to process a {@link Tour}.
     *
     * @param listJourneyFromDeliveryProcess the  {@code tour} to process
     */
    public void visit(final Tour tour) {
        this.model.undoTour(tour);
    }

    @Override
    /**
     * Calls the right methods to process a {@link Graph}.
     *
     * @param graph the  {@code graph} to process
     */
    public void visit(final Graph graph) {
        this.model.undoGraph(graph);
    }

    @Override
    /**
     * Calls the right methods to process a {@link Point}.
     *
     * @param point the  {@code point} to process
     */
    public void visit(final Point point) {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * Calls the right methods to process a {@link DeliveryProcess}.
     *
     * @param deliveryProcess the  {@code deliveryProcess} to process
     */
    public void visit(DeliveryProcess deliveryProcess) {
        throw new UnsupportedOperationException();

    }

    @Override
    /**
     * Calls the right methods to process a {@link ActionPoint}.
     *
     * @param actionPoint the  {@code actionPoint} to process
     */
    public void visit(final ActionPoint actionPoint) {
        throw new UnsupportedOperationException();

    }

    @Override
    /**
     * Calls the right methods to process a
     * {@link ListJourneyFromDeliveryProcess}.
     *
     * @param listJourneyFromDeliveryProcess the  {@code
     * ListJourneyFromDeliveryProcess} to process
     */
    public void visit(final ListJourneyFromDeliveryProcess ListJourneyFromDeliveryProcess) {
        throw new UnsupportedOperationException();

    }

    @Override
    /**
     * Calls the right methods to process a
     * {@link ErrorMessage}.
     *
     * @param error the  {@code
     * error} to process
     */
    public void visit(final ErrorMessage error) {
        throw new UnsupportedOperationException();
    }
}
