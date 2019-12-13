package model.core.management;

import model.data.Graph;
import model.data.Tour;

public interface UndoHandler {
    void undoTour(final Tour tour);

    void undoGraph(final Graph graph);
}
