package com.cje;

public class Main {

    public static void main(String[] args) {
        RequestPage mainFrm = new RequestPage();
        mainFrm.initialize();
        mainFrm.addListeners();
        mainFrm.setTitle("GrowthIndicator Ver. " + Params.VERSION);
        mainFrm.setSize(800, 720);
        mainFrm.setVisible(true);
    }
}
