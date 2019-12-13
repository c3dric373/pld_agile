package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP3 implements TSP {

    /**
     * Table that represent the best Solution.
     */
    private Integer[] bestSolution;

    /**
     * lower cost.
     */
    private int lowestCost = 0;

    /**
     * boolean to know if the time limit has been eceeded.
     */
    private Boolean timeLimitExceeded = false;

    /**
     * ex. records[29][2] represents the lowest cost so far
     * to visit 0, 2, 3 and 4 (started with 0, end with 2)
     * cuz 29 represents '11101' in binary, 2 represents the current index
     */
    private int[][] records;

    /**
     * the binary representation of the index already visited
     * ex. '11101' means 0, 2, 3 and 4 visited, 1 not visited yet
     */
    private int seenInBinary;

    public Boolean getTimeLimitExceeded() {
        return timeLimitExceeded;
    }

    public void searchSolution(final int timeLimit, final int nbNodes,
                               final int[][] cost, final int[] duration) {
        timeLimitExceeded = false;
        lowestCost = Integer.MAX_VALUE;
        bestSolution = new Integer[nbNodes];
        ArrayList<Integer> notSeen = new ArrayList<>();
        for (int i = 1; i < nbNodes; i++) notSeen.add(i);
        ArrayList<Integer> seen = new ArrayList<>(nbNodes);
        // the first visited node is 0
        seen.add(0);
        seenInBinary = 1;
        // initialize records
        int length = (1 << nbNodes) - 1;
        records = new int[length + 1][nbNodes];
        for (int i = 0; i < length; i++)
            for (int j = 0; j < nbNodes; j++)
                records[i][j] = Integer.MAX_VALUE;
        records[0][0] = 0;

        branchAndBound(0, notSeen, seen, 0, cost,
                duration, System.currentTimeMillis(), timeLimit);
    }

    public Integer getBestSolution(int i) {
        if ((bestSolution == null) || (i < 0) || (i >= bestSolution.length))
            return null;
        return bestSolution[i];
    }

    public int getLowestCost() {
        return lowestCost;
    }

    /**
     * Method to estimate future costs
     *
     * @param currentNode current node
     * @param notSeen     table of nodes not seen yet
     * @param cost        cost[i][j] = the duration from i to j,
     *                    with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node,
     *                    with 0 <= i < nbNodes
     * @return a lower bound of the cost
     */
    protected int bound(final Integer currentNode,
                        final ArrayList<Integer> notSeen, final int[][] cost,
                        final int[] duration) {
        int costEstimation = Integer.MAX_VALUE;
        int durationEstimation = 0;
        int nbNodes = cost.length;
        for (Integer node : notSeen) {
            if (cost[currentNode][node] < costEstimation)
                costEstimation = cost[currentNode][node];
            durationEstimation += duration[node];
        }
        for (Integer node1 : notSeen) {
            int min = Integer.MAX_VALUE;
            for (Integer node2 : notSeen) {
                if (node1.equals(node2) || node2 + nbNodes / 2 == node1) {
                    continue;
                }
                min = Math.min(cost[node1][node2], min);
            }
            costEstimation += min;
        }

        return costEstimation + durationEstimation;
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
    protected Iterator<Integer> iterator(final Integer currentNode,
                                         final ArrayList<Integer> notSeen,
                                         final int[][] cost,
                                         final int[] duration) {
        return new IteratorSeq2(notSeen, currentNode, cost);
    }

    /**
     * Recursive algorithm to calculate the best solution
     * and the lowest cost (by using branch and bound) of the TSP.
     *
     * @param currentNode the last visited node
     * @param notSeen     the list of visited nodes
     * @param seen        the list of nodes not visited
     * @param currentCost the sum of the cost so far (from 0 to currentNode)
     * @param cost        cost[i][j] = the duration from i to j,
     *                    with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node,
     *                    with 0 <= i < nbNodes
     * @param startTime   start time of the resolution
     * @param timeLimit   time limit for the resolution
     */
    void branchAndBound(final int currentNode, final ArrayList<Integer> notSeen,
                        final ArrayList<Integer> seen, int currentCost,
                        final int[][] cost, final int[] duration,
                        final long startTime, final int timeLimit) {
        if (System.currentTimeMillis() - startTime >= timeLimit) {
            timeLimitExceeded = true;
            return;
        }
        // if all the nodes have been visited
        if (notSeen.size() == 0) {
            currentCost += cost[currentNode][0];
            // if this solution is better
            if (currentCost < lowestCost) {
                seen.toArray(bestSolution);
                lowestCost = currentCost;
            }
        } else if (currentCost + bound(currentNode, notSeen, cost, duration) < lowestCost && records[seenInBinary][currentNode] > currentCost) {
            records[seenInBinary][currentNode] = currentCost;
            Iterator<Integer> it;
            it = iterator(currentNode, notSeen, cost, duration);
            while (it.hasNext()) {
                Integer nextNode = it.next();
                seen.add(nextNode);
                notSeen.remove(nextNode);
                seenInBinary += (1 << nextNode);
                branchAndBound(nextNode, notSeen, seen,
                        currentCost + cost[currentNode][nextNode] + duration[nextNode], cost, duration, startTime, timeLimit);
                seen.remove(nextNode);
                notSeen.add(nextNode);
                seenInBinary -= (1 << nextNode);
            }
        }
    }
}

