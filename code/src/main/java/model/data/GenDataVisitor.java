package model.data;

/**
 * This class allows to handle different data types sent to the view.
 * Every data type will be handled according to it's own type.
 */
public interface GenDataVisitor {

    /**
     * Calls the right methods to process a {@link Tour}.
     *
     * @param tour the the {@code tour} to process
     */
    void visit(final Tour tour);

    /**
     * Calls the right methods to process a {@link Graph}.
     *
     * @param graph the  {@code graph} to process
     */
    void visit(final Graph graph);

    /**
     * Calls the right methods to process a {@link Point}.
     *
     * @param point the  {@code graph} to process
     */
    void visit(final Point point);

    /**
     * Calls the right methods to process a {@link DeliveryProcess}.
     *
     * @param deliveryProcess the  {@code graph} to process
     */
    void visit(final DeliveryProcess deliveryProcess);
}
