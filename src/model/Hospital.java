/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author thinkpad
 */
public class Hospital {

    protected int number_of_beds;
    protected int numbef_of_nurser;
    protected int number_of_doctor;
    protected int number_of_supports;
    protected int numbef_of_nurser_special;
    protected int number_of_doctor_special;
    protected int number_of_supports_special;

    public Hospital(int number_of_beds, int numbef_of_nurser, int number_of_doctor, int number_of_supports, int numbef_of_nurser_special, int number_of_doctor_special, int number_of_supports_special) {
        this.number_of_beds = number_of_beds;
        this.numbef_of_nurser = numbef_of_nurser;
        this.number_of_doctor = number_of_doctor;
        this.number_of_supports = number_of_supports;
        this.numbef_of_nurser_special = numbef_of_nurser_special;
        this.number_of_doctor_special = number_of_doctor_special;
        this.number_of_supports_special = number_of_supports_special;
    }

    public Hospital() {
    }

    public int getNumber_of_beds() {
        return number_of_beds;
    }

    public void setNumber_of_beds(int number_of_beds) {
        this.number_of_beds = number_of_beds;
    }

    public int getNumbef_of_nurser() {
        return numbef_of_nurser;
    }

    public void setNumbef_of_nurser(int numbef_of_nurser) {
        this.numbef_of_nurser = numbef_of_nurser;
    }

    public int getNumber_of_doctor() {
        return number_of_doctor;
    }

    public void setNumber_of_doctor(int number_of_doctor) {
        this.number_of_doctor = number_of_doctor;
    }

    public int getNumber_of_supports() {
        return number_of_supports;
    }

    public void setNumber_of_supports(int number_of_supports) {
        this.number_of_supports = number_of_supports;
    }

    public int getNumbef_of_nurser_special() {
        return numbef_of_nurser_special;
    }

    public void setNumbef_of_nurser_special(int numbef_of_nurser_special) {
        this.numbef_of_nurser_special = numbef_of_nurser_special;
    }

    public int getNumber_of_doctor_special() {
        return number_of_doctor_special;
    }

    public void setNumber_of_doctor_special(int number_of_doctor_special) {
        this.number_of_doctor_special = number_of_doctor_special;
    }

    public int getNumber_of_supports_special() {
        return number_of_supports_special;
    }

    public void setNumber_of_supports_special(int number_of_supports_special) {
        this.number_of_supports_special = number_of_supports_special;
    }

    public int getBeds() {
        return number_of_beds;
    }

    public int getNurser() {
        return numbef_of_nurser + numbef_of_nurser_special;
    }

    public int getSupports() {
        return number_of_supports + number_of_supports_special;
    }

    public int getDoctors() {
        return number_of_doctor + number_of_doctor_special;
    }

    @Override
    public String toString() {
        return String.format("Camas: %4d, Medicos: %4d, Enfermeros: %4d, Equipos de soporte : %4d ", getBeds(), getDoctors(), getNurser(), getSupports());
    }

}
