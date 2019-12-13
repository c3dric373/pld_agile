package controller.management;

import model.genData.Graph;
import model.genData.Tour;
import model.genData.GenDataVisitor;

/**
 * Interface that provides a way for a {@link GenDataVisitor} to
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
