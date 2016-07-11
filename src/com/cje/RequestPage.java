package com.cje;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

/**
 * Created by Jieun Choi on 6/8/2016.
 *
 * class for User interface
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
    private ResultView rv;
    private Params.Gender gender = Params.Gender.NONE;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private LocalDate today;
    private int cMonth;
    private int cDay;

    private float birthWeight;
    private float height;
    private float weight;
    private float head;
    private float bmi;

    private int ageByDay;

    boolean isDataLoaded;
    boolean isCalculatedByDay;

    // Initialize member variables
    private void initParam() {
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

    private void init() {
        initParam();

        dm = new DataManager();
        rv = new ResultView();

        // Load Data
        isDataLoaded = false;
        isCalculatedByDay = true;

        isDataLoaded = dm.loadData();
        if(!isDataLoaded) {
            Utils.showMessage("Data loading failed!");
            Utils.LOGGER.log(Level.SEVERE, "File reading failed.");
        }
    }

    public RequestPage() {
        super("Growth Indicator");

        today = LocalDate.now();
        todayTxt.setText("[기준일] "+today.getYear()+"년 "+today.getMonthValue()+"월 "+today.getDayOfMonth()+"일");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        // initialize variables and the objects
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

                // Check Weight at Birth
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
                else {
                    birthWeight = -1;
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
                else {
                   height = -1;
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
                else {
                    weight = -1;
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
                else {
                    head = -1;
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
                else {
                    bmi = -1;
                }

                showResults();
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
                   Utils.log("Birth year:"+birthYear +", month:"+birthMonth+", day:"+birthDay);
                } catch(Exception ex) {
                    birthDateTxt.setText("");
                    birthDateTxt.requestFocus();
                    return;
                }

                if(Utils.isValidDate(birthYear, birthMonth, birthDay)) {
                    LocalDate birthday = LocalDate.of(birthYear, birthMonth, birthDay);
                    ageByDay = (int) ChronoUnit.DAYS.between(birthday, today);

                    Utils.log("LocalDate]"+birthday.getYear()+":"+birthday.getMonthValue()+":"+birthday.getDayOfMonth()+":"+birthday.getDayOfWeek());

                    if(ageByDay<0) {
                        Utils.showMessage("The Date should be earlier than today");
                        birthDateTxt.requestFocus();
                        return;
                    }

                    ageTxt.setText(Utils.getAgeLabel(birthday, today, ageByDay));
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

                    initParam();
                    rv.cleanView();
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
                    isCalculatedByDay = true;
                }
                else {
                    isCalculatedByDay = false;
                }

            }
        });
    }


    private void createUIComponents() throws ParseException {
        // Input Formatting Date of Birth field
        MaskFormatter mf = new MaskFormatter("####/##/##");
        mf.setValidCharacters("0123456789");
        mf.setPlaceholderCharacter('_');
        mf.setAllowsInvalid(false);
        birthDateTxt = new JFormattedTextField(mf);
    }

    private void showResults() {
        Params.DataType dataType;
        GrowthInfo gi;
        float p;

        if(birthWeight>0) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
            else dataType = Params.DataType.WEIGHT_FEMALE;
            gi = dm.getGrowthInfo(dataType, 0, isCalculatedByDay);
            if(gi!=null) {
                p = Utils.getNormalDistribute(gi, birthWeight);
            }
            else {
                p = -1;
            }
            rv.setBirthWeight(birthWeight, p);
        }

        // Getting Height data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.HEIGHT_MALE;
        else dataType = Params.DataType.HEIGHT_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (height>0)) {
            p = Utils.getNormalDistribute(gi, height);
        }
        else {
           p = -1;
        }
        rv.setHeight(gi, height, p);

        // Getting Weight data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
        else dataType = Params.DataType.WEIGHT_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (weight>0)) {
            p = Utils.getNormalDistribute(gi, weight);
        }
        else {
            p = -1;
        }
        rv.setWeight(gi, weight, p);

        // Getting Head data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.HEAD_MALE;
        else dataType = Params.DataType.HEAD_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (head>0)) {
            p = Utils.getNormalDistribute(gi, head);
        }
        else {
            p = -1;
        }
        rv.setHead(gi, head, p);

        // Getting BMI data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.BMI_MALE;
        else dataType = Params.DataType.BMI_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (bmi>0)) {
            p = Utils.getNormalDistribute(gi, bmi);
        }
        else {
            p = -1;
        }
        rv.setBmi(gi, bmi, p);

        resultTxt.setText(rv.getResultView(gender, ageByDay));
    }
}
