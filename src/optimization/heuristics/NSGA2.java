package optimization.heuristics;

import java.util.ArrayList;
import java.util.Comparator;

import optimization.core.CrowdingDistance;
import optimization.core.FastNonDominatedSort;
import optimization.core.Problem;
import optimization.core.Solution;
import optimization.operators.Crossover;
import optimization.operators.Mutation;
import optimization.operators.Selection;

public class NSGA2 extends Algorithm {

    protected final int _POPULATION;
    protected final int _MAX_ITERATION;
    protected Crossover crossover;
    protected Mutation mutation;
    protected Selection selection;
    protected FastNonDominatedSort<Solution> fnds;
    protected CrowdingDistance<Solution> cd;

    @Override
    public void execute() {
        // Create initial population
        initial_time = System.currentTimeMillis();
        this.solutions = new ArrayList<>();
        //System.out.println("Init population");
        for (int i = 0; i < _POPULATION; i++) {
            Solution solution = this.problem.generateSolution();
            this.problem.evaluate(solution);
            this.solutions.add(solution);
        }

        for (int iteration = 1; iteration < _MAX_ITERATION; iteration++) {
            //System.out.printf("Iteration %3d of %3d ...\n", iteration, _MAX_ITERATION);
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
            ArrayList<Solution> Pt = new ArrayList<>();
            fnds.computeRanking(solutions);
            int index = 0, frontIndex = 0;
            while (index < _POPULATION && frontIndex < fnds.getNumberOfSubFronts()) {
                ArrayList<Solution> front = fnds.getSubFront(frontIndex++);
                if (front.size() + index < _POPULATION) {
                    for (int i = 0; i < front.size(); i++, index++) {
                        Pt.add((Solution) front.get(i).copy());
                    }

                } else {
                    cd.compute(front);
                    front = cd.sort(front);
                    for (int i = 0; i < front.size() && index < _POPULATION; i++, index++) {
                        Pt.add((Solution) front.get(i).copy());
                    }
                }
            }
            solutions = Pt;
        }
        fnds.computeRanking(solutions);
        solutions.sort((a, b) -> a.getRank().compareTo(b.getRank()));
        end_time = System.currentTimeMillis();

    }

    public NSGA2(Problem problem, int _POPULATION, int _MAX_ITERATION, Crossover crossover, Mutation mutation,
            Selection selection) {
        super(problem);
        this._POPULATION = _POPULATION;
        this._MAX_ITERATION = _MAX_ITERATION;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
        this.fnds = new FastNonDominatedSort<>();
        this.cd = new CrowdingDistance<>();
    }

    @Override
    public NSGA2 copy() {
        return new NSGA2(problem, _POPULATION, _MAX_ITERATION, crossover, mutation, selection);
    }

}
