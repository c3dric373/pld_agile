package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP2 extends TemplateTSP {

    /**
     * Method to estimate future costs.
     *
     * @param currentNode current node
     * @param notSeen     table of nodes not seen yet
     * @param cost        cost[i][j] = the duration from i to j,
     *                    with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node,
     *                    with 0 <= i < nbNodes
     * @return a lower bound of the cost
     */
    @Override
    protected int bound(final Integer currentNode,
                        final ArrayList<Integer> notSeen,
                        final int[][] cost, final int[] duration) {
        int min = Integer.MAX_VALUE;
        for (Integer node : notSeen) {
            if (cost[currentNode][node] + duration[node] < min) {
                min = cost[currentNode][node] + duration[node];
            }
        }
        return min;
    }

    /**
     * Method to get an iterator which can iterate the permutation
     * of the nodes not seen.
     *
     * @param currentNode current node
     * @param notSeen     table of nodes not seen yet
     * @param cost        cost[i][j] = the duration from i to j,
     *                    with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node,
     *                    with 0 <= i < nbNodes
     * @return an iterator which can iterate the permutation
     * of the nodes not seen.
     */
    @Override
    protected Iterator<Integer> iterator(final Integer currentNode,
                                         final ArrayList<Integer> notSeen,
                                         final int[][] cost,
                                         final int[] duration) {
        return new IteratorSeq2(notSeen, currentNode, cost);
    }
}