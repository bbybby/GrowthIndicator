package com.cje;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.MaskFormatter;
import javax.xml.crypto.Data;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
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
    private JFormattedTextField heightTxt;
    private JFormattedTextField weightTxt;
    private JFormattedTextField headTxt;
    private JFormattedTextField bmiTxt;
    private JButton viewBtn;
    private JFormattedTextField birthWeightTxt;
    private JLabel ageTxt;
    private JLabel todayTxt;
    private JFormattedTextField birthDateTxt;
    private JLabel ageLabel;

    private DataManager dm;
    private Params.GENDER gender = Params.GENDER.NONE;
    private int birthYear;
    private int birthMonth;
    private int birthDay;

    private int cYear;  // current Year
    private int cMonth;
    private int cDay;

    private int ageByMonth;

    boolean isDataLoaded;

    public RequestPage() {
        super("Growth Indicator");

        // Initialize member variables
        dm = new DataManager();
        isDataLoaded = false;

        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH) + 1;   // since January is 0, Dec. is 11
        cDay = cal.get(Calendar.DAY_OF_MONTH);
        todayTxt.setText("[기준일] "+cYear+"년 "+cMonth+"월 "+cDay+"일");

        birthYear = -1;
        birthMonth = -1;
        birthDay = -1;
        ageByMonth = -1;


        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);


        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "SSS");

                // Check Gender
                if(maleRdBtn.isSelected()) gender = Params.GENDER.MALE;
                else if(femaleRdBtn.isSelected()) gender = Params.GENDER.FEMALE;
                else {
                    Utils.showMessage("Please select gender.");
                    return;
                }

                // Check Date of Birth
                if(ageByMonth<0) {
                    Utils.showMessage("Please input date of birth correctly.");
                    birthDateTxt.requestFocus();
                    return;
                }

                // Init DataManager component
                if(!isDataLoaded) {
                    boolean bRes = dm.loadData();
                }
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
        birthDateTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                String[] str = birthDateTxt.getText().replaceAll("_","").split("/");
                try {
                    birthYear = Integer.parseInt(str[0]);
                    birthMonth = Integer.parseInt(str[1]);
                    birthDay = Integer.parseInt(str[2]);
                } catch(Exception ex) {
                    Utils.log("focust Lost: "+ex.getMessage());
                    birthDateTxt.setText("");
                    birthDateTxt.requestFocus();
                    return;
                }

                if(Utils.isValidDate(birthYear, birthMonth, birthDay)) {
                    int days = Utils.getDayBetweenDates(birthYear, birthMonth, birthDay, cYear, cMonth, cDay);
                    if(days<0) {
                        Utils.showMessage("The Date should be earlier than today");
                        birthDateTxt.requestFocus();
                        return;
                    }
                    ageByMonth = days/30;
                    String label = ageByMonth  + "개월";
                    if(ageByMonth>12) {
                        int years = ageByMonth/12;
                        int months = ageByMonth%12;
                        label += "  ["+years+"년 "+months+"개월]";
                    }
                    ageTxt.setText(label);
                }
                else {
                    Utils.showMessage("Please check the date");
                    birthDateTxt.requestFocus();
                    return;
                }
            }
        });
        heightTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(heightTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if(!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        weightTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
    }


    private void createUIComponents() throws ParseException {
        MaskFormatter mf = new MaskFormatter("####/##/##");
        mf.setValidCharacters("0123456789");
        mf.setPlaceholderCharacter('_');
        mf.setAllowsInvalid(false);
        birthDateTxt = new JFormattedTextField(mf);
    }
}
