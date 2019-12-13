package model.core;

public interface TSP {

    /**
     * @return true if time limit is exceeded
     */
    Boolean getTimeLimitExceeded();

    /**
     * Search for a circle which has the lowest cost to visit every node
     * (between 0 and nbNodes - 1)
     *
     * @param timeLimit time limit for the resolution
     * @param nbNodes   number of nodes
     * @param cost      cost[i][j] = the duration from i to j, with 0 <= i <
     *                  nbNodes and 0 <= j < nbNodes
     * @param duration  duration[i] = duration to visit the i-th node, with 0
     *                 <= i < nbNodes
     */
    void searchSolution(int timeLimit, int nbNodes, int[][] cost,
                        int[] duration);

    /**
     * @param i an index
     * @return the node visited in i-th position according to the result of
     * the method searchSolution
     */
    Integer getBestSolution(int i);

    /**
     * @return the lowest cost according to the result of the method
     * searchSolution
     */
    int getLowestCost();
}
