package com.attendancesolution.bams.singletonlClasses;

import java.util.HashMap;

/**
 * Created by Akshay on 27-Apr-16.
 */
public class Colors2 {
    HashMap<String, String> colors = new HashMap<>(7);


    public Colors2() {
        colors.put("Monday", "#e44747");
        colors.put("Tuesday", "#47bde4");
        colors.put("Wednesday", "#c1e447");
        colors.put("Thursday", "#e4b247");
        colors.put("Friday", "#EC2790");
        colors.put("Saturday", "#c1e447");
        colors.put("Sunday", "#9ce447");


    }

    public HashMap<String, String> getColors() {
        return colors;
    }

    public void setColors(HashMap<String, String> colors) {
        this.colors = colors;
    }

    public String getColorFromDay(String day) {
        return colors.get(day);
    }
}
