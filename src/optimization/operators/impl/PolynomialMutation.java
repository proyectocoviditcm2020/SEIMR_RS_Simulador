package optimization.operators.impl;

import optimization.core.Solution;
import optimization.core.Tools;
import optimization.operators.Mutation;

public class PolynomialMutation implements Mutation {
    private static final double DEFAULT_PROBABILITY = 0.01;
    private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0;
    private double distributionIndex;
    private double mutationProbability;

    /** Constructor */
    public PolynomialMutation() {
        this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX);
    }

    public PolynomialMutation(double distributionIndex, double mutationProbability) {
        this.distributionIndex = distributionIndex;
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void make(Solution solution) {
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy;

        for (int i = 0; i < solution.getVariables().size(); i++) {
            if (Tools.random.nextDouble() <= mutationProbability) {
                y = (double) solution.getVariable(i);
                yl = 0;
                yu = 1;
                if (yl == yu) {
                    y = yl;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = Tools.random.nextDouble();
                    mutPow = 1.0 / (distributionIndex + 1.0);
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);
                    y = repairSolutionVariableValue(y, yl, yu);
                }
                solution.setVariable(i, y);
            }
        }
    }

    private double repairSolutionVariableValue(double v, double lowerBound, double upperBound) {
        if (v < lowerBound || v > upperBound)
            return Tools.random.doubles(lowerBound, upperBound).findFirst().getAsDouble();
        return v;
    }

}
