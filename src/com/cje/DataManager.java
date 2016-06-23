package com.cje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by bbybby on 6/15/2016.
 */
public class DataManager {
    private Map<Params.DataType, Map<String, GrowthInfo>> giMap;
//    List<Params.DataType> dataList = Arrays.of

    /*
    public Map<String, GrowthInfo> weight_male_Map;
    public Map<String, GrowthInfo> weight_female_Map;
    public Map<String, GrowthInfo> height_male_Map;
    public Map<String, GrowthInfo> height_female_Map;
    public Map<String, GrowthInfo> bmi_male_Map;
    public Map<String, GrowthInfo> bmi_female_Map;
    public Map<String, GrowthInfo> head_male_Map;
    public Map<String, GrowthInfo> head_female_Map;
    */

    public DataManager() {
        //giMap = new HashMap<Params.DataType, Map<String, GrowthInfo>>();
        giMap = new HashMap<>();
    }

    // Read data from files
    public boolean loadData() {
        boolean bResult = true;

        for(Params.DataType data : Params.DataType.values()) {
            Map<String, GrowthInfo> map = readFile(data.getFileName());
            if( (map==null)|| (map.size()==0) ) {
                Utils.LOGGER.log(Level.SEVERE, "reading failure of "+data.getFileName());
                giMap.put(data, null);
                bResult = false;
                break;
            }
            else {
                giMap.put(data, map);
            }
        }

        return bResult;
    }

    public Map<String, GrowthInfo> readFile(String filename) {
        Map<String, GrowthInfo> map = new HashMap<>();

        Path file = Paths.get(Params.FILE_PATH+ filename);

        try {
            InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] str = line.split(Params.FIELD_DIVIDER);

                if(str.length != Params.FIELD_CNT) {    // File should have defined numbers of fields
                    Utils.LOGGER.log(Level.SEVERE, "The number of fields("+str.length+
                            ") in the file doesn't match with the requirement("+Params.FIELD_CNT+"). ["+filename+"]");
                    return null;
                }
                GrowthInfo gi = new GrowthInfo(Float.parseFloat(str[0]),
                        Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]),
                        str[4], str[5], str[6], str[7], str[8],
                        str[9], str[10], str[11], str[12]);

                map.put(str[0], gi);
            }
        } catch(NumberFormatException e) {
            Utils.LOGGER.log(Level.SEVERE, e.getMessage());
            map = null;
        } catch(IOException e) {
            Utils.LOGGER.log(Level.SEVERE, e.getMessage());
            map = null;
        } catch(Exception e) {
            Utils.LOGGER.log(Level.SEVERE, e.getMessage());
            map = null;
        }

        return map;
    }

    public GrowthInfo getGrowthInfo(Params.DataType dataType, int ageByDay) {
        int ageByMonth = ageByDay/30;  // By month
        float key = (float)ageByMonth - 0.5f;
        Utils.log("key:"+key);
        Map<String, GrowthInfo> map = giMap.get(dataType);
        GrowthInfo gi = map.get(String.valueOf(key));
        return gi;
    }

    /*
    public DataManager getInstance() {
        Utils.LOGGER.info("return instance of DataManager");
        return new DataManager();
    }
    */
}
