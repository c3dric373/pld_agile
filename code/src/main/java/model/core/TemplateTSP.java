package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {

    private Integer[] bestSolution;
    private int lowestCost = 0;
    private Boolean timeLimitExceeded = false;

    public Boolean getTimeLimitExceeded() {
        return timeLimitExceeded;
    }

    public void searchSolution(int timeLimit, int nbNodes, int[][] cost, int[] duration) {
        timeLimitExceeded = false;
        lowestCost = Integer.MAX_VALUE;
        bestSolution = new Integer[nbNodes];
        ArrayList<Integer> notSeen = new ArrayList<>();
        for (int i = 1; i < nbNodes; i++) notSeen.add(i);
        ArrayList<Integer> seen = new ArrayList<>(nbNodes);
        // the first visited node is 0
        seen.add(0);
        branchAndBound(0, notSeen, seen, 0, cost, duration, System.currentTimeMillis(), timeLimit);
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
     * This method should be implemented by subclasses of TemplateTSP
     *
     * @param currentNode current node
     * @param notSeen     table of nodes not seen yet
     * @param cost        cost[i][j] = the duration from i to j, with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node, with 0 <= i < nbNodes
     * @return a lower bound of the cost
     */
    protected abstract int bound(Integer currentNode, ArrayList<Integer> notSeen, int[][] cost, int[] duration);

    /**
     * This method should be implemented by subclasses of TemplateTSP
     *
     * @param currentNode current node
     * @param notSeen     table of nodes not seen yet
     * @param cost        cost[i][j] = the duration from i to j, with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node, with 0 <= i < nbNodes
     * @return an iterator which can iterate the permutation of the nodes not seen
     */
    protected abstract Iterator<Integer> iterator(Integer currentNode, ArrayList<Integer> notSeen, int[][] cost, int[] duration);

    /**
     * Recursive algorithm to calculate the best solution and the lowest cost (by using branch and bound) of the TSP
     *
     * @param currentNode the last visited node
     * @param notSeen     the list of visited nodes
     * @param seen        the list of nodes not visited
     * @param currentCost the sum of the cost so far (from 0 to currentNode)
     * @param cost        cost[i][j] = the duration from i to j, with 0 <= i < nbNodes and 0 <= j < nbNodes
     * @param duration    duration[i] = duration to visit the i-th node, with 0 <= i < nbNodes
     * @param startTime   start time of the resolution
     * @param timeLimit   time limit for the resolution
     */
    void branchAndBound(int currentNode, ArrayList<Integer> notSeen, ArrayList<Integer> seen, int currentCost,
                        int[][] cost, int[] duration, long startTime, int timeLimit) {
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
        } else if (currentCost + bound(currentNode, notSeen, cost, duration) < lowestCost) {
            Iterator<Integer> it = iterator(currentNode, notSeen, cost, duration);
            while (it.hasNext()) {
                Integer nextNode = it.next();
                seen.add(nextNode);
                notSeen.remove(nextNode);
                branchAndBound(nextNode, notSeen, seen, currentCost + cost[currentNode][nextNode] + duration[nextNode], cost, duration, startTime, timeLimit);
                seen.remove(nextNode);
                notSeen.add(nextNode);
            }
        }
    }
}

