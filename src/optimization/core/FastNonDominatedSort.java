package optimization.core;

import java.util.ArrayList;
import java.util.Iterator;

import optimization.operators.DominanceComparator;


public class FastNonDominatedSort<S extends Solution> {
    protected ArrayList<ArrayList<S>> fronts;
    protected DominanceComparator paretoDominance;

    public FastNonDominatedSort() {
        this.paretoDominance = new DominanceComparator();
        this.fronts = new ArrayList<>();
    }

    public void computeRanking(ArrayList<S> population) {
        this.fronts = new ArrayList<>();
        ArrayList<ArrayList<Integer>> dominate_me = new ArrayList<>();

        for (int i = 0; i < population.size(); i++) {
            dominate_me.add(new ArrayList<>());
            fronts.add(new ArrayList<>());
        }
        for (int i = 0; i < population.size() - 1; i++) {
            for (int j = 1; j < population.size(); j++) {
               // if (i != j && !population.get(i).equals(population.get(j))) {
                    int value = paretoDominance.compare(population.get(i), population.get(j));
                    if (value == -1 && !dominate_me.get(j).contains(i)) {
                        dominate_me.get(j).add(i);
                    } else if (value == 1 && !dominate_me.get(i).contains(j)) {
                        dominate_me.get(i).add(j);
                    }
               // }
            }
        }
        int i = 0;
        Iterator<ArrayList<Integer>> iter = dominate_me.iterator();

        ArrayList<Integer> toRemove = new ArrayList<>();
        int min = getMinDom(dominate_me);
        i = 0;
        while (iter.hasNext()) {
            ArrayList<Integer> dom = iter.next();
            if (dom.size() == min) {
                fronts.get(0).add(population.get(i));
                toRemove.add(i);
            }
            i++;
        }
        for (Integer integer : toRemove) {
            solutionRemove(integer, dominate_me);
        }
        for (int j = 1; j < fronts.size(); j++) {
            ArrayList<S> currentFront = fronts.get(j);
            i = 0;
            min = getMinDom(dominate_me);
            for (ArrayList<Integer> dom_me : dominate_me) {
                if (!toRemove.contains(i) && dom_me.size() == min) {
                    currentFront.add(population.get(i));
                    toRemove.add(i);
                }
                i++;
            }
            for (Integer integer : toRemove) {
                solutionRemove(integer, dominate_me);
            }
        }
        setRanking();
    }

    private void setRanking() {
        fronts.removeIf(front -> front.size() == 0);

        for (int j = 0; j < fronts.size(); j++) {
            ArrayList<S> front = fronts.get(j);
            for (S solution : front) {
                solution.setRank(j);
            }
        }
    }

    private int getMinDom(ArrayList<ArrayList<Integer>> dominate_me) {
        int min = Integer.MAX_VALUE;
        for (ArrayList<Integer> arrayList : dominate_me) {
            if (arrayList.size() < min) {
                min = arrayList.size();
            }
        }
        return min;
    }

    private void solutionRemove(Integer i, ArrayList<ArrayList<Integer>> dominate_me) {
        for (ArrayList<Integer> arrayList : dominate_me) {
            if (arrayList.contains(i)) {
                arrayList.remove(i);
            }
        }
    }

    public ArrayList<S> getSubFront(int index) {
        return fronts.get(index);
    }

    public int getNumberOfSubFronts() {
        return fronts.size();
    }

}