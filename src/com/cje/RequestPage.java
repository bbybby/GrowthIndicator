package com.cje;

import javax.swing.*;

/**
 * Created by bbybby on 6/8/2016.
 */
public class RequestPage extends JFrame{
    private JTextField titleTextField;

    private JPanel mainPanel;
    private JPanel inputPanel;
    private JRadioButton maleRdBtn;
    private JRadioButton femaleRdBtn;
    private JComboBox birthYearCBox;
    private JComboBox birthMonthCBox;
    private JComboBox birthDayCBox;
    private JFormattedTextField heightTxt;
    private JFormattedTextField weightTxt;
    private JFormattedTextField headTxt;
    private JFormattedTextField bmiTxt;
    private JButton viewBtn;
    private JFormattedTextField birthWeightTxt;

    public RequestPage() {
        super("Growth Indicator");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        //setVisible(true);
    }
}
