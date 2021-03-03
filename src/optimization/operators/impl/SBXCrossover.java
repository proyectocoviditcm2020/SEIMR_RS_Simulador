package optimization.operators.impl;

import java.util.ArrayList;

import optimization.core.Solution;
import optimization.core.Tools;
import optimization.operators.Crossover;

public class SBXCrossover implements Crossover {
    private static final double EPS = 1.0e-14;

    private double distributionIndex;
    private double crossoverProbability;

    /** Constructor */
    public SBXCrossover(double crossoverProbability, double distributionIndex) {
        // this (crossoverProbability, distributionIndex, new
        // RepairDoubleSolutionWithBoundValue()) ;
        this.distributionIndex = distributionIndex;
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public ArrayList<Solution> make(Solution a, Solution b) {
        ArrayList<Solution> offspring = new ArrayList<>(2);

        offspring.add(a.copy());
        offspring.add(b.copy());

        int i;
        double rand;
        double y1, y2, lowerBound, upperBound;
        double c1, c2;
        double alpha, beta, betaq;
        double valueX1, valueX2;

        if (Tools.random.nextDouble() <= crossoverProbability) {
            for (i = 0; i < a.getVariables().size(); i++) {
                valueX1 = (double) a.getVariable(i);
                valueX2 = (double) b.getVariable(i);
                if (Tools.random.nextDouble() <= 0.5) {
                    if (Math.abs(valueX1 - valueX2) > EPS) {
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        lowerBound = 0;
                        upperBound = 1;

                        rand = Tools.random.nextDouble();
                        beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

                        beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                        c1 = repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = repairSolutionVariableValue(c2, lowerBound, upperBound);

                        if (Tools.random.nextDouble() <= 0.5) {
                            offspring.get(0).setVariable(i, c2);
                            offspring.get(1).setVariable(i, c1);
                        } else {
                            offspring.get(0).setVariable(i, c1);
                            offspring.get(1).setVariable(i, c2);
                        }
                    } else {
                        offspring.get(0).setVariable(i, valueX1);
                        offspring.get(1).setVariable(i, valueX2);
                    }
                } else {
                    offspring.get(0).setVariable(i, valueX2);
                    offspring.get(1).setVariable(i, valueX1);
                }
            }
        }

        return offspring;
    }

    private double repairSolutionVariableValue(double v, double lowerBound, double upperBound) {
        if (v < lowerBound || v > upperBound)
            return Tools.random.doubles(lowerBound, upperBound).findFirst().getAsDouble();
        return v;
    }

}
