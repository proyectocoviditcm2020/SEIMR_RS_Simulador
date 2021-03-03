package optimization.operators;

import optimization.core.Solution;

import java.util.ArrayList;

public interface Crossover {
    public ArrayList<Solution> make(Solution a, Solution b);

}
