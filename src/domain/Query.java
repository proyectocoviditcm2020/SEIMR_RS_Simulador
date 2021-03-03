/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.HashMap;
import optimization.problems.Covid19;

/**
 *
 * @author thinkpad
 */
public class Query {

    public enum DataType {
        RECOVERY_DATA, DEATH_DATA;
    };
    private HashMap<DataType, ArrayList<Double>> data;
    private Covid19.SerieType problemType;
    private int iterations;
    private int populationSize;
    private int executions;

    public Query(HashMap<DataType, ArrayList<Double>> data, Covid19.SerieType problemType, int iterations, int populationSize, int executions) {
        this.data = data;
        this.problemType = problemType;
        this.iterations = iterations;
        this.populationSize = populationSize;
        this.executions = executions;
    }

    public HashMap<DataType, ArrayList<Double>> getData() {
        return data;
    }

    public void setData(HashMap<DataType, ArrayList<Double>> data) {
        this.data = data;
    }

    public Covid19.SerieType getProblemType() {
        return problemType;
    }

    public void setProblemType(Covid19.SerieType problemType) {
        this.problemType = problemType;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getExecutions() {
        return executions;
    }

    public void setExecutions(int executions) {
        this.executions = executions;
    }

}
