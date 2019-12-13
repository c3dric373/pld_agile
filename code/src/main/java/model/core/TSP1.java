package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

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
        return 0;
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
        return new IteratorSeq(notSeen, currentNode);
    }
//
//    public static void main(String[] args) {
//        TSP2 tsp2 = new TSP2();
//        int timeLimit = Integer.MAX_VALUE;
//        int nbNodes = 5;
//        int[][] cout = new int[nbNodes][nbNodes];
//        cout[0][0] = 0;
//        cout[0][1] = 1;
//        cout[0][2] = 2;
//        cout[0][3] = 1;
//        cout[0][4] = 2;
//        cout[1][0] = 1;
//        cout[1][1] = 0;
//        cout[1][2] = 3;
//        cout[1][3] = 100;
//        cout[1][4] = 3;
//        cout[2][0] = 2;
//        cout[2][1] = 3;
//        cout[2][2] = 0;
//        cout[2][3] = 3;
//        cout[2][4] = 100;
//        cout[3][0] = 1;
//        cout[3][1] = 100;
//        cout[3][2] = 3;
//        cout[3][3] = 0;
//        cout[3][4] = 100;
//        cout[4][0] = 2;
//        cout[4][1] = 3;
//        cout[4][2] = 100;
//        cout[4][3] = 100;
//        cout[4][4] = 0;
//
//        int[] duration = new int[nbNodes];
//        long start_time = System.currentTimeMillis();
//        tsp2.searchSolution(timeLimit, nbNodes, cout, duration);
//        System.out.println(tsp2.getLowestCost());
//        for (int i = 0; i < nbNodes; i++) {
//            System.out.print(tsp2.getBestSolution(i) + " ");
//        }
//        System.out.println();
//        System.out.println(System.currentTimeMillis() - start_time);
//    }
}
