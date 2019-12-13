package model.core;

import java.util.ArrayList;

public class TSP4 extends TSP3 {

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
        int firstEnterEstimation = Integer.MAX_VALUE;
        int lastLeaveEstimation = Integer.MAX_VALUE;
        int costEstimation = 0;
        int durationEstimation = 0;
        int nbNodes = cost.length;
        for (Integer node1 : notSeen) {
            int minEnter = Integer.MAX_VALUE;
            int minLeave = Integer.MAX_VALUE;
            for (Integer node2 : notSeen) {
                if (node1.equals(node2)) {
                    continue;
                }
                if (node1 - nbNodes / 2 == node2) {
                    minLeave = Math.min(minLeave, cost[node2][node1]);
                } else if (node2 - nbNodes / 2 == node1) {
                    minEnter = Math.min(minEnter, cost[node1][node2]);
                } else {
                    minLeave = Math.min(minLeave, cost[node2][node1]);
                    minEnter = Math.min(minEnter, cost[node1][node2]);
                }
            }
            if (notSeen.indexOf(node1 + nbNodes / 2) == -1) {
                firstEnterEstimation = Math.min(firstEnterEstimation,
                        cost[currentNode][node1]);
            }
            if (node1 > nbNodes / 2) {
                lastLeaveEstimation = Math.min(lastLeaveEstimation,
                        cost[node1][0]);
            }
            costEstimation += (minEnter + minLeave) / 2;
            durationEstimation += duration[node1];
        }

        return costEstimation + durationEstimation
                + firstEnterEstimation / 2 + lastLeaveEstimation / 2;
    }
}
