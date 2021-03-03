package optimization.operators.impl;

import optimization.core.Solution;
import optimization.core.Tools;
import optimization.operators.Mutation;

public class SimpleMutation implements Mutation {
    protected double probability;

    public SimpleMutation(double probability) {
        this.probability = probability;
    }

    @Override
    public void make(Solution solution) {
        for (int i = 0; i < solution.getVariables().size(); i++) {
            if (Tools.random.nextDouble() <= probability) {
                solution.setVariable(i, Tools.random.nextDouble());
            }
        }
    }
}
