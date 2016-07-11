package com.cje;

import org.apache.commons.math3.distribution.NormalDistribution;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Logger;

/**
 * Created by Jieun Choi on 6/13/2016.
 *
 * class for utilities
 */
public class Utils {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public Utils() {}

    public static boolean isDecimalInput(char c) {
        boolean isValid = false;

        if( Character.isDigit(c) || (c== KeyEvent.VK_SPACE) || (c==KeyEvent.VK_DELETE) || (c=='.')) {
            isValid = true;
        }

        return isValid;
    }

    public static boolean isActualNumber(String str) {
        float f;
        try {
            f = Float.parseFloat(str);
            if(f>0) return true;
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static boolean isValidDate(int year, int month, int day) {
        boolean result = true;
        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            log(e.getMessage());
            result = false;
        }

        return result;
    }

    public static String getAgeLabel(LocalDate begin, LocalDate end, int ageByDay) {
        String label = "";

        Period p = Period.between(begin, end);
        int years = p.getYears();
        int months = p.getMonths();
        int days = p.getDays();

        int total_months = years*12 + months;

        if(years>0) label += years + "년 ";
        if(months>0) label += months + "개월 ";
        label += days + "일     [ " + total_months + "개월 ("+ ageByDay + "일) ]";

        return label;
    }

    public static float getNormalDistribute(GrowthInfo gi, float value) {
        double res;
        double z;
        double L = gi.getL();
        double M = gi.getM();
        double S = gi.getS();

        if(L == 0) {
            z =  Math.log(value/M)/S;
        }
        else {
            z = (Math.pow((value/M), L) - 1)/(L*S);
        }

        NormalDistribution nd = new NormalDistribution();
        res = nd.cumulativeProbability(z);
        Utils.log("L:"+L+", M:"+M+", S:"+S+",  z:"+z+", res:"+res);

        return (float)res;
    }

    /*
     getting the approximate value using a linear equation (y = mx + b)
      */
    public static float getApproximateValue(float x1, float y1, float x2, float y2, float x) {
        float y;
        float m;    // slope in the equation
        float b;    // constant in the equation

        m = (y2-y1)/(x2-x1);
        b = y1 - m*x1;

        y = m*x + b;

        return y;
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void log(String msg) {
        System.out.println(msg);
    }
}
