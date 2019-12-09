package model.core;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return 0;
	}

    public static void main(String[] args) {
		TSP1 tsp1 = new TSP1();
		int tpsLimite = Integer.MAX_VALUE;
		int nbSommets = 5;
		int[][] cout = new int[nbSommets][nbSommets];
		cout[0][0] = 0;
		cout[0][1] = 1;
		cout[0][2] = 2;
		cout[0][3] = 1;
		cout[0][4] = 2;
		cout[1][0] = 1;
		cout[1][1] = 0;
		cout[1][2] = 3;
		cout[1][3] = 100;
		cout[1][4] = 3;
		cout[2][0] = 2;
		cout[2][1] = 3;
		cout[2][2] = 0;
		cout[2][3] = 3;
		cout[2][4] = 100;
		cout[3][0] = 1;
		cout[3][1] = 100;
		cout[3][2] = 3;
		cout[3][3] = 0;
		cout[3][4] = 100;
		cout[4][0] = 2;
		cout[4][1] = 3;
		cout[4][2] = 100;
		cout[4][3] = 100;
		cout[4][4] = 0;

		int[] duree = new int[nbSommets];
		long start_time = System.currentTimeMillis();
		tsp1.searchSolution(tpsLimite, nbSommets, cout, duree);
		System.out.println(tsp1.getCoutMeilleureSolution());
		for (int i = 0; i < nbSommets; i++) {
			System.out.print(tsp1.getBestSolution(i) + " ");
		}
		System.out.println();
		System.out.println(System.currentTimeMillis() - start_time);
	}
}
