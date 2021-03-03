package optimization.operators.impl;

import optimization.core.Solution;

import java.util.ArrayList;
import optimization.core.Tools;
import optimization.operators.Crossover;

public class HUXCrossover implements Crossover {
    protected Double probability;

    public HUXCrossover() {
        probability = 1.0;
    }

    public HUXCrossover(Double probability) {
        this.probability = probability;
    }


    @Override
    public ArrayList<Solution> make(Solution a, Solution b) {
        ArrayList<Solution> child = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Solution c;
            c = a.copy();
            if (Tools.getRandom().nextDouble() <= probability)
                for (int j = 0; j < a.getVariables().size(); j++) {
                    if (Tools.getRandom().nextDouble() < 0.5) {
                        c.setVariable(j, b.getVariable(j));
                    }
                }
            child.add(c);

        }
        return child;
    }
}
