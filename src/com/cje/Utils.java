package com.cje;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by bbybby on 6/13/2016.
 */
public class Utils {
    public Utils() {}

    public static boolean isDecimalInput(char c) {
        boolean isValid = false;

        if( Character.isDigit(c) || (c== KeyEvent.VK_SPACE) || (c==KeyEvent.VK_DELETE) || (c=='.')) {
            isValid = true;
        }

        return isValid;
    }

    public static boolean isActualValue(String str) {
        float f;
        try {
            f = Float.parseFloat(str);
            if(f>0) return true;
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

}
