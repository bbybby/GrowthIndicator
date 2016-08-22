package com.cje;

import java.io.File;

/**
 * Created by Jieun Choi on 6/13/2016.
 * Last Updated on 8/22/2016
 *
 * class for static parameters
 */
public class Params {
    public static final String VERSION = "0.9.9 Beta";
    public static final int MAX_INPUT_LENGTH = 6;

    public static final String FIELD_DIVIDER = ",";
    public static final int FIELD_CNT = 13;

    public static final int INIT_VAL = -1;
    public static final int ERROR_CODE = -100;
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
