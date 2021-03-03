package optimization.core;

public abstract class Problem {
    protected String name;
    protected int numberOfVariables;
    protected int numberOfObjectives;
    protected int numberOfResources;

    public Problem(int numberOfVariables, int numberOfObjectives, String name) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfObjectives = numberOfObjectives;
        this.numberOfResources = 0;
        this.name = name;
    }

    public abstract Solution generateSolution();

    public abstract void evaluate(Solution solution);

    public String getName() {
        return name;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }

    public void setNumberOfResources(int numberOfResources) {
        this.numberOfResources = numberOfResources;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Problem{" + "name='" + name + '\'' + ", numberOfVariables=" + numberOfVariables
                + ", numberOfObjectives=" + numberOfObjectives + ", numberOfResources=" + numberOfResources + '}';
    }
}
