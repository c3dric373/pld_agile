package model.core.management;

import model.data.Graph;
import model.data.Tour;

/**
 * Interface that provides a way for a {@link model.data.GenDataVisitor} to
 * communicate with an {@link ApplicationManagerImpl}.
 */
public interface UndoHandler {
    /**
     * Handle a undo with a {@link Tour} as parameter.
     *
     * @param tour the tour to reload.
     */
    void undoTour(Tour tour);

    /**
     * Handle a undo with a {@link Graph} as parameter.
     *
     * @param graph the tour to reload.
     */
    void undoGraph(Graph graph);
}
