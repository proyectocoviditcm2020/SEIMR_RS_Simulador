package optimization.operators;

import java.util.Comparator;

import optimization.core.Solution;

public class DominanceComparator implements Comparator<Solution> {

    @Override
    public int compare(Solution a, Solution b) {
        int value = Double.compare(a.getPenalties(), b.getPenalties());
        if (value != 0)
            return value;
        boolean dom_a = false, dom_b = false;
        for (int i = 0; i < b.getObjectives().size() && (!dom_a || !dom_b); i++) {
            value = Double.compare((Double) a.getObjective(i), (Double) b.getObjective(i));
            if (value == -1) {
                dom_a = true;
            }
            if (value == 1) {
                dom_b = true;
            }
        }
        return (dom_a == dom_b) ? 0 : (dom_a) ? -1 : 1;

    }

}
