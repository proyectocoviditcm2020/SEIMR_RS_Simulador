package optimization.problems;

import domain.Cuarentena;
import domain.ModelSEIMRRS2;
import java.util.ArrayList;

import optimization.core.Problem;
import optimization.core.Solution;
import optimization.core.Tools;
import tech.tablesaw.api.Table;

/**
 *
 *
 */
public class Covid19BiObjective extends Problem {

    protected final int status;

    protected ModelSEIMRRS2 model;
    protected Table table;

    public Covid19BiObjective(ModelSEIMRRS2 model, int status) {
        super(model.getCuarentList().size(), 2, "Covid19 Bi-Objective");
        System.out.println(getClass() + " " + model.getCuarentList().size());
        this.model = model;
        this.status = status;
        this.numberOfResources = 3;

    }

    /**
     * x_1 = intensidad de la cuarentena -> A[0] x_2 - tampico x_3 - madero x_4
     * - altamira min muertes -> f_1 = sum sd[n] min total de desempleo -> f_2 =
     * meses * intensidad de la cuarentena
     */
    @Override
    public void evaluate(Solution solution) {
        ArrayList<Number> resources = new ArrayList<>();
        ArrayList<Double> deathByQuarentine = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // model.setQuarentinePercentage(solution.getVariable(0).doubleValue());
            for (int j = 0; j < model.getCuarentList().size(); j++) {
                Cuarentena current = model.getCuarentList().get(j);
                current.setPorcentaje(solution.getVariable(j).doubleValue());
            }
            model.condicionesIniciales();
            model.simulaModelo();
            resources.add(getDeaths(i).stream().mapToDouble(Double::doubleValue).max().getAsDouble());
        }
        double unemployed = 0;
        for (int i = 0; i < solution.getVariables().size(); i++) {
            double tmp = f2(solution.getVariable(i).doubleValue());
            unemployed += tmp;
            if (tmp < 0) {
                solution.setPenalties(solution.getPenalties() + 1);
            }
        }
        solution.setResources(resources);
        solution.setObjective(0, resources.stream().reduce(0, (a, b) -> a.doubleValue() + b.doubleValue()));
        solution.setObjective(1, unemployed);
        if (Double.isNaN(solution.getObjective(0).doubleValue())) {
            solution.setPenalties(solution.getPenalties() + 1);
        }
        if (Double.isNaN(unemployed) || unemployed < 0) {
            solution.setPenalties(solution.getPenalties() + Math.abs(unemployed));
                    solution.setObjective(1, Math.abs(unemployed)*100);

        }

    }

    public void setGZeta(double v[]) {
        model.setgZeta(v);
    }

    public void setGSigma(double v[]) {
        model.setgSigma(v);
    }

    public void setMiuSigma(double v) {
        model.setMiuSigma(v);
    }

    public void setMiuN(double v) {
        model.setMIUN(v);
    }

    public double f2(double x) {
        return -1930.4415 + 9655.4629 * x - 160601.73 * Math.pow(Math.max(x - 0.1, 0), 3)
                + 321203.47 * Math.pow(Math.max(x - 0.2, 0), 3) - 160601.73 * Math.pow(Math.max(x - 0.3, 0), 3);
    }

    @Override
    public Solution generateSolution() {
        Solution solution = new Solution(this.numberOfVariables, this.numberOfObjectives);
        for (int i = 0; i < this.numberOfVariables; i++) {
            solution.setVariable(i, Tools.random.nextDouble()); // quarentine percentage
        }

        return solution;
    }

    protected ArrayList<Double> getDeaths(int region) {
        ArrayList<Double> points = new ArrayList<>();
        for (int i = 0; i < model.getT().length; i++) {
            if (status < 6) {
                points.add(model.getD()[region][status][i] * model.getPobTotalRS(region, status));
            } else {
                points.add(model.sumaElementosEstadoWPob(model.getD(), region, i));
            }
        }
        return points;
    }

}
