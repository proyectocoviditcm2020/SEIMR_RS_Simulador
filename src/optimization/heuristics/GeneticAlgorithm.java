package optimization.heuristics;

import java.util.ArrayList;
import java.util.Comparator;

import optimization.core.Problem;
import optimization.core.Solution;
import optimization.operators.Crossover;
import optimization.operators.DominanceComparator;
import optimization.operators.Mutation;
import optimization.operators.Selection;
import optimization.operators.impl.BinaryTournament;

public class GeneticAlgorithm extends Algorithm {

    protected final int POPULATION;
    protected final int MAX_ITERATION;
    protected final int DIVERSITY_RATE;
    protected final int DIVERSITY_UPDATE_ITERATION;

    protected Crossover crossover;
    protected Mutation mutation;
    protected Selection selection;
    protected Comparator<Solution> cmp;

    public GeneticAlgorithm(Problem problem, int POPULATION, int MAX_ITERATION, double DIVERSITY_RATE,
            double DIVERSITY_UPDATE_ITERATION, Crossover crossover, Mutation mutation) {
        super(problem);
        this.POPULATION = POPULATION;
        this.MAX_ITERATION = MAX_ITERATION;
        int value = (int) (DIVERSITY_RATE * POPULATION);
        this.DIVERSITY_RATE = (value >= POPULATION) ? value - 1 : value;
        this.DIVERSITY_UPDATE_ITERATION = (int) (DIVERSITY_UPDATE_ITERATION * MAX_ITERATION);

        this.crossover = crossover;
        this.mutation = mutation;
        this.cmp = new DominanceComparator();
        this.selection = new BinaryTournament(POPULATION / 2, cmp);
    }

    @Override
    public void execute() {
        // Create initial population
        initial_time = System.currentTimeMillis();
        this.solutions = new ArrayList<>();
        for (int i = 0; i < POPULATION; i++) {
            Solution solution = this.problem.generateSolution();
            this.problem.evaluate(solution);
            while (Double.isNaN((Double) solution.getObjective(0))) {
                solution = this.problem.generateSolution();
                this.problem.evaluate(solution);
            }
            this.solutions.add(solution);
        }
        for (int iteration = 1; iteration < MAX_ITERATION; iteration++) {
            // System.out.printf("Iteration %3d of %3d ...\n", iteration, MAX_ITERATION);
            ArrayList<Solution> parents = this.selection.make(solutions);
            ArrayList<Solution> qt = new ArrayList<>();
            for (int j = 0; j < parents.size(); j++) {
                qt.addAll(crossover.make(parents.get(j), parents.get((j + 1 < parents.size()) ? j + 1 : 0)));
            }
            for (Solution solution : qt) {
                mutation.make(solution);
                problem.evaluate(solution);
            }
            solutions.addAll(qt);
            solutions.sort(cmp);
            solutions = new ArrayList<>(solutions.subList(0, POPULATION));
            /*
             * for (int i = 0; i < qt.size(); i++) { Solution child = qt.get(i); for (int j
             * = 0; j < solutions.size(); j++) { if (cmp.compare(child, solutions.get(j)) ==
             * -1) { solutions.set(j, child.copy()); break; } } }
             */
           /* if ((int) (100 * (double) iteration / MAX_ITERATION) % DIVERSITY_UPDATE_ITERATION == 0) {
                // System.out.print((double) iteration / MAX_ITERATION + " -> ");
                diversity(solutions);
            }*/
        }
        solutions.sort(cmp);
        end_time = System.currentTimeMillis();

    }

    private void diversity(ArrayList<Solution> solutions) {
        int rate_ = 0;

        for (int j = 0; j < solutions.size(); j++) {
            Solution tmp;
            if (Double.isNaN((Double) solutions.get(j).getObjective(0))
                    || Double.isNaN((Double) solutions.get(j).getObjective(1))) {
                do {
                    tmp = this.problem.generateSolution();
                    this.problem.evaluate(tmp);
                } while (Double.isNaN((Double) solutions.get(j).getObjective(0))
                        || Double.isNaN((Double) solutions.get(j).getObjective(1)));
                solutions.set(j, tmp);
            } else if (j > (solutions.size() - 1 - DIVERSITY_RATE) && rate_ < DIVERSITY_RATE) {
                do {
                    tmp = this.problem.generateSolution();
                    this.problem.evaluate(tmp);
                } while (Double.isNaN((Double) solutions.get(j).getObjective(0))
                        || Double.isNaN((Double) solutions.get(j).getObjective(1)));
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
    public GeneticAlgorithm copy() {
        GeneticAlgorithm ga = new GeneticAlgorithm(problem, POPULATION, MAX_ITERATION, DIVERSITY_RATE, DIVERSITY_UPDATE_ITERATION, crossover, mutation);
        return ga;
    }

}
