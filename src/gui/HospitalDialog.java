/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JDialog;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import model.Hospital;

/**
 *
 * @author thinkpad
 */
public class HospitalDialog extends JDialog {

    private Hospital _hospital;

    public HospitalDialog(Frame father, Hospital hospital) {

        super(father, "Condiguracion de Hospital", true);
        Hospital model;
        if (hospital != null) {
            model = hospital;
        } else {
            model = new Hospital(100, 50, 50, 100, 50, 50, 20);
        }
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();

        java.awt.GridBagLayout jPanel3Layout = new java.awt.GridBagLayout();
        jPanel3Layout.columnWidths = new int[]{0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel3Layout.rowHeights = new int[]{0};
        jPanel3.setLayout(jPanel3Layout);
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();

        jLabel9.setText("Enfermeros");

        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel9, gridBagConstraints);

        jPanel3.add(jLabel9, gridBagConstraints);
        javax.swing.JLabel jLabel12 = new javax.swing.JLabel();

        jLabel12.setText("Medicos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel3.add(jLabel12, gridBagConstraints);

        javax.swing.JTextField textFieldNumberOfSupport = new javax.swing.JTextField("" + model.getNumber_of_supports());

        textFieldNumberOfSupport.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc14 = (PlainDocument) textFieldNumberOfSupport.getDocument();
        doc14.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 10;
        gridBagConstraints2.gridy = 0;
        jPanel3.add(textFieldNumberOfSupport, gridBagConstraints2);
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();

        jLabel8.setText("Equipo de Soporte");
        java.awt.GridBagConstraints gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 8;
        gridBagConstraints3.gridy = 0;
        jPanel3.add(jLabel8, gridBagConstraints3);
        javax.swing.JTextField textFieldNumberOfDoctor = new javax.swing.JTextField("" + model.getNumber_of_doctor());

        textFieldNumberOfDoctor.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc13 = (PlainDocument) textFieldNumberOfDoctor.getDocument();
        doc13.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 6;
        gridBagConstraints4.gridy = 0;
        jPanel3.add(textFieldNumberOfDoctor, gridBagConstraints4);

        javax.swing.JTextField textFieldNumberOfNurser = new javax.swing.JTextField("" + model.getNumbef_of_nurser());

        textFieldNumberOfNurser.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc9 = (PlainDocument) textFieldNumberOfNurser.getDocument();
        doc9.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 0;
        jPanel3.add(textFieldNumberOfNurser, gridBagConstraints5);

        java.awt.GridBagLayout jPanel5Layout = new java.awt.GridBagLayout();
        jPanel5Layout.columnWidths = new int[]{0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel5Layout.rowHeights = new int[]{0};
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        jPanel5.setLayout(jPanel5Layout);
        javax.swing.JLabel jLabel16 = new javax.swing.JLabel();

        jLabel16.setText("Enfermeros");
        java.awt.GridBagConstraints gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.gridy = 0;
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        jPanel5.add(jLabel16, gridBagConstraints6);
        javax.swing.JLabel jLabel17 = new javax.swing.JLabel();

        jLabel17.setText("Medicos");
        java.awt.GridBagConstraints gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 4;
        gridBagConstraints7.gridy = 0;
        jPanel5.add(jLabel17, gridBagConstraints7);
        javax.swing.JTextField textFieldNumberOfSpecialSupport = new javax.swing.JTextField("" + model.getNumber_of_supports_special());

        textFieldNumberOfSpecialSupport.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc15 = (PlainDocument) textFieldNumberOfSpecialSupport.getDocument();
        doc15.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 10;
        gridBagConstraints8.gridy = 0;
        jPanel5.add(textFieldNumberOfSpecialSupport, gridBagConstraints8);
        javax.swing.JLabel jLabel18 = new javax.swing.JLabel();

        jLabel18.setText("Equipo de Soporte");
        java.awt.GridBagConstraints gridBagConstraints9 = new java.awt.GridBagConstraints();
        gridBagConstraints9.gridx = 8;
        gridBagConstraints9.gridy = 0;
        jPanel5.add(jLabel18, gridBagConstraints9);
        javax.swing.JTextField textFieldNumberOfSpecialDoctor = new javax.swing.JTextField("" + model.getNumber_of_doctor_special());

        textFieldNumberOfSpecialDoctor.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc16 = (PlainDocument) textFieldNumberOfSpecialDoctor.getDocument();
        doc16.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints10 = new java.awt.GridBagConstraints();
        gridBagConstraints10.gridx = 6;
        gridBagConstraints10.gridy = 0;
        jPanel5.add(textFieldNumberOfSpecialDoctor, gridBagConstraints10);
        javax.swing.JTextField textFieldNumberOfSpecialNurser = new javax.swing.JTextField("" + model.getNumbef_of_nurser_special());

        textFieldNumberOfSpecialNurser.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc17 = (PlainDocument) textFieldNumberOfSpecialNurser.getDocument();
        doc17.setDocumentFilter(new MyIntFilter());

        java.awt.GridBagConstraints gridBagConstraints11 = new java.awt.GridBagConstraints();
        gridBagConstraints11.gridx = 2;
        gridBagConstraints11.gridy = 0;
        jPanel5.add(textFieldNumberOfSpecialNurser, gridBagConstraints11);
        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();

        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();

        jLabel6.setText("Configurar Hospitales");

        javax.swing.JLabel jLabel15 = new javax.swing.JLabel();

        jLabel15.setText("Generales");

        javax.swing.JLabel jLabel14 = new javax.swing.JLabel();

        jLabel14.setText("Especializados");

        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();

        jLabel7.setText("Camas");

        javax.swing.JTextField textFieldNumberOfBeds = new javax.swing.JTextField("" + model.getNumber_of_beds());

        textFieldNumberOfBeds.setPreferredSize(new java.awt.Dimension(90, 34));
        PlainDocument doc10 = (PlainDocument) textFieldNumberOfBeds.getDocument();
        doc10.setDocumentFilter(new MyIntFilter());

        javax.swing.JButton jButton1 = new javax.swing.JButton();

        jButton1.setText("Agregar");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel15)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(47, 47, 47)
                                                .addComponent(textFieldNumberOfBeds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(textFieldNumberOfBeds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addGap(12, 12, 12)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(9, 9, 9)
                                .addContainerGap())
        );
        getContentPane().add(jPanel4);
        jButton1.addActionListener((ae) -> {
            _hospital = new Hospital();
            _hospital.setNumbef_of_nurser(Integer.parseInt(textFieldNumberOfNurser.getText()));
            _hospital.setNumber_of_doctor(Integer.parseInt(textFieldNumberOfDoctor.getText()));
            _hospital.setNumber_of_supports(Integer.parseInt(textFieldNumberOfSupport.getText()));

            _hospital.setNumbef_of_nurser_special(Integer.parseInt(textFieldNumberOfSpecialNurser.getText()));
            _hospital.setNumber_of_doctor_special(Integer.parseInt(textFieldNumberOfSpecialDoctor.getText()));
            _hospital.setNumber_of_supports_special(Integer.parseInt(textFieldNumberOfSpecialSupport.getText()));
            _hospital.setNumber_of_beds(Integer.parseInt(textFieldNumberOfBeds.getText()));
            
            setVisible(false);
        });

    }

    public Hospital getModel() {
        return _hospital;
    }

    class MyIntFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else {
                // warn the user and don't allow the insert
            }
        }

        private boolean test(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                AttributeSet attrs) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                // warn the user and don't allow the insert
            }

        }

        @Override
        public void remove(FilterBypass fb, int offset, int length)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (test(sb.toString())) {
                super.remove(fb, offset, length);
            } else {
                // warn the user and don't allow the insert
            }

        }
    }
}
