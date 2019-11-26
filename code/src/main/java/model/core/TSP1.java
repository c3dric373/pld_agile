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

//    public static void main(String[] args) {
//		TSP1 tsp1 = new TSP1();
//		int tpsLimite = Integer.MAX_VALUE;
//		int nbSommets = 4;
//		int[][] cout = new int[4][4];
//		cout[0][0] = 0;
//		cout[0][1] = 20;
//		cout[0][2] = 42;
//		cout[0][3] = 35;
//		cout[1][0] = 20;
//		cout[1][1] = 0;
//		cout[1][2] = 30;
//		cout[1][3] = 34;
//		cout[2][0] = 42;
//		cout[2][1] = 30;
//		cout[2][2] = 0;
//		cout[2][3] = 12;
//		cout[3][0] = 35;
//		cout[3][1] = 34;
//		cout[3][2] = 12;
//		cout[3][3] = 0;
//		int[] duree = new int[4];
//		tsp1.chercheSolution(tpsLimite, nbSommets, cout, duree);
//		System.out.println(tsp1.getCoutMeilleureSolution());
//		for (int i = 0; i < 4; i++) {
//			System.out.print(tsp1.getMeilleureSolution(i) + " ");
//		}
//	}
}
