/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Hospital;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Interval;
import model.ResourceModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

/**
 *
 * @author J. Alfredo Brambila Hdez.
 */
public class JFrameRecursosFSim extends javax.swing.JFrame {

    /** Creates new form JFrameRecursos */
    public JFrameRecursosFSim() {
	try {
	//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	
	} catch(Exception e) {
	    
	}
        hospitales = new DefaultComboBoxModel();

        initComponents();
        setTitle("SEIM/RS - Generador de Recursos");
        jComboBoxTypeSelector.setSelectedIndex(0);
        jButtonExport.setEnabled(false);
        jComboBoxTypeSelector.setEnabled(false);
        jComboBoxResource.setEnabled(false);
        graficar();
	
	this.setLocationRelativeTo(null);
    }
    
    public void fromSeriesSimulador() {
	
            File selectedFile = new File("serie_simulador.xlsx");
            if (selectedFile.getName().contains(".xlsx")) {
                try {
                    try (XSSFWorkbook workbook = new XSSFWorkbook(selectedFile)) {
                        Sheet sheet = workbook.getSheetAt(0);
                        originalData = Table.create(selectedFile.getName());
                        for (int i = 0; i < sheet.getLastRowNum(); i++) {
                            Row row = sheet.getRow(i);
                            for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
                                if (i == 0) {
                                    originalData.addColumns(DoubleColumn.create(row.getCell(j).getStringCellValue()));
                                } else {
                                    DoubleColumn column = (DoubleColumn) originalData.column(j);
                                    column.append(row.getCell(j).getNumericCellValue());
                                }
                            }
                        }
                    }
                    jTextFieldFilePath.setText(selectedFile.getName());
                    loadCombo();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al intentar leer el archivo: " + ex.getMessage(), "Error con el archivo de datos", JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedFile.getName().contains(".csv")) {
                try {
                    originalData = Table.read().csv(selectedFile);
                    jTextFieldFilePath.setText(selectedFile.getAbsolutePath());
                    loadCombo();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al intentar leer el archivo: " + ex.getMessage(), "Error con el archivo de datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        
    }
    
    public void setSeriesSimulador(double[][] d, String[] columnas) {
	int filas = d.length;
	int colS = d[0].length;
	
	try {
	    for (int i = 0; i < filas; i++) {
		for (int j = 0; j < colS; j++) {
		    if (i == 0) {
			originalData.addColumns(DoubleColumn.create(columnas[i]));
		    } else {
			DoubleColumn column = (DoubleColumn) originalData.column(j);
			column.append(d[i][j]);
		    }
		}
	    }

	    jTextFieldFilePath.setText("Leído desde SIMULADOR");
	    loadCombo();
	} catch (Exception ex) {
	    Logger.getLogger(JFrameRecursosFSim.class.getName()).log(Level.SEVERE, null, ex);
	    JOptionPane.showMessageDialog(this, "Ocurrio un error al intentar leer el informacion del simulador: " + ex.getMessage(), "Error con el archivo de datos", JOptionPane.ERROR_MESSAGE);
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jComboBoxCriticos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxMuertes = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jComboBoxNoCriticos = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextFieldFilePath = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButtonExport = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jComboBoxHospitales = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBoxResource = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxTypeSelector = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanelGraph = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modelo SEIMR/R-S - Asignación de Recursos Series Simuladas");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jComboBoxCriticos.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBoxCriticos.setPreferredSize(new java.awt.Dimension(200, 22));

        jLabel2.setText("Criticos:");

        jLabel3.setText("Muertos");

        jComboBoxMuertes.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBoxMuertes.setPreferredSize(new java.awt.Dimension(200, 22));

        jButton1.setText("calcular");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBoxNoCriticos.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBoxNoCriticos.setPreferredSize(new java.awt.Dimension(200, 22));
        jComboBoxNoCriticos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNoCriticosActionPerformed(evt);
            }
        });

        jLabel1.setText("No criticos:");

        jButton2.setText("Abrir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextFieldFilePath.setEditable(false);
        jTextFieldFilePath.setMinimumSize(new java.awt.Dimension(200, 22));
        jTextFieldFilePath.setPreferredSize(new java.awt.Dimension(200, 34));

        jLabel4.setText("Archivo de datos:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxMuertes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxCriticos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxNoCriticos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(109, 109, 109)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1)))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxNoCriticos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxCriticos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxMuertes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Hospilales"));

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jButtonExport.setText("Exportar");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jComboBoxHospitales.setModel(hospitales);
        jComboBoxHospitales.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBoxHospitales.setPreferredSize(new java.awt.Dimension(200, 34));
        jComboBoxHospitales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHospitalesActionPerformed(evt);
            }
        });

        jButton3.setText("Agregar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Editar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Eliminar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jComboBoxResource.setMinimumSize(new java.awt.Dimension(200, 22));
        jComboBoxResource.setPreferredSize(new java.awt.Dimension(200, 34));
        jComboBoxResource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResourceActionPerformed(evt);
            }
        });

        jLabel7.setText("Recurso");

        jComboBoxTypeSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No critico", "Critico", "Muertes" }));
        jComboBoxTypeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeSelectorActionPerformed(evt);
            }
        });

        jLabel19.setText("Hospitales:");

        jLabel6.setText("Tipo");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExport))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxHospitales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addGap(292, 292, 292))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jComboBoxHospitales, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxResource, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonExport)
                .addGap(22, 22, 22))
        );

        jPanelGraph.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanelGraphLayout = new javax.swing.GroupLayout(jPanelGraph);
        jPanelGraph.setLayout(jPanelGraphLayout);
        jPanelGraphLayout.setHorizontalGroup(
            jPanelGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelGraphLayout.setVerticalGroup(
            jPanelGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Data", "xlsx", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().contains(".xlsx")) {
                try {
                    try (XSSFWorkbook workbook = new XSSFWorkbook(selectedFile)) {
                        Sheet sheet = workbook.getSheetAt(0);
                        originalData = Table.create(selectedFile.getName());
                        for (int i = 0; i < sheet.getLastRowNum(); i++) {
                            Row row = sheet.getRow(i);
                            for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
                                if (i == 0) {
                                    originalData.addColumns(DoubleColumn.create(row.getCell(j).getStringCellValue()));
                                } else {
                                    DoubleColumn column = (DoubleColumn) originalData.column(j);
                                    column.append(row.getCell(j).getNumericCellValue());
                                }
                            }
                        }
                    }
                    jTextFieldFilePath.setText(selectedFile.getName());
                    loadCombo();
                } catch (Exception ex) {
                    Logger.getLogger(JFrameRecursosFSim.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al intentar leer el archivo: " + ex.getMessage(), "Error con el archivo de datos", JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedFile.getName().contains(".csv")) {
                try {
                    originalData = Table.read().csv(selectedFile);
                    jTextFieldFilePath.setText(selectedFile.getAbsolutePath());
                    loadCombo();
                } catch (Exception ex) {
                    Logger.getLogger(JFrameRecursosFSim.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al intentar leer el archivo: " + ex.getMessage(), "Error con el archivo de datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBoxNoCriticosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNoCriticosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxNoCriticosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (originalData != null) {
            int nocritical = jComboBoxNoCriticos.getSelectedIndex();
            int critical = jComboBoxCriticos.getSelectedIndex();
            int deaths = jComboBoxMuertes.getSelectedIndex();
            if (nocritical != -1 && critical != -1 && deaths != -1) {
                resourceModel = new ResourceModel(originalData, nocritical, critical, deaths);
                resourceModel.calculate();
                //                paint_model();

                jComboBoxTypeSelector.setEnabled(true);
                jComboBoxResource.setEnabled(true);
                if (jComboBoxHospitales.getItemCount() > 0) {
                    jButtonExport.setEnabled(true);
                }

                if (jComboBoxResource.getItemCount() > 0) {
                    jComboBoxResource.setSelectedIndex(0);
                    //paint_model(0, jComboBoxResource.getSelectedItem().toString());
                }
            } else {
                jComboBoxTypeSelector.setEnabled(false);
                jComboBoxResource.setEnabled(false);
                jButtonExport.setEnabled(false);
            }
        } else {
            // Do something...
            jComboBoxTypeSelector.setEnabled(false);
            jComboBoxResource.setEnabled(false);
            jButtonExport.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBoxHospitalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHospitalesActionPerformed
        // TODO add your handling code here:
        if (jComboBoxHospitales.getItemCount() > 0 && resourceModel != null) {
            jButtonExport.setEnabled(true);
        } else {
            jButtonExport.setEnabled(false);
        }
    }//GEN-LAST:event_jComboBoxHospitalesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        HospitalDialog dialog = new HospitalDialog(this, null);
	dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.getModel() != null) {
            hospitales.addElement(dialog.getModel());
            if (resourceModel != null) {
                paint_model(jComboBoxResource.getSelectedIndex(), jComboBoxResource.getSelectedItem().toString());
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Hospital hospital = (Hospital) jComboBoxHospitales.getSelectedItem();
        int index = jComboBoxHospitales.getSelectedIndex();
        HospitalDialog dialog = new HospitalDialog(this, hospital);
	dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        Hospital newValue = dialog.getModel();
        if (newValue != null) {
            Hospital old = (Hospital) hospitales.getElementAt(index);
            old.setNumbef_of_nurser(newValue.getNumbef_of_nurser());
            old.setNumbef_of_nurser_special(newValue.getNumbef_of_nurser_special());
            old.setNumber_of_beds(newValue.getBeds());
            old.setNumber_of_doctor(newValue.getNumber_of_doctor());
            old.setNumber_of_doctor_special(newValue.getNumber_of_doctor_special());
            old.setNumber_of_supports(newValue.getNumber_of_supports());
            old.setNumber_of_supports_special(newValue.getNumber_of_supports_special());
            if (resourceModel != null) {
                paint_model(jComboBoxResource.getSelectedIndex(), jComboBoxResource.getSelectedItem().toString());
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (jComboBoxHospitales.getSelectedIndex() != -1) {
            jComboBoxHospitales.removeItemAt(jComboBoxHospitales.getSelectedIndex());
        }
        if (resourceModel != null) {
            paint_model(jComboBoxResource.getSelectedIndex(), jComboBoxResource.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                        case JOptionPane.NO_OPTION:
                        return;
                        case JOptionPane.CLOSED_OPTION:
                        return;
                        case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                    }
                }
                super.approveSelection();
            }
        };
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            ArrayList<Hospital> hospitales_ = new ArrayList<>();
            for (int i = 0; i < jComboBoxHospitales.getItemCount(); i++) {
                hospitales_.add((Hospital) hospitales.getElementAt(i));
            }
            try {
                resourceModel.exportToExcel(fileToSave.getAbsolutePath(), hospitales_);
                JOptionPane.showMessageDialog(this, "El archivo se guardo correctamente", "Dialog",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (InvalidFormatException | IOException ex) {
                    ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ocurrio un error al guardar el archivo", "Error al guardar",
                    JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jComboBoxTypeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeSelectorActionPerformed
        // TODO add your handling code here:
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        switch (jComboBoxTypeSelector.getSelectedIndex()) {
            case 0:
            comboBoxModel.addElement("Camas");
            comboBoxModel.addElement("Equipo de soporte");
            comboBoxModel.addElement("Enfermeras");
            comboBoxModel.addElement("Medicos");
            jComboBoxResource.setModel(comboBoxModel);

            break;
            case 1:
            comboBoxModel.addElement("Camas especializadas");
            comboBoxModel.addElement("Equipo de soporte especializadas");
            comboBoxModel.addElement("Enfermeras especializadas");
            comboBoxModel.addElement("Medicos especializados");
            jComboBoxResource.setModel(comboBoxModel);
            break;
            case 2:
            comboBoxModel.addElement("Cremacion");
            jComboBoxResource.setModel(comboBoxModel);

            break;
            default:
            System.out.println(jComboBoxTypeSelector.getSelectedItem());
        }
        if (jComboBoxResource.getItemCount() > 0)
        jComboBoxResource.setSelectedIndex(0);
    }//GEN-LAST:event_jComboBoxTypeSelectorActionPerformed

    private void jComboBoxResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResourceActionPerformed
        // TODO add your handling code here:
        if (resourceModel != null) {
            paint_model(jComboBoxResource.getSelectedIndex(), jComboBoxResource.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jComboBoxResourceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameRecursosFSim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameRecursosFSim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameRecursosFSim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameRecursosFSim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameRecursosFSim().setVisible(true);
            }
        });
    }

    private DefaultComboBoxModel hospitales;
    private Table originalData;
    private ResourceModel resourceModel;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JComboBox<String> jComboBoxCriticos;
    private javax.swing.JComboBox<String> jComboBoxHospitales;
    private javax.swing.JComboBox<String> jComboBoxMuertes;
    private javax.swing.JComboBox<String> jComboBoxNoCriticos;
    private javax.swing.JComboBox<String> jComboBoxResource;
    private javax.swing.JComboBox<String> jComboBoxTypeSelector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelGraph;
    private javax.swing.JTextField jTextFieldFilePath;
    // End of variables declaration//GEN-END:variables

    private void loadCombo() {
        String[] columnNames = originalData.columnNames().toArray(new String[originalData.columnNames().size()]);
        jComboBoxCriticos.setModel(new DefaultComboBoxModel<>(columnNames));
        jComboBoxNoCriticos.setModel(new DefaultComboBoxModel<>(columnNames));
        jComboBoxMuertes.setModel(new DefaultComboBoxModel<>(columnNames));
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            if (columnName.contains("I2")) {
                jComboBoxNoCriticos.setSelectedIndex(i);
            } else if (columnName.contains("I3")) {
                jComboBoxCriticos.setSelectedIndex(i);
            } else if (columnName.contains("D")) {
                jComboBoxMuertes.setSelectedIndex(i);
            }

        }
    }

    private void graficar() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries dataS = new XYSeries("S");
        XYSeries dataE = new XYSeries("E");
        JFreeChart createXYLineChart = ChartFactory.createXYLineChart("SEIMR-R-S", "Días", "Población", dataset);
        ChartPanel cp = new ChartPanel(createXYLineChart);

        Dimension d = this.jPanelGraph.getSize();
        System.out.println("org.castellanos94.covid19resources.gui.Main.graficar()" + d);
        cp.setBounds(20, 20, d.width - 20, d.height - 20);
        this.jPanelGraph.removeAll();
        this.jPanelGraph.add(cp, BorderLayout.CENTER);
        this.jPanelGraph.validate();
    }

    private void paint_model(int selectedIndex, String label) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries data = new XYSeries(label);
        XYSeries current = new XYSeries("Recursos actuales");
        XYSeries upper = null;

        Color c = Color.RED;
        // modelo.sumaElementosEstado(modelo.getS(),rg,i)*modelo.getPobTotalRS_Region(rg)
        if (jComboBoxTypeSelector.getSelectedIndex() < 2) {
            switch (selectedIndex) {
                case 0:
                    int all_beds = 0;
                    for (int i = 0; i < hospitales.getSize(); i++) {
                        all_beds += ((Hospital) hospitales.getElementAt(i)).getBeds();
                    }
                    ArrayList<Integer> beds = (jComboBoxTypeSelector.getSelectedIndex() == 0) ? resourceModel.getBeds() : resourceModel.getCritical_beds();
                    for (int i = 0; i < beds.size(); i++) {
                        data.add(i + 1, beds.get(i));
                        current.add(i + 1, all_beds);
                    }
                    c = Color.RED;
                    break;

                case 1:
                    int all_support = 0;
                    for (int i = 0; i < hospitales.getSize(); i++) {
                        if (jComboBoxTypeSelector.getSelectedIndex() == 0) {
                            all_support += ((Hospital) hospitales.getElementAt(i)).getNumber_of_supports();
                        } else {
                            all_support += ((Hospital) hospitales.getElementAt(i)).getNumber_of_supports_special();
                        }
                    }
                    ArrayList<Integer> support = (jComboBoxTypeSelector.getSelectedIndex() == 0) ? resourceModel.getSupports() : resourceModel.getCritical_supports();
                    for (int i = 0; i < support.size(); i++) {
                        data.add(i + 1, support.get(i));
                        current.add(i + 1, all_support);
                    }
                    c = Color.GREEN;
                    break;
                case 2:
                    int nursers = 0;
                    for (int i = 0; i < hospitales.getSize(); i++) {
                        if (jComboBoxTypeSelector.getSelectedIndex() == 0) {
                            nursers += ((Hospital) hospitales.getElementAt(i)).getNumbef_of_nurser();
                        } else {
                            nursers += ((Hospital) hospitales.getElementAt(i)).getNumbef_of_nurser_special();
                        }
                    }
                    ArrayList<Interval> nurser = (jComboBoxTypeSelector.getSelectedIndex() == 0) ? resourceModel.getNursers() : resourceModel.getCritical_nursers();
                    data = new XYSeries("Optimista");
                    upper = new XYSeries("Pesimista");
                    for (int i = 0; i < nurser.size(); i++) {
                        data.add(i + 1, nurser.get(i).getLower());
                        upper.add(i + 1, nurser.get(i).getUpper());
                        current.add(i + 1, nursers);
                    }
                    c = Color.GREEN;
                    break;
                case 3:
                    int doctors = 0;
                    for (int i = 0; i < hospitales.getSize(); i++) {
                        if (jComboBoxTypeSelector.getSelectedIndex() == 0) {
                            doctors += ((Hospital) hospitales.getElementAt(i)).getNumber_of_doctor();
                        } else {
                            doctors += ((Hospital) hospitales.getElementAt(i)).getNumber_of_doctor_special();
                        }
                    }
                    ArrayList<Interval> doctor = (jComboBoxTypeSelector.getSelectedIndex() == 0) ? resourceModel.getDoctors() : resourceModel.getCritical_doctors();
                    data = new XYSeries("Optimista");
                    upper = new XYSeries("Pesimista");
                    for (int i = 0; i < doctor.size(); i++) {
                        data.add(i + 1, doctor.get(i).getLower());
                        current.add(i + 1, doctors);
                        upper.add(i + 1, doctor.get(i).getUpper());
                    }
                    c = Color.GREEN;
                    break;
                default:
                    System.out.println(selectedIndex);
                    break;
            }
        } else {
            ArrayList<Integer> deaths = resourceModel.getCremacion();
            for (int i = 0; i < deaths.size(); i++) {
                data.add(i + 1, deaths.get(i));
            }

            c = Color.BLACK;
        }

        dataset.addSeries(data);
        dataset.addSeries(current);
        if (upper != null) {
            dataset.addSeries(upper);
        }
        JFreeChart grafica = ChartFactory.createXYLineChart(label, "Días", "Población", dataset);
        ChartPanel cp = new ChartPanel(grafica);
        //add(cp);

        Dimension d = this.jPanelGraph.getSize();

        cp.setBounds(20, 20, d.width - 20, d.height - 20);

        final XYPlot plot = grafica.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, c);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, Color.black);
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        if (upper != null) {
            renderer.setSeriesPaint(2, Color.RED);
            renderer.setSeriesStroke(2, new BasicStroke(2.0f));

        }
        plot.setRenderer(renderer);

        // this.jPanelGrafica.get
        this.jPanelGraph.removeAll();
        this.jPanelGraph.add(cp, BorderLayout.CENTER);
        this.jPanelGraph.validate();
        this.jPanelGraph.updateUI();
    }
}
