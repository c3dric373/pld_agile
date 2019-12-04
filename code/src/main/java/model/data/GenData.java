package model.data;

/**
 * The general implementation of data that is passed to the
 * {@link view.Observer}.
 * Includes an accept method for the{@link GenDataVisitor}.
 */
public interface GenData {

    /**
     * Method to call the {@link GenDataVisitor} on this data.
     *
     * @param genDataVisitor The visitor instance to be called.
     */
    void accept(final GenDataVisitor genDataVisitor);
}
