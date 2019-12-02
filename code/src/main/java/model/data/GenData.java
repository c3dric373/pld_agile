package model.data;

/**
 * The general implementation of data that is passed to the
 * {@link view.Observer}.
 * Includes an accept method for the{@link GenDataVisitor}.
 */
public interface GenData {

    void accept(final GenDataVisitor genDataVisitor);
}
