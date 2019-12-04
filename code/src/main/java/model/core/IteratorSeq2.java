package model.core;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq2 implements Iterator<Integer> {

    private Integer[] candidats;
    private int nbCandidats;

    /**
     * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
     * @param nonVus
     * @param sommetCrt
     */
    public IteratorSeq2(Collection<Integer> nonVus, int sommetCrt, int[][] cout) {
        int nb = cout.length / 2;
        boolean pickUp = true;
        if (sommetCrt > nb) {
            for (Integer i : nonVus) {
                if (i == sommetCrt - nb) {
                    pickUp = false;
                    break;
                }
            }
        }
        if (!pickUp) {
            this.candidats = new Integer[0];
            this.nbCandidats = 0;
        } else {
            this.candidats = new Integer[nonVus.size()];
            nbCandidats = 0;
            for (Integer s : nonVus) {
                candidats[nbCandidats++] = s;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nbCandidats > 0;
    }

    @Override
    public Integer next() {
        return candidats[--nbCandidats];
    }

    @Override
    public void remove() {}

}
