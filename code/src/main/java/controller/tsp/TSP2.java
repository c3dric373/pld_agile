package controller.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP2 extends TemplateTSP {

    @Override
    protected int bound(Integer currentNode, ArrayList<Integer> notSeen,
                        int[][] cost, int[] duration) {
        int min = Integer.MAX_VALUE;
        for (Integer node : notSeen) {
            if (cost[currentNode][node] + duration[node] < min)
                min = cost[currentNode][node] + duration[node];
        }
        return min;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentNode,
                                         ArrayList<Integer> notSeen,
                                         int[][] cost, int[] duration) {
        return new IteratorSeq2(notSeen, currentNode, cost);
    }
}