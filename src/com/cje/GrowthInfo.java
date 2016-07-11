package com.cje;

/**
 * Created by Jieun Choi on 6/15/2016.
 *
 * class for data module
 */
public class GrowthInfo {
    private float age;  // by month; should have only tenth except '0'   ex) 0, 0.5, 1.0, 1.5, ....
    private float l;    // L value
    private float m;    // M value
    private float s;    // S value
    private String p3;   // 3%
    private String p5;
    private String p10;
    private String p25;
    private String p50;
    private String p75;
    private String p90;
    private String p95;
    private String p97;

    public float getL() {
        return l;
    }

    public float getM() {
        return m;
    }

    public float getS() {
        return s;
    }

    public String getP3() {
        return p3;
    }

    public String getP5() {
        return p5;
    }

    public String getP10() {
        return p10;
    }

    public String getP25() {
        return p25;
    }

    public String getP50() {
        return p50;
    }

    public String getP75() {
        return p75;
    }

    public String getP90() {
        return p90;
    }

    public String getP95() {
        return p95;
    }

    public String getP97() {
        return p97;
    }

    public float getAge() { return age; }

    public GrowthInfo(float _age, float _l, float _m, float _s,
                      String _p3, String _p5, String _p10, String _p25,
                      String _p50, String _p75, String _p90, String _p95, String _p97) {
        age = _age;
        l = _l;
        m = _m;
        s = _s;
        p3 = _p3;
        p5 = _p5;
        p10 = _p10;
        p25 = _p25;
        p50 = _p50;
        p75 = _p75;
        p90 = _p90;
        p95 = _p95;
        p97 = _p97;
    }


    /*
    private float age;  // by month
    private float l;    // L value
    private float m;    // M value
    private float s;    // S value
    private float p3;   // 3%
    private float p5;
    private float p10;
    private float p25;
    private float p50;
    private float p75;
    private float p90;
    private float p95;
    private float p97;

    public GrowthInfo(float _age, float _l, float _m, float _s,
                      float _p3, float _p5, float _p10, float _p25,
                      float _p50, float _p75, float _p90, float _p95, float _p97) {
        age = _age;
        l = _l;
        m = _m;
        s = _s;
        p3 = _p3;
        p5 = _p5;
        p10 = _p10;
        p25 = _p25;
        p50 = _p50;
        p75 = _p75;
        p90 = _p90;
        p95 = _p95;
        p97 = _p97;
    }
    */
}
