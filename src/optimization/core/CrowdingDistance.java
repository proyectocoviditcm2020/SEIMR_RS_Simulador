package optimization.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CrowdingDistance<S extends Solution> {
    public void compute(ArrayList<S> solutions) {
        if (solutions.size() == 0) {
            return;
        } else if (solutions.size() == 1) {
            solutions.get(0).getAttributes().put(getAttributeKey(), Double.POSITIVE_INFINITY);
            return;
        } else if (solutions.size() == 2) {
            solutions.get(0).getAttributes().put(getAttributeKey(), Double.POSITIVE_INFINITY);
            solutions.get(1).getAttributes().put(getAttributeKey(), Double.POSITIVE_INFINITY);
            return;
        }
        for (S solution : solutions) {
            solution.getAttributes().put(getAttributeKey(), 0);
        }
        int numOfObj = solutions.get(0).getObjectives().size();
        Double MaxValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < numOfObj; i++) {
            final int ii = i;
            Collections.sort(solutions, new Comparator<Solution>() {

                @Override
                public int compare(Solution a, Solution b) {
                    return Double.compare(a.getObjective(ii).doubleValue(), b.getObjective(ii).doubleValue());
                }

            });
            solutions.get(0).getAttributes().put(getAttributeKey(), MaxValue);
            solutions.get(solutions.size() - 1).getAttributes().put(getAttributeKey(), MaxValue);
            double min = solutions.get(0).getObjectives().get(i).doubleValue();
            double max = solutions.get(solutions.size() - 1).getObjectives().get(i).doubleValue();
            double distance = 0;
            for (int j = 1; j < solutions.size() - 1; j++) {
                distance = solutions.get(j + 1).getObjectives().get(i).doubleValue()
                        - solutions.get(j - 1).getObjectives().get(i).doubleValue();
                distance  /= (max - min);
                distance += ((Number) solutions.get(j).getAttributes().getOrDefault(getAttributeKey(), 0.0)).doubleValue();
                solutions.get(j).getAttributes().put(getAttributeKey(), distance);
            }
        }
    }

    public String getAttributeKey() {
        return getClass().getName();
    }

    public ArrayList<S> sort(ArrayList<S> solutions) {
        solutions.sort(new CrowdingDistanceComparator().reversed());
        return solutions;
    }

    public class CrowdingDistanceComparator implements Comparator<S> {

        @Override
        public int compare(S a, S b) {
            return ((Double) a.getAttributes().get(getAttributeKey()))
                    .compareTo(((Double) b.getAttributes().get(getAttributeKey())));
        }

    }
}
