package com.cje;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;

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
    private JButton resetBtn;
    private JButton exitBtn;
    private JPanel resultPanel;
    private JLabel resultTxt;
    private JCheckBox calculateByDayCkBox;
    private JLabel ageLabel;

    private DataManager dm;
    private Params.Gender gender = Params.Gender.NONE;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private int cYear;  // current Year
    private int cMonth;
    private int cDay;

    private float birthWeight;
    private float height;
    private float weight;
    private float head;
    private float bmi;

    private int ageByDay;

    boolean isDataLoaded;
    boolean isCaculatedByDay;

    // Initialize member variables and load data from files
    public void init() {
        birthYear = -1;
        birthMonth = -1;
        birthDay = -1;
        ageByDay = -1;

        birthWeight = -1;
        height = -1;
        weight = -1;
        head = -1;
        bmi = -1;

        // Load Data
        dm = new DataManager();
        isDataLoaded = false;
        isCaculatedByDay = false;

        isDataLoaded = dm.loadData();
        if(!isDataLoaded) {
            Utils.showMessage("Data loading failed!");
            Utils.LOGGER.log(Level.SEVERE, "File reading failed.");
        }
    }

    public RequestPage() {
        super("Growth Indicator");

        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH) + 1;   // since January is 0, Dec. is 11
        cDay = cal.get(Calendar.DAY_OF_MONTH);
        todayTxt.setText("[기준일] "+cYear+"년 "+cMonth+"월 "+cDay+"일");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        init();

        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isDataLoaded) {
                    Utils.showMessage("파일이 정상적으로 로딩되지 않았습니다.");
                    return;
                }

                // Check Gender
                if(maleRdBtn.isSelected()) gender = Params.Gender.MALE;
                else if(femaleRdBtn.isSelected()) gender = Params.Gender.FEMALE;
                else {
                    Utils.showMessage("Please select gender.");
                    return;
                }

                // Check Weight of Birth
                if(birthWeightTxt.getText().length()!=0) {
                    if(!Utils.isActualNumber(birthWeightTxt.getText())) {
                        Utils.showMessage("Please input number correctly");
                        birthWeightTxt.requestFocus();
                        return;
                    }
                    else {
                        birthWeight = Float.parseFloat(birthWeightTxt.getText());
                    }
                }

                // Check Date of Birth
                if(ageByDay<0) {
                    Utils.showMessage("Please input date of birth correctly.");
                    birthDateTxt.requestFocus();
                    return;
                }

                // Check Height value
                if(heightTxt.getText().length()>0) {
                   if(!Utils.isActualNumber(heightTxt.getText())) {
                       Utils.showMessage("Please input number correctly");
                       heightTxt.requestFocus();
                       return;
                   }
                   else {
                       height = Float.parseFloat(heightTxt.getText());
                   }
                }

                // Check Weight value
                if(weightTxt.getText().length()>0) {
                    if(!Utils.isActualNumber(weightTxt.getText())) {
                        Utils.showMessage("Please input number correctly");
                        weightTxt.requestFocus();
                        return;
                    }
                    else {
                        weight = Float.parseFloat(weightTxt.getText());
                    }
                }

                // Check the girth of Head value
                if(headTxt.getText().length()>0) {
                    if(!Utils.isActualNumber(headTxt.getText())) {
                        Utils.showMessage("Please input number correctly");
                        headTxt.requestFocus();
                        return;
                    }
                    else {
                        head = Float.parseFloat(headTxt.getText());
                    }
                }

                // Check BMI value
                if(bmiTxt.getText().length()>0) {
                    if(!Utils.isActualNumber(bmiTxt.getText())) {
                        Utils.showMessage("Please input number correctly");
                        bmiTxt.requestFocus();
                        return;
                    }
                    else {
                        bmi = Float.parseFloat(bmiTxt.getText());
                    }
                }

                showResults();
                //
                /*
                GrowthInfo gi = dm.getGrowthInfo(ageByDay);
                Utils.log("Age:"+ ageByDay/30 +", L:"+gi.getL()+", M:"+gi.getM()+", S:"+gi.getS()+
                    ", "+gi.getP3());
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
                    ageByDay = Utils.getDayBetweenDates(birthYear, birthMonth, birthDay, cYear, cMonth, cDay);
                    if(ageByDay<0) {
                        Utils.showMessage("The Date should be earlier than today");
                        birthDateTxt.requestFocus();
                        return;
                    }
                    int months = ageByDay/30;
                    int years = months/12;
                    int rest_months = months%12;
                    int rest_days = ageByDay%30;
                    String label = months  + "개월   [ ";
                    if(years>0) label += years+"년 ";
                    if(rest_months>0) label += rest_months+"개월";
                    label += " "+rest_days+"일 ]";

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
                if(weightTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if(!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        headTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(headTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if(!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        bmiTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(bmiTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if(!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    birthWeightTxt.setText("");
                    birthDateTxt.setText("");
                    ageTxt.setText("");
                    heightTxt.setText("");
                    weightTxt.setText("");
                    headTxt.setText("");
                    bmiTxt.setText("");
                    resultTxt.setText("");

                    birthYear = -1;
                    birthMonth = -1;
                    birthDay = -1;
                    ageByDay = -1;

                    birthWeight = -1;
                    height = -1;
                    weight = -1;
                    head = -1;
                    bmi = -1;
            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "프로그램을 끝내시겠습니까?","Select an Option", JOptionPane.YES_NO_OPTION)) {
                    System.exit(0);
                }
            }
        });
        calculateByDayCkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(calculateByDayCkBox.isSelected()) {
                    isCaculatedByDay = true;
                }
                else {
                    isCaculatedByDay = false;
                }

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

    private void showResults() {
        Params.DataType dataType;
        //resultTxt.setText("[입력에 대한 결과]");
        String str = "<html><h3>[입력에 대한 결과]</h3>";

        if(birthWeight>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
            else dataType = Params.DataType.WEIGHT_FEMALE;
            GrowthInfo gi = dm.getGrowthInfo(dataType, 0, isCaculatedByDay);
            str += "<br>출생시 체중("+birthWeight+"kg)의 퍼센타일 : ";
            if(gi!=null) {
                float p = Utils.getNormalDistribute(gi, birthWeight);
                str += String.format("%.2f", p * 100) + "p<br>";
            }
            else {
                str += "<br>해당 년령의 데이터가 없습니다.";
            }
        }

        if(height>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.HEIGHT_MALE;
            else dataType = Params.DataType.HEIGHT_FEMALE;
            GrowthInfo gi = dm.getGrowthInfo(dataType, ageByDay, isCaculatedByDay);
            if(gi!=null) {
                float p = Utils.getNormalDistribute(gi, height);
                str += "<br>신장 퍼센타일: " + String.format("%.2f", p * 100) + "p";
            }
            else {
                str += "<br>해당 년령의 신장 데이터가 없습니다.";
            }
        }

        if(weight>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
            else dataType = Params.DataType.WEIGHT_FEMALE;
            GrowthInfo gi = dm.getGrowthInfo(dataType, ageByDay, isCaculatedByDay);
            if(gi!=null) {
                float p = Utils.getNormalDistribute(gi, weight);
                str += "<br>체중 퍼센타일: " + String.format("%.2f",p*100) + "p";
            }
            else {
                str += "<br>해당 년령의 체중 데이터가 없습니다.";
            }
        }

        if(head>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.HEAD_MALE;
            else dataType = Params.DataType.HEAD_FEMALE;
            GrowthInfo gi = dm.getGrowthInfo(dataType, ageByDay, isCaculatedByDay);
            if(gi!=null) {
                float p = Utils.getNormalDistribute(gi, head);
                str += "<br>두위 퍼센타일: " + String.format("%.2f",p*100) + "p";
            }
            else {
                str += "<br>해당 년령의 두위 데이터가 없습니다.";
            }
        }

        if(bmi>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.BMI_MALE;
            else dataType = Params.DataType.BMI_FEMALE;
            GrowthInfo gi = dm.getGrowthInfo(dataType, ageByDay, isCaculatedByDay);
            if(gi!=null) {
                float p = Utils.getNormalDistribute(gi, bmi);
                str += "<br>BMI 퍼센타일: " + String.format("%.2f",p*100) + "p";
            }
            else {
                str += "<br>해당 년령의 BMI 데이터가 없습니다.";
            }
        }
        resultTxt.setText(str+"</html>");
    }
}
