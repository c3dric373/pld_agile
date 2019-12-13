package model.core;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq implements Iterator<Integer> {

    /**
     * Integer table of candidates.
     */
    private Integer[] candidates;

    /**
     * Number of candidates.
     */
    private int nbCandidates;

    /**
     * Create an iterator which can iterate the permutation
     * of the nodes not seen.
     *
     * @param notSeen     table of nodes not seen yet
     * @param currentNode current node
     */
    public IteratorSeq(final Collection<Integer> notSeen,
                       final int currentNode) {
        this.candidates = new Integer[notSeen.size()];
        nbCandidates = 0;
        for (Integer s : notSeen) {
            candidates[nbCandidates++] = s;
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
}
