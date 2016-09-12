package com.cje;

/**
 * Created by Jieun Choi on 6/13/2016.
 * Last Updated on 8/22/2016
 *
 * Main class
 */

public class Main {

    public static void main(String[] args) {
        RequestPage mainFrm = new RequestPage();
        mainFrm.initialize();
        mainFrm.addListeners();
        mainFrm.setTitle("GrowthIndicator Ver. " + Params.VERSION);
        mainFrm.setSize(800, 740);
        mainFrm.setLocationRelativeTo(null);
        mainFrm.setVisible(true);
    }
}
