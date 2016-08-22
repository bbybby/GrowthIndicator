package com.cje;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private JFormattedTextField birthHeightTxt;
    private JLabel ageTxt;
    private JLabel baseDateTxt;
    private JFormattedTextField birthDateTxt;
    private JButton resetBtn;
    private JButton exitBtn;
    private JPanel resultPanel;
    private JLabel resultTxt;
    private JCheckBox calculateByDayCkBox;
    private JButton changeDateBtn;
    private JLabel ageLabel;

    private DataManager dm;
    private ResultView rv;
    private Params.Gender gender = Params.Gender.NONE;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private LocalDate baseDate;

    private float birthWeight;
    private float birthHeight;
    private float height;
    private float weight;
    private float head;
    private float bmi;

    private int ageByDay;

    boolean isDataLoaded;
    boolean isCalculatedByDay;

    // Initialize member variables
    private void initParam() {
        birthYear = Params.INIT_VAL;
        birthMonth = Params.INIT_VAL;
        birthDay = Params.INIT_VAL;
        ageByDay = Params.INIT_VAL;

        birthWeight = Params.INIT_VAL;
        birthHeight = Params.INIT_VAL;
        height = Params.INIT_VAL;
        weight = Params.INIT_VAL;
        head = Params.INIT_VAL;
        bmi = Params.INIT_VAL;

        setBaseDate(LocalDate.now());
    }

    public RequestPage() {
        super("Growth Indicator");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    // initialize variables and the objects
    public void initialize() {
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

    // Returns valid input otherwise returns ERROR CODE
    private float getValidInput(JTextField txtField) {
        if (txtField.getText().length() != 0) {
            if (!Utils.isActualNumber(txtField.getText())) {
                Utils.showMessage("Please input valid number");
                txtField.requestFocus();
                return Params.ERROR_CODE;
            } else {
                return Float.parseFloat(txtField.getText());
            }
        } else {    // NO INPUT
           return Params.INIT_VAL;
        }
    }

    public void addListeners() {
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isDataLoaded) {
                    Utils.showMessage("파일이 정상적으로 로딩되지 않았습니다.");
                    return;
                }

                // Check Gender
                if (maleRdBtn.isSelected()) gender = Params.Gender.MALE;
                else if (femaleRdBtn.isSelected()) gender = Params.Gender.FEMALE;
                else {
                    Utils.showMessage("Please select gender.");
                    return;
                }

                float val;
                // Get Weight at Birth
                val = getValidInput(birthWeightTxt);
                if(val == Params.ERROR_CODE) return;
                else birthWeight = val;

                // Get Height at Birth
                val = getValidInput(birthHeightTxt);
                if(val == Params.ERROR_CODE) return;
                else birthHeight = val;

                // Get Height value
                val = getValidInput(heightTxt);
                if(val == Params.ERROR_CODE) return;
                else height = val;

                // Get Weight value
                val = getValidInput(weightTxt);
                if(val == Params.ERROR_CODE) return;
                else weight = val;

                // Get Head value
                val = getValidInput(headTxt);
                if(val == Params.ERROR_CODE) return;
                else head = val;

                // Get BMI value
                val = getValidInput(bmiTxt);
                if(val == Params.ERROR_CODE) return;
                else bmi = val;

                // Check Date of Birth
                if (ageByDay < 0) {
                    Utils.showMessage("Please input date of birth correctly.");
                    birthDateTxt.requestFocus();
                    return;
                }

                showResults();
            }
        });

        class LocalKeyAdapter extends KeyAdapter {
            JTextField txtField;

            public LocalKeyAdapter(JTextField txtField) {
                this.txtField = txtField;
            }

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (txtField.getText().length() >= Params.MAX_INPUT_LENGTH) {
                    e.consume();
                    return;
                }

                if (!Utils.isDecimalInput(e.getKeyChar())) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        }

        birthWeightTxt.addKeyListener(new LocalKeyAdapter(birthWeightTxt));
        heightTxt.addKeyListener(new LocalKeyAdapter(heightTxt));
        weightTxt.addKeyListener(new LocalKeyAdapter(weightTxt));
        headTxt.addKeyListener(new LocalKeyAdapter(headTxt));
        bmiTxt.addKeyListener(new LocalKeyAdapter(bmiTxt));

        birthDateTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int y, m, d;

                String[] str = birthDateTxt.getText().replaceAll("_", "").split("/");
                try {
                    y = Integer.parseInt(str[0]);
                    m = Integer.parseInt(str[1]);
                    d = Integer.parseInt(str[2]);
                } catch (Exception ex) {
                    birthDateTxt.setText("");
                    birthDateTxt.requestFocus();
                    return;
                }

                if (Utils.isValidDate(y, m, d)) {
                    LocalDate birthday = LocalDate.of(y, m, d);
                    int gap = (int) ChronoUnit.DAYS.between(birthday, baseDate);

                    if (gap < 0) {
                        Utils.showMessage("The Date should be earlier than the Base Date");
                        birthDateTxt.requestFocus();
                        return;
                    }

                    // Setting valid Date of Birth & Age
                    birthYear = y;
                    birthMonth = m;
                    birthDay = d;
                    ageByDay = gap;
                    ageTxt.setText(Utils.getAgeLabel(birthday, baseDate, ageByDay));
                } else {
                    Utils.showMessage("Please check the date");
                    birthDateTxt.requestFocus();
                    return;
                }
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    birthWeightTxt.setText("");
                    birthHeightTxt.setText("");
                    birthDateTxt.setText("");
                    ageTxt.setText("");
                    heightTxt.setText("");
                    weightTxt.setText("");
                    headTxt.setText("");
                    bmiTxt.setText("");

                    initParam();
                    cleanView();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "프로그램을 끝내시겠습니까?", "Select an Option", JOptionPane.YES_NO_OPTION)) {
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

        resultTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // copy the information to the clipboard (for pasting into the doctor's chart)
                if(e.getClickCount() == 2) {
                    String strInfo = rv.getInformationNotes();
                    StringSelection stringSelection = new StringSelection(strInfo);
                    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clpbrd.setContents(stringSelection, null);
                }
            }
        });

        changeDateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangeDateDialog();
            }
        });

        birthHeightTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(birthHeightTxt.getText().length()>=Params.MAX_INPUT_LENGTH) {
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

    private void setBaseDate(LocalDate newDate) {
        baseDate = newDate;
        baseDateTxt.setText("[기준일] " + baseDate.getYear() + "년 "
                + baseDate.getMonthValue() + "월 " + baseDate.getDayOfMonth() + "일");
        if(ageByDay != Params.INIT_VAL) {    // System already has the Birth date and Age, so we need to update
            LocalDate birthday = LocalDate.of(birthYear, birthMonth, birthDay);
            ageByDay = (int) ChronoUnit.DAYS.between(birthday, baseDate);

            ageTxt.setText(Utils.getAgeLabel(birthday, baseDate, ageByDay));
        }
    }

    private void showChangeDateDialog() {
        String msg = "기준일을 입력해 주세요";
        MaskFormatter mf;

        try {
            mf = new MaskFormatter("####/##/##");
            mf.setValidCharacters("0123456789");
            mf.setPlaceholderCharacter('_');
            mf.setAllowsInvalid(false);
        }
        catch (ParseException e) {
            Utils.LOGGER.log(Level.SEVERE, "Error in showChangeDateDialog");
            Utils.showMessage("Parsing Error");
            return;
        }
        JFormattedTextField dateTxt = new JFormattedTextField(mf);
        JPanel panel = new JPanel(new FlowLayout());    // for adjusting the size of dateTxt
        dateTxt.setColumns(0);
        panel.add(dateTxt);

        Object [] array = { msg, panel };
        Object [] options = { "OK", "Cancel"};

        JOptionPane optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                null, options, options[0]);

        final JDialog dialog = new JDialog(new JFrame(), "Click a button", true);
        dialog.setTitle("기준일 변경");
        dialog.setLocationRelativeTo(this);

        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String prop = evt.getPropertyName();

                if (isVisible() && (evt.getSource() == optionPane)
                        && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                        JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    //Reset the JOptionPane's value.
                    //If you don't do this, then if the user presses the same button next time,
                    //no property change event will be fired.
                    optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

                    if(value.toString().equals(options[0])) {
                        int y, m, d;
                        String[] str = dateTxt.getText().replaceAll("_","").split("/");
                        try {
                            y = Integer.parseInt(str[0]);
                            m = Integer.parseInt(str[1]);
                            d = Integer.parseInt(str[2]);
                        } catch(Exception ex) {
                            dateTxt.setText("");
                            dateTxt.requestFocus();

                            return;
                        }
                        if(Utils.isValidDate(y, m, d)) {
                            if(ageByDay != Params.INIT_VAL) {    // If the system has Birth Date and Age
                                int gap = (int) ChronoUnit.DAYS.between(LocalDate.of(birthYear, birthMonth, birthDay),
                                        LocalDate.of(y,m,d));

                                if(gap<0) {
                                    Utils.showMessage("The Date should be later than the Birth Date");
                                    birthDateTxt.requestFocus();
                                    return;
                                }

                            }

                            setBaseDate(LocalDate.of(y, m, d));
                            cleanView();
                            Utils.showMessage("기준일이 변경되었습니다.");
                            dialog.dispose();
                        }
                        else {
                            Utils.showMessage("Please check the date");
                        }
                    }
                    else {
                        dialog.dispose();
                    }
                }
            }
        });

        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createUIComponents() throws ParseException {
        // Input Formatting Date of Birth field
        MaskFormatter mf = new MaskFormatter("####/##/##");
        mf.setValidCharacters("0123456789");
        mf.setPlaceholderCharacter('_');
        mf.setAllowsInvalid(false);
        birthDateTxt = new JFormattedTextField(mf);
    }

    private  void cleanView() {
        resultTxt.setText("");
        rv.cleanView();
    }

    private void showResults() {
        Params.DataType dataType;
        GrowthInfo gi;
        float p;

        if(birthWeight != Params.INIT_VAL) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
            else dataType = Params.DataType.WEIGHT_FEMALE;
            gi = dm.getGrowthInfo(dataType, 0, isCalculatedByDay);
            if(gi!=null) {
                p = Utils.getNormalDistribute(gi, birthWeight);
            }
            else {
                p = Params.INIT_VAL;
            }
            rv.setBirthWeight(birthWeight, p);
        }

        if(birthHeight != Params.INIT_VAL) {
            if(gender==Params.Gender.MALE) dataType = Params.DataType.HEIGHT_MALE;
            else dataType = Params.DataType.HEIGHT_FEMALE;
            gi = dm.getGrowthInfo(dataType, 0, isCalculatedByDay);
            if(gi!=null) {
                p = Utils.getNormalDistribute(gi, birthHeight);
            }
            else {
                p = Params.INIT_VAL;
            }
            rv.setBirthHeight(birthHeight, p);
        }

        // Getting Height data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.HEIGHT_MALE;
        else dataType = Params.DataType.HEIGHT_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (height != Params.INIT_VAL)) {
            p = Utils.getNormalDistribute(gi, height);
        }
        else {
           p = Params.INIT_VAL;
        }
        rv.setHeight(gi, height, p);

        // Getting Weight data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.WEIGHT_MALE;
        else dataType = Params.DataType.WEIGHT_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (weight != Params.INIT_VAL)) {
            p = Utils.getNormalDistribute(gi, weight);
        }
        else {
            p = Params.INIT_VAL;
        }
        rv.setWeight(gi, weight, p);

        // Getting Head data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.HEAD_MALE;
        else dataType = Params.DataType.HEAD_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (head != Params.INIT_VAL)) {
            p = Utils.getNormalDistribute(gi, head);
        }
        else {
            p = Params.INIT_VAL;
        }
        rv.setHead(gi, head, p);

        // Getting BMI data
        if(gender==Params.Gender.MALE) dataType = Params.DataType.BMI_MALE;
        else dataType = Params.DataType.BMI_FEMALE;
        gi = dm.getGrowthInfo(dataType, ageByDay, isCalculatedByDay);
        if((gi!=null) && (bmi != Params.INIT_VAL)) {
            p = Utils.getNormalDistribute(gi, bmi);
        }
        else {
            p = Params.INIT_VAL;
        }
        rv.setBmi(gi, bmi, p);

        resultTxt.setText(rv.getResultView(gender, ageByDay));
    }
}
