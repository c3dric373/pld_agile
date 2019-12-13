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
    void visit(Tour tour);

    /**
     * Calls the right methods to process a {@link Graph}.
     *
     * @param graph the  {@code graph} to process
     */
    void visit(Graph graph);

    /**
     * Calls the right methods to process a {@link Point}.
     *
     * @param point the  {@code graph} to process
     */
    void visit(Point point);

    /**
     * Calls the right methods to process a {@link DeliveryProcess}.
     *
     * @param deliveryProcess the  {@code graph} to process
     */
    void visit(DeliveryProcess deliveryProcess);

    /**
     * Calls the right methods to process a {@link ActionPoint}.
     *
     * @param actionPoint the  {@code graph} to process
     */
    void visit(ActionPoint actionPoint);

    /**
     * Calls the right methods to process
     * a {@link ListJourneyFromDeliveryProcess}.
     *
     * @param listJourneyFromDeliveryProcess the  {@code graph} to process
     */
    void visit(ListJourneyFromDeliveryProcess listJourneyFromDeliveryProcess);

    /**
     * Calls the right methods to process a {@link ErrorMessage}.
     *
     * @param error the error
     */
    void visit(ErrorMessage error);
}
