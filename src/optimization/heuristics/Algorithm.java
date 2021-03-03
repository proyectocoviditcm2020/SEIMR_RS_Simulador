package optimization.heuristics;

import optimization.core.Problem;
import optimization.core.Solution;

import java.util.ArrayList;

public abstract class Algorithm {
    protected Problem problem;
    protected ArrayList<Solution> solutions;
    protected ArrayList<Solution> initial_solutions;
    protected long initial_time;
    protected long end_time;

    public Algorithm(Problem problem) {
        this.problem = problem;
    }

    public abstract void execute();

    public Problem getProblem() {
        return problem;
    }

    public ArrayList<Solution> getSolutions() {
        return solutions;
    }

    /**
     * Computing time in seconds
     * 
     * @return time
     */
    public double getComputingTime() {
        return (end_time - initial_time) / 1000.0;
    }

    public ArrayList<Solution> getInitial_solutions() {
        return initial_solutions;
    }

    public void setInitial_solutions(ArrayList<Solution> initial_solutions) {
        this.initial_solutions = initial_solutions;
    }
    public abstract Algorithm copy();
}