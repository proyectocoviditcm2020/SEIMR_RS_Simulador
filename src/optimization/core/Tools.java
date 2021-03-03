package optimization.core;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Tools {
    public static Random random = new Random();

    public static Random getRandom() {
        return random;
    }

    public static double EUCLIDEAN_DISTANCE(Double[] p, Double[] q) {
        double rs = 0;
        for (int i = 0; i < Math.min(p.length, q.length); i++) {
            rs += Math.pow((q[i] - p[i]), 2);
        }
        return Math.sqrt(rs);
    }

    public static double MANHATTAN_DISTANCE(Double[] p, Double[] q) {
        double rs = 0;
        for (int i = 0; i < Math.min(p.length, q.length); i++) {
            rs += Math.abs((q[i] - p[i]));
        }
        return rs;
    }

    public static double CANBERRA_DISTANCE(Double[] p, Double[] q) {
        double rs = 0;
        for (int i = 0; i < Math.min(p.length, q.length); i++) {
            rs += Math.abs((q[i] - p[i])) / (Math.abs(p[i]) + Math.abs(q[i]));
        }
        return rs;
    }

    public static double CHEBYSHEV_DISTANCE(Double[] p, Double[] q) {
        double rs = 0;
        for (int i = 0; i < Math.min(p.length, q.length); i++) {
            rs = Math.max(rs, Math.abs(p[i] - q[i]));
        }
        return rs;
    }

    /**
     * Mean Squared Error
     * 
     * @param observed  vector values of the variable beign predicted
     * @param predicted values.
     * @return
     */
    public static double MSE(Double[] observed, Double[] predicted) {
        double rs = 0;
        for (int i = 0; i < Math.min(observed.length, predicted.length); i++) {
            rs += Math.pow(observed[i] - predicted[i], 2);
        }
        return rs / Math.min(observed.length, predicted.length);
    }

    /**
     * Mean Absolute Error
     * 
     * @param observed  vector values of the variable beign predicted
     * @param predicted real value.
     * @return
     */
    public static double MAE(Double[] observed, Double[] predicted) {
        double rs = 0;
        for (int i = 0; i < Math.min(observed.length, predicted.length); i++) {
            rs += Math.abs(observed[i] - predicted[i]);
        }
        return rs / Math.min(observed.length, predicted.length);
    }

    public static void SOLUTIONS_TO_FILE(Problem problem, ArrayList<Solution> front, String path) throws IOException {
        if (!path.contains(".csv"))
            path += ".csv";
        Table table = Table.create(problem.getName());
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            DoubleColumn column = DoubleColumn.create("Var-" + (i + 1));
            for (Solution solution : front)
                column.append(solution.getVariable(i));

            table.addColumns(column);
        }
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            DoubleColumn column = DoubleColumn.create("F-" + (i + 1));
            for (Solution solution : front)
                column.append(solution.getObjective(i));
            table.addColumns(column);
        }
        for (int i = 0; i < problem.getNumberOfResources(); i++) {
            DoubleColumn column = DoubleColumn.create("Res-" + (i + 1));
            for (Solution solution : front)
                column.append(solution.getResource(i));

            table.addColumns(column);
        }
        DoubleColumn column = DoubleColumn.create("Penalties");
        for (Solution solution : front)
            column.append(solution.getPenalties());

        DoubleColumn column_rank = DoubleColumn.create("Rank");
        for (Solution solution : front)
            column_rank.append(solution.getRank());

        table.addColumns(column, column_rank);
        table.write().csv(path);
    }
}
