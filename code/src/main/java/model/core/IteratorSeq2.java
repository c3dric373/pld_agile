package model.core;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq2 implements Iterator<Integer> {

    private Integer[] candidates;
    private int nbCandidates;

    /**
     * Create an iterator which can iterate the permutation of the nodes not seen
     *
     * @param notSeen     table of nodes not seen yet
     * @param currentNode current node
     * @param cost        cost[i][j] = the duration from i to j, with 0 <= i < nbNodes and 0 <= j < nbNodes
     */
    public IteratorSeq2(Collection<Integer> notSeen, int currentNode, int[][] cost) {
        int nb = cost.length / 2;
        boolean pickUp = true;
        if (currentNode > nb) {
            // if current node is a delivery point, we have to verify whether its pick up point is visited or not
            for (Integer i : notSeen) {
                if (i == currentNode - nb) {
                    pickUp = false;
                    break;
                }
            }
        }
        if (!pickUp) {
            // if current node is a delivery point and its pick up point hasn't been visited, we create an empty iterator
            this.candidates = new Integer[0];
            this.nbCandidates = 0;
        } else {
            // if not, we create a normal iterator
            this.candidates = new Integer[notSeen.size()];
            nbCandidates = 0;
            for (Integer s : notSeen) {
                candidates[nbCandidates++] = s;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nbCandidates > 0;
    }

    @Override
    public Integer next() {
        return candidates[--nbCandidates];
    }

    @Override
    public void remove() {
    }

}
