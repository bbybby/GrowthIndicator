package com.cje;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private JLabel ageTxt;
    private JLabel todayTxt;
    private JLabel ageLabel;

    //    private Utils utils;
    private Params.GENDER gender = Params.GENDER.NONE;
    private int birthYear;
    private int birthMonth;
    private int birthDay;

    private int cYear;  // current Year
    private int cMonth;
    private int cDay;

    private int ageByMonth;

    public RequestPage() {
        super("Growth Indicator");

        // Initialize member variables
        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH) + 1;   // since January is 0, Dec. is 11
        cDay = cal.get(Calendar.DAY_OF_MONTH);
        todayTxt.setText("[기준일] "+cYear+"년 "+cMonth+"월 "+cDay+"일");

        initBirthYearComboBox();

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);


        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "SSS");

                if(maleRdBtn.isSelected()) gender = Params.GENDER.MALE;
                else if(femaleRdBtn.isSelected()) gender = Params.GENDER.FEMALE;
                else {
                    Utils.showMessage("Please select gender.");
                }

                //if(birthWeightTxt.getText()=="")
//                    Utils.showMessage("first:"+birthWeightTxt.getText().charAt(0));
                /*
                if((birthWeightTxt.getText()!=null)&&!Utils.isActualValue(birthWeightTxt.getText())) {
                    Utils.showMessage("false");
                }
                */

            }
        });

        birthWeightTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(birthWeightTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if(!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                   e.consume();
                }
            }
        });
    }

    private void initBirthYearComboBox() {
        for(int i=cYear; i>=cYear-Params.BIRTH_YEAR_GAP; i--) {
            birthYearCBox.addItem(Integer.toString(i));

        }
    }

    private void createUIComponents() {
        //JOptionPane.showMessageDialog(null, "createdUI");

    }
}
