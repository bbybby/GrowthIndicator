package com.cje;

/**
 * Created by Jieun Choi on 7/4/2016.
 *
 * class for result page view
 */
public class ResultView {
    private static final String STYLE = "<style>"
        + "table, th, td { border-collapse: collapse; border: 1px solid black; } "
        + "th { padding: 8px; background-color: #99ccff; } "
        + "td { padding: 5px; }"
        + "body { font-size: 11px; }"
        + "</style>";

    private static final String HEADER = "<html><head>"+STYLE+"</head><body>";
    private static final String TAIL = "</body></html>";
    private static final String NO_DATA_MSG = "해당 년령<br>데이터 없음";

    private float birthWeight;
    private String birthWeightPercentile;
    private float height;
    private String heightPercentile;
    private float weight;
    private String weightPercentile;
    private float head;
    private String headPercentile;
    private float bmi;
    private String bmiPercentile;

    private GrowthInfo gi_height;
    private GrowthInfo gi_weight;
    private GrowthInfo gi_head;
    private GrowthInfo gi_bmi;

    public ResultView() { initParam(); }

    private void initParam() {
        birthWeight = -1;
        birthWeightPercentile = "";
        height = -1;
        heightPercentile = "";
        weight = -1;
        weightPercentile = "";
        head = -1;
        headPercentile = "";
        bmi = -1;
        bmiPercentile = "";

        gi_height = null;
        gi_weight = null;
        gi_head = null;
        gi_bmi = null;
    }

    public void setBirthWeight(float bw, float p) {
        birthWeight = bw;
        if(p == -1) {
            if(birthWeight>0) birthWeightPercentile = NO_DATA_MSG;
            else birthWeightPercentile = "";
        }
        else birthWeightPercentile = String.format("%.2f",p*100) + "p";
    }

    public void setHeight(GrowthInfo gi, float h, float p) {
        gi_height = gi;
        height = h;
        if(p == -1) {
            if(height>0) heightPercentile = NO_DATA_MSG;
            else heightPercentile = "";
        }
        else heightPercentile = String.format("%.2f",p*100) + "p";
    }

    public void setWeight(GrowthInfo gi, float w, float p) {
        gi_weight = gi;
        weight = w;
        if(p == -1) {
            if(weight>0) weightPercentile = NO_DATA_MSG;
            else weightPercentile = "";
        }
        else weightPercentile = String.format("%.2f",p*100) + "p";
    }

    public void setHead(GrowthInfo gi, float hd, float p) {
        gi_head = gi;
        head = hd;
        if(p == -1) {
            if(head>0) headPercentile = NO_DATA_MSG;
            else headPercentile = "";
        }
        else headPercentile = String.format("%.2f",p*100) + "p";
    }

    public void setBmi(GrowthInfo gi, float b, float p) {
        gi_bmi = gi;
        bmi = b;
        if(p == -1) {
            if(bmi>0) bmiPercentile =  "해당 년령<br>데이터 없음";
            else bmiPercentile = "";
        }
        else bmiPercentile = String.format("%.2f",p*100) + "p";
    }

    private String getMeasuredDataTable() {
        String strView = "<table> <tr>"
                + "<th></th> <th>출생체중 (kg)</th> <th>신장 (cm)</th> <th>체중 (kg)</th> <th>두위 (cm)</th> <th>BMI</th>"
                + "</tr>";
        strView += "<tr><th>측정치</th><td>";
        if(birthWeight>0) strView += birthWeight;
        strView += "</td><td>";
        if(height>0) strView += height;
        strView += "</td><td>";
        if(weight>0) strView += weight;
        strView += "</td><td>";
        if(head>0) strView += head;
        strView += "</td><td>";
        if(bmi>0) strView += bmi;
        strView += "</td></tr>"
                + "<tr><th>퍼센타일</th>"
                + "<td>" + birthWeightPercentile + "</td>"
                + "<td>" + heightPercentile + "</td>"
                + "<td>" + weightPercentile + "</td>"
                + "<td>" + headPercentile + "</td>"
                + "<td>" + bmiPercentile + "</td></tr>"
                + "</table>";

        return strView;
    }

    private  String getPercentileRow(GrowthInfo g) {
        GrowthInfo gi = g;
        String str;
        if(gi != null) {
            str = "<td>" + gi.getP3() + "</td>"
                    + "<td>" + gi.getP5() + "</td>"
                    + "<td>" + gi.getP10() + "</td>"
                    + "<td>" + gi.getP25() + "</td>"
                    + "<td>" + gi.getP50() + "</td>"
                    + "<td>" + gi.getP75() + "</td>"
                    + "<td>" + gi.getP90() + "</td>"
                    + "<td>" + gi.getP95() + "</td>"
                    + "<td>" + gi.getP97() + "</td>";
        }
        else {
            str = "<td colspan=9>해당 년령의 데이터가 없습니다.</td>";
        }

        return str;
    }

    private String getPercentileTable() {
        String strView = "<table><tr>"
                + "<th></th><th>3p</th><th>5p</th><th>10p</th><th>25p</th>"
                + "<th>50p</th><th>75p</th><th>90p</th><th>95p</th><th>97p</th></tr>";
        strView += "<tr><th>신장</th>" + getPercentileRow(gi_height) + "</tr>";
        strView += "<tr><th>체중</th>" + getPercentileRow(gi_weight) + "</tr>";
        strView += "<tr><th>두위</th>" + getPercentileRow(gi_head) + "</tr>";
        strView += "<tr><th>BMI</th>" + getPercentileRow(gi_bmi) + "</tr>";
        strView += "</table>";

        return strView;
    }

    public String getResultView(Params.Gender gender, int ageByDay) {
        String strView = HEADER;

        strView += "[ 나이:" + ageByDay + "일, 성별:";
        if(gender == Params.Gender.MALE) strView += "남";
        else if(gender == Params.Gender.FEMALE) strView += "여";
        strView += " ] 성장 지표";

        strView += getMeasuredDataTable();

        strView += "<br>" + getPercentileTable();

        strView += TAIL;

        return strView;
    }

    public void cleanView() {
        initParam();
    }
}
