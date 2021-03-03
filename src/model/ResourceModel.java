/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

/**
 *
 * @author thinkpad
 */
public class ResourceModel {

    protected ArrayList<Integer> beds;
    protected ArrayList<Integer> supports;
    protected ArrayList<Interval> nursers;
    protected ArrayList<Interval> doctors;

    protected ArrayList<Integer> critical_beds;
    protected ArrayList<Integer> critical_supports;
    protected ArrayList<Interval> critical_nursers;
    protected ArrayList<Interval> critical_doctors;
    protected ArrayList<Integer> cremacion;
    protected Table original_data;
    protected int no_critical_index;
    protected int critical_index;
    protected int death_index;

    public ResourceModel(Table original_datal, int no_critical_index, int critical_index, int death_index) {
        this.original_data = original_datal;
        this.no_critical_index = no_critical_index;
        this.critical_index = critical_index;
        this.death_index = death_index;
        beds = new ArrayList<>();
        supports = new ArrayList<>();
        nursers = new ArrayList<>();
        doctors = new ArrayList<>();
        critical_beds = new ArrayList<>();
        critical_supports = new ArrayList<>();
        critical_nursers = new ArrayList<>();
        critical_doctors = new ArrayList<>();
        cremacion = new ArrayList<>();

    }

    public void calculate() {
        int rows = original_data.rowCount();
        DoubleColumn no_critical = (DoubleColumn) original_data.column(no_critical_index);
        DoubleColumn critical = (DoubleColumn) original_data.column(critical_index);
        DoubleColumn deaths = (DoubleColumn) original_data.column(death_index);

        for (int i = 0; i < rows; i++) {

            int value = (int) Math.ceil(no_critical.getDouble(i));
            // System.out.println(i + " : " + no_criticos.get(i - 1) + " <->  " + value);
            beds.add(value);
            supports.add(value);
            int lower = (value % 6 == 0) ? value / 6 : value / 6 + 1;
            int upper = (value % 4 == 0) ? value / 4 : value / 4 + 1;
            Interval enfermeras = new Interval(lower, upper);
            if (value < 4) {
                enfermeras = new Interval(0, 1);
            }
            if (value < 1) {
                enfermeras = new Interval(0, 0);
            }
            nursers.add(enfermeras);

            lower = (value % 10 == 0) ? value / 10 : value / 10 + 1;
            upper = (value % 8 == 0) ? value / 8 : value / 8 + 1;
            Interval medicos = new Interval(lower, upper);
            if (value < 8) {
                medicos = new Interval(0, 1);
            }
            if (value < 1) {
                medicos = new Interval(0, 0);
            }
            doctors.add(medicos);
            // System.out.printf("No-Critical: %d (beds), %d (supports), %s (nursers), %s (doctors)\n", beds.get(i), supports.get(i), nursers.get(i), doctors.get(i));
            // Critical
            value = (int) Math.ceil(critical.getDouble(i));
            critical_beds.add(value);
            critical_supports.add(value);
            lower = (value % 2 == 0) ? value / 2 : value / 2 + 1;
            upper = value;
            enfermeras = new Interval(Math.min(lower, upper), Math.max(lower, upper));
            if (value < 2) {
                enfermeras = new Interval(0, 1);
            }
            if (value < 1) {
                enfermeras = new Interval(0, 0);
            }
            critical_nursers.add(enfermeras);
            lower = (value % 8 == 0) ? value / 8 : value / 8 + 1;
            upper = (value % 5 == 0) ? value / 5 : value / 5 + 1;
            medicos = new Interval(Math.min(lower, upper), Math.max(lower, upper));

            if (value < 5) {
                medicos = new Interval(0, 1);
            }
            if (value < 1) {
                medicos = new Interval(0, 0);
            }
            critical_doctors.add(medicos);
            cremacion.add((int) Math.ceil(deaths.getDouble(i)));

        }
    }

    public void exportToExcel(String path_to_save, ArrayList<Hospital> hospitales) throws InvalidFormatException, IOException {
        File input_file = new File(path_to_save);

        XSSFWorkbook outWorkbook = new XSSFWorkbook();
        // No criticos
        XSSFSheet no_criticos_Sheet = outWorkbook.createSheet("No-Criticos");
        create_title(no_criticos_Sheet, true);
        XSSFSheet criticos_Sheet = outWorkbook.createSheet("Criticos");
        create_title(criticos_Sheet, false);
        XSSFSheet deaths_Sheet = outWorkbook.createSheet("Muertos");
        deaths_Sheet.createRow(0).createCell(0, CellType.STRING).setCellValue("Cremacion");
        double total_beds = hospitales.stream().mapToLong(Hospital::getNumber_of_beds).sum();
        double total_support = hospitales.stream().mapToLong(Hospital::getNumber_of_supports).sum();
        double total_nurser = hospitales.stream().mapToLong(Hospital::getNumbef_of_nurser).sum();
        double total_doctor = hospitales.stream().mapToLong(Hospital::getNumber_of_doctor).sum();

        double total_support_special = hospitales.stream().mapToLong(Hospital::getNumber_of_supports_special).sum();
        double total_nurser_special = hospitales.stream().mapToLong(Hospital::getNumbef_of_nurser_special).sum();
        double total_doctor_scpecial = hospitales.stream().mapToLong(Hospital::getNumber_of_doctor_special).sum();
        CellStyle style = outWorkbook.createCellStyle();
        style.setDataFormat(outWorkbook.createDataFormat().getFormat("0.000%"));

        for (int i = 1; i < beds.size(); i++) {
            // No criticos
            Row row_no_criticos = no_criticos_Sheet.createRow(i);
            row_no_criticos.createCell(0, CellType.NUMERIC).setCellValue(beds.get(i));
            row_no_criticos.createCell(1, CellType.NUMERIC).setCellValue(supports.get(i));
            row_no_criticos.createCell(2, CellType.STRING).setCellValue(nursers.get(i).toString());
            row_no_criticos.createCell(3, CellType.STRING).setCellValue(doctors.get(i).toString());

            // Criticos
            Row criticos_Row = criticos_Sheet.createRow(i);

            criticos_Row.createCell(0, CellType.NUMERIC).setCellValue(critical_beds.get(i));
            criticos_Row.createCell(1, CellType.NUMERIC).setCellValue(critical_supports.get(i));
            criticos_Row.createCell(2, CellType.STRING).setCellValue(critical_nursers.get(i).toString());

            criticos_Row.createCell(3, CellType.STRING).setCellValue(critical_doctors.get(i).toString());
            deaths_Sheet.createRow(i).createCell(0, CellType.NUMERIC).setCellValue(Math.ceil(cremacion.get(i)));

            // Adittional information
            row_no_criticos.createCell(4, CellType.NUMERIC).setCellValue(beds.get(i) / total_beds + critical_beds.get(i) / total_beds);
            row_no_criticos.getCell(4).setCellStyle(style);

            row_no_criticos.createCell(5, CellType.NUMERIC).setCellValue(nursers.get(i).upper / total_nurser);
            row_no_criticos.getCell(5).setCellStyle(style);

            row_no_criticos.createCell(6, CellType.NUMERIC).setCellValue(doctors.get(i).upper / total_doctor);
            row_no_criticos.getCell(6).setCellStyle(style);

            row_no_criticos.createCell(7, CellType.NUMERIC).setCellValue(supports.get(i) / total_support);
            row_no_criticos.getCell(7).setCellStyle(style);

            // Adittional information
            criticos_Row.createCell(4, CellType.NUMERIC).setCellValue(beds.get(i) / total_beds + critical_beds.get(i) / total_beds);
            criticos_Row.getCell(4).setCellStyle(style);

            criticos_Row.createCell(5, CellType.NUMERIC).setCellValue(critical_nursers.get(i).upper / total_nurser_special);
            criticos_Row.getCell(5).setCellStyle(style);

            criticos_Row.createCell(6, CellType.NUMERIC).setCellValue(critical_doctors.get(i).upper / total_doctor_scpecial);
            criticos_Row.getCell(6).setCellStyle(style);

            criticos_Row.createCell(7, CellType.NUMERIC).setCellValue(critical_supports.get(i) / total_support_special);
            criticos_Row.getCell(7).setCellStyle(style);

        }
        Row row_no_criticos = no_criticos_Sheet.getRow(0);

        for (int i = 0; i < row_no_criticos.getLastCellNum(); i++) {
            no_criticos_Sheet.autoSizeColumn(i);
            criticos_Sheet.autoSizeColumn(i);
        }
        if (!input_file.getName().endsWith(".xlsx")) {
            String name = input_file.getName() + ".xlsx";
            input_file = new File(input_file.getParentFile().getAbsolutePath(), name);
        }
        outWorkbook.write(new FileOutputStream(input_file));
        outWorkbook.close();
    }

    private void create_title(XSSFSheet sheet, boolean type) {
        Row title = sheet.createRow(0);
        title.createCell(0, CellType.STRING).setCellValue("Camas");
        title.createCell(1, CellType.STRING).setCellValue("Soporte " + ((type) ? "no invasivo" : "invasivo"));
        title.createCell(2, CellType.STRING).setCellValue("Enfermera " + ((type) ? "general" : "especializada"));
        title.createCell(3, CellType.STRING).setCellValue("Medico " + ((type) ? "general" : "especializada"));
        title.createCell(4, CellType.STRING).setCellValue("Porcentaje de ocupacion de camas");
        title.createCell(5, CellType.STRING).setCellValue("Porcentaje de ocupacion de enfermeras");
        title.createCell(6, CellType.STRING).setCellValue("Porcentaje de ocupacion de medicos");
        title.createCell(7, CellType.STRING).setCellValue("Porcentaje de ocupacion de equipo de soporte");
    }

    public ArrayList<Integer> getBeds() {
        return beds;
    }

    public void setBeds(ArrayList<Integer> beds) {
        this.beds = beds;
    }

    public ArrayList<Integer> getSupports() {
        return supports;
    }

    public void setSupports(ArrayList<Integer> supports) {
        this.supports = supports;
    }

    public ArrayList<Interval> getNursers() {
        return nursers;
    }

    public void setNursers(ArrayList<Interval> nursers) {
        this.nursers = nursers;
    }

    public ArrayList<Interval> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Interval> doctors) {
        this.doctors = doctors;
    }

    public ArrayList<Integer> getCritical_beds() {
        return critical_beds;
    }

    public void setCritical_beds(ArrayList<Integer> critical_beds) {
        this.critical_beds = critical_beds;
    }

    public ArrayList<Integer> getCritical_supports() {
        return critical_supports;
    }

    public void setCritical_supports(ArrayList<Integer> critical_supports) {
        this.critical_supports = critical_supports;
    }

    public ArrayList<Interval> getCritical_nursers() {
        return critical_nursers;
    }

    public void setCritical_nursers(ArrayList<Interval> critical_nursers) {
        this.critical_nursers = critical_nursers;
    }

    public ArrayList<Interval> getCritical_doctors() {
        return critical_doctors;
    }

    public void setCritical_doctors(ArrayList<Interval> critical_doctors) {
        this.critical_doctors = critical_doctors;
    }

    public ArrayList<Integer> getCremacion() {
        return cremacion;
    }

    public void setCremacion(ArrayList<Integer> cremacion) {
        this.cremacion = cremacion;
    }

    public Table getOriginal_data() {
        return original_data;
    }

    public void setOriginal_data(Table original_data) {
        this.original_data = original_data;
    }

    public int getNo_critical_index() {
        return no_critical_index;
    }

    public void setNo_critical_index(int no_critical_index) {
        this.no_critical_index = no_critical_index;
    }

    public int getCritical_index() {
        return critical_index;
    }

    public void setCritical_index(int critical_index) {
        this.critical_index = critical_index;
    }

    public int getDeath_index() {
        return death_index;
    }

    public void setDeath_index(int death_index) {
        this.death_index = death_index;
    }
}
