package com.cje;

/**
 * Created by bbybby on 6/13/2016.
 */
public class Params {
    public static final int MAX_INPUT_LENGTH = 6;
//    public static final int BIRTH_YEAR_GAP = 20;

    public static final String FILE_PATH = "C:\\Users\\bbybby\\Works\\Programming\\Projects\\GrowthIndicator\\data\\";
    public static final String FIELD_DIVIDER = ",";
    public static final int FILE_CNT = 8;
    public static final int FIELD_CNT = 13;
    public enum Gender { MALE, FEMALE, NONE };
    public enum DataType {
        HEIGHT_MALE("GI_height_male.csv"), HEIGHT_FEMALE("GI_height_female.csv"),
        WEIGHT_MALE("GI_weight_male.csv"), WEIGHT_FEMALE("GI_weight_female.csv"),
        HEAD_MALE("GI_head_male.csv"), HEAD_FEMALE("GI_head_female.csv"),
        BMI_MALE("GI_bmi_male.csv"), BMI_FEMALE("GI_bmi_female.csv");

        String fileName;

        DataType(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return this.fileName;
        }
    };
}
