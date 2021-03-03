package optimization.operators;

import optimization.core.Solution;

import java.util.ArrayList;

public interface Selection {
    public ArrayList<Solution> make(final ArrayList<Solution> population);
}