package com.cje;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

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
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello!"+ birthWeightTxt.getValue());
            }
        });

    }


    private void createUIComponents() {
        //NumberFormat fmt = NumberFormat.getNumberInstance();
        MaskFormatter fmt = new MaskFormatter();
        fmt.setValidCharacters("0123456789.");
        //fmt.setMaximumFractionDigits(2);
        birthWeightTxt = new JFormattedTextField(fmt);
        birthWeightTxt.setVisible(true);
    }
}
