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
public class Interval {

    protected double lower;
    protected double upper;

    public Interval(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public double getLower() {
        return lower;
    }

    public double getUpper() {
        return upper;
    }

    @Override
    public String toString() {
        return String.format("%4.5f %4.5f", lower, upper);
    }

}
