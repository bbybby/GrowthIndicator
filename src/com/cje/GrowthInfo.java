package com.cje;

/**
 * Created by bbybby on 6/15/2016.
 */
public class GrowthInfo {
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
}
