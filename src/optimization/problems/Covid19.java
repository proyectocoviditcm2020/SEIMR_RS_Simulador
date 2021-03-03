package optimization.problems;

import domain.ModelSEIMRRS2;
import java.util.ArrayList;
import optimization.core.Problem;
import optimization.core.Solution;
import optimization.core.Tools;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Random;

public class Covid19 extends Problem {

    public enum SerieType {
        RECOVERY, DEATHS, DEATHS_RECOVERY;
    };

    protected final int region;
    protected final int status;
    protected final int scenario;

    protected ModelSEIMRRS2 model;
    protected Table table;
    protected final int start;
    protected final int column_target;
    protected final Double[] target;
    protected Double[] points_evaluated;
    protected final SerieType serieType;
    protected Random rand = new Random();
    private double[] delta = {0, 1};
    private double[] etha = {0, 100};
    private double[] phi = {0, 100};

    public Covid19(ModelSEIMRRS2 model, int region, int status, int scenario, Table table, int start, int column_target, SerieType type) {
        super(4 + 4 + 1, 1, "Covid19 Mono-Objective " + type.toString());
        this.serieType = type;
        if (serieType == SerieType.RECOVERY) {
            this.numberOfVariables = 4 + 4 + 4 + 1;
        }
        this.model = model;
        this.start = start;
        this.column_target = column_target;
        this.table = table;
        this.region = region;
        this.status = status;
        this.scenario = scenario;
        this.numberOfResources = this.numberOfVariables;
        Column<?> column = table.column(column_target);
        this.target = new Double[column.size()];
        for (int i = 0; i < column.size(); i++) {
            target[i] = Double.parseDouble(column.getString(i));
        }

    }

    @Override
    public Solution generateSolution() {
        Solution solution = new Solution(this.numberOfVariables, this.numberOfObjectives);
        for (int i = 0; i < this.numberOfVariables; i++) {
            solution.setVariable(i, rand.nextDouble());
        }
        return solution;
    }

    @Override
    public void evaluate(Solution solution) {
        // Evaluate constraints

        double penalties = 0.0;
        ArrayList<Number> resource = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++) {
            Double x = transform(i, solution.getVariable(i).doubleValue());
            resource.add(x);
            if (validate(i, x)) {
                penalties += 1;
            }
        }
        solution.setPenalties(penalties);
        //
        double[] gz = new double[4];
        double[] gs = new double[4];
        double[] phix = new double[4];

        for (int i = 0, j = 0, k = 0; i < this.numberOfVariables - 1; i++) {
            if (i < 4) {
                gz[i] = resource.get(i).doubleValue();
            } else if (i < 8) {
                gs[j++] = resource.get(i).doubleValue();
            } else {
                phix[k++] = resource.get(i).doubleValue();
            }
        }
        solution.setResources(resource);
        if (serieType == SerieType.RECOVERY) {
            model.setMIUN(resource.get(this.numberOfVariables - 1).doubleValue());
            model.setPHIXB(phix);
        }
        model.setDELXB(gz);

        model.setETAXB(gs);
        if (serieType == SerieType.DEATHS) {
            model.setMIUUB(resource.get(this.numberOfVariables - 1).doubleValue());
        }

        model.setEscenario(scenario);
        model.condicionesIniciales();

        model.simulaModelo();

        Double[] p = points(serieType);

        points_evaluated = new Double[target.length];

        for (int i = start, j = 0; j < points_evaluated.length; i++, j++) {
            points_evaluated[j] = p[i];
        }

        // solution.setObjective(0, Tools.EUCLIDEAN_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.MANHATTAN_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.CANBERRA_DISTANCE(points_evaluated, target));
        // solution.setObjective(0, Tools.CHEBYSHEV_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.MSE(points_evalauted, target));
        solution.setObjective(0, Tools.MAE(points_evaluated, target));

        if (Double.isNaN(solution.getObjective(0).doubleValue())) {
            solution.setPenalties(solution.getPenalties() + 1);
        }

    }

    protected Double[] points(SerieType type) {
        Double[] points = new Double[model.getT().length];
        for (int i = 0; i < model.getT().length; i++) {
            if (status < model.getNEstratos()) {
                if (type == SerieType.DEATHS) {
                    points[i] = model.getD()[region][status][i] * model.getPobTotalRS(region, status);
                } else {
                    points[i] = model.getR()[region][status][i] * model.getPobTotalRS(region, status);
                }
            } else {
                if (type == SerieType.DEATHS) {
                    if (region == model.getNRegiones()) {
                        points[i] = model.sumaElementosEstadoWPobTodo(model.getD(), i);
                    } else {
                        points[i] = model.sumaElementosEstadoWPob(model.getD(), region, i);
                    }
                } else {
                    if (region == model.getNRegiones()) {
                        points[i] = model.sumaElementosEstadoWPobTodo(model.getR(), i);
                    } else {
                        points[i] = model.sumaElementosEstadoWPob(model.getR(), region, i);
                    }
                }

            }
        }
        return points;
    }

    private Double generate_value(int var) {

        if (serieType == SerieType.DEATHS) {
            switch (var) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return (Tools.random.nextDouble() - this.delta[0]) / (this.delta[1] - this.delta[0]);
                case 4:
                case 5:
                case 6:
                case 7:
                    return (Tools.random.nextDouble() - etha[0]) / (etha[1] - etha[0]);
                case 8:
                    return Tools.random.nextDouble();
            }
        } else {
            switch (var) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return (Tools.random.nextDouble() - this.delta[0]) / (this.delta[1] - this.delta[0]);
                case 4:
                case 5:
                case 6:
                case 7:
                    return (Tools.random.nextDouble() - etha[0]) / (etha[1] - etha[0]);
                case 8:
                case 9:
                case 10:
                case 11:
                    return (Tools.random.nextDouble() - phi[0]) / (phi[1] - phi[0]);
                case 12:
                    return Tools.random.nextDouble();
            }
        }
        return Tools.random.nextDouble();
    }

    public boolean validate(int var, Double x) {
        if (serieType == SerieType.DEATHS) {
            if (var <= 3) {
                return x >= delta[0] && x <= delta[1];
            }
            if (var <= 7) {
                return x >= etha[0] && x <= etha[1];
            }
            return x >= 0 && x <= 1;
        } else {
            if (var <= 3) {
                return x >= delta[0] && x <= delta[1];
            }
            if (var <= 7) {
                return x >= etha[0] && x <= etha[1];
            }
            if (var <= 11) {
                return x >= phi[0] && x <= phi[1];

            }
            return x >= 0 && x <= 1;
        }
    }

    public Double transform(int var, Double z) {
        if (serieType == SerieType.DEATHS) {
            switch (var) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return (z * (delta[1] - delta[0])) + delta[0];
                case 4:
                case 5:
                case 6:
                case 7:
                    return (z * (etha[1] - etha[0])) + etha[0];
                case 8:
                    return (z * (1 - 0)) + 0;
            }
        } else if (serieType == SerieType.RECOVERY) {
            switch (var) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return (z * (delta[1] - delta[0])) + delta[0];
                case 4:
                case 5:
                case 6:
                case 7:
                    return (z * (etha[1] - etha[0])) + etha[0];
                case 8:
                case 9:
                case 10:
                case 11:
                    return (z * (phi[1] - phi[0])) + phi[0];
                case 12:
                    return (z * (1 - 0)) + 0;
            }
        } else {
            switch (var) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return (z * (delta[1] - delta[0])) + delta[0];
                case 4:
                case 5:
                case 6:
                case 7:
                    return (z * (etha[1] - etha[0])) + etha[0];
                case 8:
                case 9:
                case 10:
                case 11:
                    return (z * (phi[1] - phi[0])) + phi[0];
                case 12:
                case 13:
                    return (z * (1 - 0)) + 0;
            }
        }
        return z;
    }

    public Double[] getPoints_evaluated() {
        return points_evaluated;
    }

    public Double[] getTarget() {
        return target;
    }

    public ModelSEIMRRS2 getModel() {
        return model;
    }

}
