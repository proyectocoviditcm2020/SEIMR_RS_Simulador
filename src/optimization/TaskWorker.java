/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimization;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import optimization.core.Solution;
import optimization.heuristics.Algorithm;
import optimization.problems.Covid19;

/**
 *
 * @author thinkpad
 */
public class TaskWorker extends SwingWorker<Void, Integer> {

    protected JProgressBar progressBar;
    protected JButton button;
    protected Algorithm algorithm;
    protected int executions;
    protected Solution best;
    protected JTextField fitness;
    protected JTextField[] params;

    public TaskWorker(Algorithm algorithm, int executions, JTextField textFitness, JProgressBar progressBar, JButton optimize_button, JTextField... params) {
        this.algorithm = algorithm;
        this.executions = executions;
        this.progressBar = progressBar;
        this.progressBar.setMaximum(executions);
        this.button = optimize_button;
        this.params = params;
        this.fitness = textFitness;
    }

    @Override
    protected void process(List<Integer> chunks) {
        if (chunks.size() > 0) {
            int current = chunks.get(chunks.size() - 1);
            progressBar.setValue(current);

            progressBar.setToolTipText("Ejecutando ... " + (current + 1));
            if (current == progressBar.getMaximum()) {
                progressBar.setToolTipText("Finalizado");
            }
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.button.setEnabled(false);
        if (progressBar.getMaximum() > 1) {
            publish(0);
        }
        for (int i = 0; i < params.length; i++) {
            params[i].setText("");
        }
        fitness.setText("");
        for (int i = 0; i < executions; i++) {
            System.out.print("running... " + i + " ");
            algorithm = algorithm.copy();
            algorithm.execute();
            if (best == null) {
                best = algorithm.getSolutions().get(0).copy();
            } else if (best.getObjective(0).doubleValue() > algorithm.getSolutions().get(0).getObjective(0)
                    .doubleValue()) {
                best = algorithm.getSolutions().get(0).copy();
            }
            publish(i+1);
            System.out.println(best);
        }
        publish(executions);
        this.button.setEnabled(true);
        for (int i = 0; i < best.getVariables().size(); i++) {
            params[i].setText(best.getResource(i).toString());
        }       
        this.fitness.setText(best.getResource(0).toString());
        return null;
    }

    public Solution getBest() {
        return best;
    }

}
