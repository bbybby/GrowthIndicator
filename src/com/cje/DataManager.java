package com.cje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by bbybby on 6/15/2016.
 */
public class DataManager {
    private Map<Params.DataType, Map<String, GrowthInfo>> giMap;

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

    /*
     */
    public GrowthInfo getGrowthInfo(Params.DataType dataType, int ageByDay, boolean isCalculatedByDay) {
        GrowthInfo gi = null;
        Map<String, GrowthInfo> map = giMap.get(dataType);
        float medianMonthKey = (float)((int)(ageByDay/30))+ 0.5f;

        if(ageByDay == 0) {  // at birth
            gi = map.get("0");
            return gi;
        }

        if(isCalculatedByDay) { // get approximate data based on the age by days
            gi = getAdjustedGrowthInfo(map, ageByDay, medianMonthKey);
            Utils.LOGGER.info("Getting approximate value by days ["+ageByDay+"]");
        }
        else {  // get data based on the age by months
            gi = map.get(String.valueOf(medianMonthKey));
            Utils.LOGGER.info("Getting data from key value");
        }
        return gi;
    }

    private GrowthInfo getAdjustedGrowthInfo(Map<String, GrowthInfo> map, int ageByDay, float medianMonthKey) {
        GrowthInfo gi = map.get(String.valueOf(medianMonthKey));
        GrowthInfo gi_neighbor = null;
        GrowthInfo gi_result  = null;
        int keyDays = (int)medianMonthKey*30;

        if(gi == null) {    // We don't need to go further
            return null;
        }

        if(ageByDay == keyDays) {   // we don't need to get approximate value
            return gi;
        }
        else if(ageByDay < keyDays) {   // use the data of a month younger age
            float youngerAge = medianMonthKey - 1.0f;
            gi_neighbor = map.get(String.valueOf(youngerAge));
        }
        else {  // use the data of a month older age
            float olderAge = medianMonthKey + 1.0f;
            gi_neighbor = map.get(String.valueOf(olderAge));
        }

        gi_result = getApproximateValue(gi, gi_neighbor, ageByDay);

        if(gi_result == null) {   //
            Utils.showMessage("근사값을 구할 수 없어서 기준월령값("+medianMonthKey+"개월)으로 표시합니다.");
            Utils.LOGGER.log(Level.WARNING,
                    "Couldn't get neighbor data for approximate value [Key of age by months:"+medianMonthKey+"]");
            return gi;  // data searched by median key value
        }


        return gi_result;
    }

    /*
     getting the approximate value using a linear equation (y = mx + b)
      */
    private GrowthInfo getApproximateValue(GrowthInfo gi, GrowthInfo gi_neighbor, int ageByDay) {
        if(gi_neighbor == null) return null;
        if(gi == null) {
            Utils.LOGGER.log(Level.WARNING, "Object of GrowthInfo should not be null in getApproximateValue()");
            return null;
        }

        float age, l, m, s;
        float p3, p5, p10, p25, p50, p75, p90, p95, p97;
        float x1 = gi.getAge() * 30;    // key age by days of gi
        float x2 = gi_neighbor.getAge() * 30;   // key age by days of gi_neighbor

        age = Math.round(((float)ageByDay/30)*10)/10;
        l = Utils.getApproximateValue(x1, gi.getL(), x2, gi_neighbor.getL(), ageByDay);
        m = Utils.getApproximateValue(x1, gi.getM(), x2, gi_neighbor.getM(), ageByDay);
        s = Utils.getApproximateValue(x1, gi.getS(), x2, gi_neighbor.getS(), ageByDay);
        p3 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP3()), x2, Float.parseFloat(gi_neighbor.getP3()), ageByDay);
        p5 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP5()), x2, Float.parseFloat(gi_neighbor.getP5()), ageByDay);
        p10 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP10()), x2, Float.parseFloat(gi_neighbor.getP10()), ageByDay);
        p25 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP25()), x2, Float.parseFloat(gi_neighbor.getP25()), ageByDay);
        p50 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP50()), x2, Float.parseFloat(gi_neighbor.getP50()), ageByDay);
        p75 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP75()), x2, Float.parseFloat(gi_neighbor.getP75()), ageByDay);
        p90 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP90()), x2, Float.parseFloat(gi_neighbor.getP90()), ageByDay);
        p95 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP95()), x2, Float.parseFloat(gi_neighbor.getP95()), ageByDay);
        p97 = Utils.getApproximateValue(x1, Float.parseFloat(gi.getP97()), x2, Float.parseFloat(gi_neighbor.getP97()), ageByDay);

        GrowthInfo result_gi = new GrowthInfo(age, l, m, s,
                        String.format("%.2f", p3),
                        String.format("%.2f", p5),
                        String.format("%.2f", p10),
                        String.format("%.2f", p25),
                        String.format("%.2f", p50),
                        String.format("%.2f", p75),
                        String.format("%.2f", p90),
                        String.format("%.2f", p95),
                        String.format("%.2f", p97) );

        return result_gi;
    }

    /*
    public DataManager getInstance() {
        Utils.LOGGER.info("return instance of DataManager");
        return new DataManager();
    }
    */
}
