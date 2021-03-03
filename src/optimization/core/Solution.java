package optimization.core;

import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
    protected ArrayList<Number> variables;
    protected ArrayList<Number> objectives;
    protected ArrayList<Number> resources;
    protected HashMap<String, Object> attributes;
    protected double penalties;
    protected Integer rank = 0;

    public Solution(int variables, int objectives) {
        this.variables = new ArrayList<>();
        this.objectives = new ArrayList<>();
        this.resources = new ArrayList<>();
        attributes = new HashMap<>();

        for (int i = 0; i < variables; i++) {
            this.variables.add(null);
        }
        for (int i = 0; i < objectives; i++) {
            this.objectives.add(null);
        }
        this.penalties = 0;

    }

    public Solution(Solution solution) {
        this.variables = new ArrayList<>();
        this.objectives = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.rank = solution.rank;
        attributes = new HashMap<>();

        for (int i = 0; i < solution.variables.size(); i++) {
            variables.add(solution.getVariable(i));
        }

        for (int i = 0; i < solution.objectives.size(); i++) {
            objectives.add(solution.getObjective(i));
        }

        for (int i = 0; i < solution.resources.size(); i++) {
            resources.add(solution.getResource(i));
        }
        this.penalties = solution.penalties;

    }

    public Number getVariable(int index) {
        return this.variables.get(index);
    }

    public void setVariable(int index, Number element) {
        this.variables.set(index, element);
    }

    public Number getObjective(int index) {
        return this.objectives.get(index);
    }

    public void setObjective(int index, Number element) {
        this.objectives.set(index, element);
    }

    public ArrayList<Number> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Number> variables) {
        this.variables = variables;
    }

    public ArrayList<Number> getObjectives() {
        return objectives;
    }

    public void setObjectives(ArrayList<Number> objectives) {
        this.objectives = objectives;
    }

    public ArrayList<Number> getResource() {
        return resources;
    }

    public void setResources(ArrayList<Number> resource) {
        this.resources = resource;
    }

    public Number getResource(int index) {
        return this.resources.get(index);
    }

    public void setResource(int index, Number element) {
        this.resources.set(index, element);
    }

    public double getPenalties() {
        return penalties;
    }

    public void setPenalties(Double penalties) {
        this.penalties = penalties;
    }

    public Solution copy() {
        return new Solution(this);
    }

    @Override
    public String toString() {
        return String.format("%s * %s * %s * %f %3d", (variables.isEmpty())? "-":variables, objectives, resources, penalties, rank)
                .replaceAll("\\[", "").replaceAll("\\]", "");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((variables == null) ? 0 : variables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Solution other = (Solution) obj;
        if (variables == null) {
            if (other.variables != null)
                return false;
        } else if (!variables.equals(other.variables))
            return false;
        return true;
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}