package com.cje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by bbybby on 6/15/2016.
 */
public class DataManager {
    public ArrayList<GrowthInfo> weight_male_List = new ArrayList<GrowthInfo>();
    public DataManager() { }

    public boolean loadData() {
        boolean bResult = false;
        Path file = Paths.get(Params.dataPath + "GI_weight_male.csv");

        try {
            InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            int i=0;
            while ((line = reader.readLine()) != null) {
                String[] str = line.split(Params.FIELD_DIVIDER);
                float[] f = new float[Params.FIELD_CNT];
                /*
                GrowthInfo gi = new GrowthInfo(str[0], str[1], str[2], str[3], str[4],
                            str[5], str[6], str[7], str[8], str[9],
                            str[10], str[11], str[12]);
                weight_male_List.add()
                */
                i++;
                if(i==5) break;
            }
        } catch (IOException x) {
            System.err.println(x);
        }

        return bResult;
    }

    /*
    public DataManager getInstance() {
        Utils.LOGGER.info("return instance of DataManager");
        return new DataManager();
    }
    */
}
