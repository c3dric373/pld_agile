package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer currentNode, ArrayList<Integer> notSeen, int[][] cost, int[] duration) {
        return new IteratorSeq(notSeen, currentNode);
    }

    @Override
    protected int bound(Integer currentNode, ArrayList<Integer> notSeen, int[][] cost, int[] duration) {
        return 0;
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
