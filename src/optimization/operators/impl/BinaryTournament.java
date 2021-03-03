package optimization.operators.impl;

import optimization.core.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import optimization.operators.Selection;

public class BinaryTournament implements Selection {
    protected int size;
    protected Comparator<Solution> solutionComparator;

    public BinaryTournament(int size, Comparator<Solution> solutionComparator) {
        this.size = size;
        this.solutionComparator = solutionComparator;
    }

    @Override
    public ArrayList<Solution> make(final ArrayList<Solution> population) {
        ArrayList<Solution> children = new ArrayList<>();
        Collections.shuffle(population);
        int index = 0;
        for (int i = 0; i < size; i++) {
            int value = solutionComparator.compare(population.get(index), population.get(index + 1 < population.size() ? index + 1 : 0));
            if (value <= 0) {
                children.add(population.get(index).copy());
            } else {
                children.add(population.get(index + 1 < population.size() ? index + 1 : 0).copy());
            }
            index = (index + 1 < population.size()) ? index + 1 : 0;
        }
        return children;
    }
}
