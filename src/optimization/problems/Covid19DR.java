package optimization.problems;

import domain.ModelSEIMRRS2;
import java.util.ArrayList;

import optimization.core.Solution;
import optimization.core.Tools;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * Vector solucion { Deltx , Etax, Phix, Miun, Miuub}
 *
 * @author thinkpad
 */
public class Covid19DR extends Covid19 {

    protected Double[] data_recovery;
    protected Double[] points_evaluated_r;

    public Covid19DR(ModelSEIMRRS2 model, int region, int status, int scenario, Table table, int start, int column_deaths,
            int column_recovery) {
        super(model, region, status, scenario, table, start, column_deaths, SerieType.DEATHS_RECOVERY);
        this.numberOfVariables = 4 + 4 + 4 + 1 + 1;
        Column<?> column = table.column(column_recovery);
        this.data_recovery = new Double[column.size()];
        for (int i = 0; i < column.size(); i++) {
            data_recovery[i] = Double.parseDouble(column.getString(i));
        }
        this.setName("Covid19 Mono-Objective DR");
    }

    @Override
    public void evaluate(Solution solution) {
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

        for (int i = 0, j = 0, k = 0; i < this.numberOfVariables - 2; i++) {
            if (i < 4) {
                gz[i] = resource.get(i).doubleValue();
            } else if (i < 8) {
                gs[j++] = resource.get(i).doubleValue();
            } else {
                phix[k++] = resource.get(i).doubleValue();
            }
        }
        solution.setResources(resource);
        model.setMIUN(resource.get(this.numberOfVariables - 2).doubleValue());
        model.setPHIXB(phix);

        model.setDELXB(gz);

        model.setETAXB(gs);
        model.setMIUUB(resource.get(this.numberOfVariables - 1).doubleValue());

        model.setEscenario(scenario);
        model.condicionesIniciales();

        model.simulaModelo();

        Double[] pd = points(SerieType.DEATHS);
        Double[] pr = points(SerieType.RECOVERY);

        points_evaluated = new Double[target.length];
        points_evaluated_r = new Double[target.length];

        for (int i = start, j = 0; j < points_evaluated.length; i++, j++) {
            points_evaluated[j] = pd[i];
            points_evaluated_r[j] = pr[i];

        }
        // solution.setObjective(0, Tools.EUCLIDEAN_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.MANHATTAN_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.CANBERRA_DISTANCE(points_evaluated, target));
        // solution.setObjective(0, Tools.CHEBYSHEV_DISTANCE(points_evalauted, target));
        // solution.setObjective(0, Tools.MSE(points_evalauted, target));
        solution.setObjective(0, Tools.MAE(points_evaluated, target) + Tools.MAE(points_evaluated_r, data_recovery));

        if (Double.isNaN(solution.getObjective(0).doubleValue())) {
            solution.setPenalties(solution.getPenalties() + 1);
        }

    }

    public Double[] getPoints_evaluated_r() {
        return points_evaluated_r;
    }

    public Double[] getData_recovery() {
        return data_recovery;
    }
}
