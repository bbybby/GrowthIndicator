package com.cje;

import org.apache.commons.math3.distribution.NormalDistribution;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by bbybby on 6/13/2016.
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

    public static int getDayBetweenDates(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        int days = -1;
        Calendar cal1 = Calendar.getInstance();
        cal1.set(fromYear, fromMonth, fromDay);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(toYear, toMonth, toDay);

        days = (int)((cal2.getTimeInMillis() - cal1.getTimeInMillis())/(24*60*60*1000));

        return days;
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

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void log(String msg) {
        System.out.println(msg);
    }
}
