package optimization.heuristics;

import optimization.core.Problem;
import optimization.core.Solution;
import optimization.operators.Crossover;
import optimization.operators.Mutation;
import optimization.operators.Selection;
import optimization.operators.impl.BinaryTournament;

import java.util.ArrayList;
import java.util.Comparator;

public class MoGeA extends Algorithm {

    protected final int POPULATION;
    protected final int MAX_ITERATION;
    protected final int DIVERSITY_RATE;
    protected final int DIVERSITY_UPDATE_ITERATION;

    protected Crossover crossover;
    protected Mutation mutation;
    protected Selection selection;
    protected Comparator<Solution> cmp;

    public MoGeA(Problem problem, int POPULATION, int MAX_ITERATION, double DIVERSITY_RATE,
            double DIVERSITY_UPDATE_ITERATION, Crossover crossover, Mutation mutation) {
        super(problem);
        this.POPULATION = POPULATION;
        this.MAX_ITERATION = MAX_ITERATION;
        int value = (int) (DIVERSITY_RATE * POPULATION);
        this.DIVERSITY_RATE = (value >= POPULATION) ? value - 1 : value;
        this.DIVERSITY_UPDATE_ITERATION = (int) (DIVERSITY_UPDATE_ITERATION * MAX_ITERATION);

        this.crossover = crossover;
        this.mutation = mutation;
        this.cmp = Comparator.comparing(Solution::getPenalties).thenComparing(
                (a, b) -> Double.compare(a.getObjective(0).doubleValue(), b.getObjective(0).doubleValue()));
        this.selection = new BinaryTournament(POPULATION / 2, cmp);
    }

    @Override
    public void execute() {
        // Create initial population
        initial_time = System.currentTimeMillis();
        this.solutions = new ArrayList<>();
        if (initial_solutions != null) {
            for (Solution solution : initial_solutions) {
                problem.evaluate(solution);
            }
            this.solutions.addAll(initial_solutions);

        }
        for (int i = this.solutions.size(); i < POPULATION; i++) {
            Solution solution = this.problem.generateSolution();
            this.problem.evaluate(solution);

            while (Double.isNaN((Double) solution.getObjective(0))) {
                solution = this.problem.generateSolution();
                this.problem.evaluate(solution);                
            }
            this.solutions.add(solution);
        }
        for (int iteration = 1; iteration < MAX_ITERATION; iteration++) {
            System.out.printf("Iteration %3d of %3d ...\n", iteration, MAX_ITERATION);
            ArrayList<Solution> parents = this.selection.make(solutions);
            ArrayList<Solution> qt = new ArrayList<>();
            for (int j = 0; j < parents.size(); j++) {
                qt.addAll(crossover.make(parents.get(j), parents.get((j + 1 < parents.size()) ? j + 1 : 0)));
            }
           // System.out.println("After crossover");
            for (Solution solution : qt) {
                mutation.make(solution);
                problem.evaluate(solution);
            }
            //System.out.println("After mutation");
            // solutions.addAll(qt);
            for (Solution c : qt) {
                for (int i = 0; i < solutions.size(); i++) {
                    if (cmp.compare(c, solutions.get(i)) == -1) {
                        solutions.set(i, c.copy());
                        break;
                    }
                }
            }
            // solutions.sort(cmp);
                        //System.out.println("After replace");

           /* if ((int) (100 * (double) iteration / MAX_ITERATION) % DIVERSITY_UPDATE_ITERATION == 0) {
                System.out.print((double) iteration / MAX_ITERATION + " -> ");
                diversity(solutions);
            }*/
                                 //   System.out.println("diversity pass");

        }
        solutions.sort(cmp);
        end_time = System.currentTimeMillis();

    }

    private void diversity(ArrayList<Solution> solutions) {
        int rate_ = 0;

        for (int j = 0; j < solutions.size(); j++) {
            Solution tmp;
            if (Double.isNaN((Double) solutions.get(j).getObjective(0))) {
                do {
                    tmp = this.problem.generateSolution();
                    this.problem.evaluate(tmp);
                } while (Double.isNaN((Double) tmp.getObjective(0)));
                solutions.set(j, tmp);
            } else if (j > (solutions.size() - 1 - DIVERSITY_RATE) && rate_ < DIVERSITY_RATE) {
                do {
                    tmp = this.problem.generateSolution();
                    this.problem.evaluate(tmp);
                } while (Double.isNaN((Double) tmp.getObjective(0)));
                solutions.set(j, tmp);
                rate_++;
            }
            // average += solutions.get(j).getObjective(0).doubleValue();
        }

        /*
         * average /= solutions.size(); System.out.
         * printf("Diversity report : %f (average), %f (min), %d (nan), %d (replaced)\n"
         * , average, min, n, rate_);
         */
    }

    @Override
    public MoGeA copy() {
       return new MoGeA(problem, POPULATION, MAX_ITERATION, DIVERSITY_RATE, DIVERSITY_UPDATE_ITERATION, crossover, mutation);
    }

}
