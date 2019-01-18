package com.attendancesolution.bams.singletonlClasses;

import java.util.HashMap;

/**
 * Created by Akshay on 26-Apr-16.
 */
public class Colors {

    HashMap<String,String> colors = new HashMap<>(26);

    public Colors(){
        colors.put("A","#e44747");
        colors.put("B","#e46447");
        colors.put("C","#e48247");
        colors.put("D","#e4b247");
        colors.put("E","#e4e247");
        colors.put("F","#c1e447");
        colors.put("G","#9ce447");
        colors.put("H","#68e447");
        colors.put("I","#47e44e");
        colors.put("J","#47e482");
        colors.put("K","#47e4ae");
        colors.put("L","#47e4db");
        colors.put("M","#47bde4");
        colors.put("N","#4794e4");
        colors.put("O","#477ae4");
        colors.put("P","#4752e4");
        colors.put("Q","#6147e4");
        colors.put("R","#8647e4");
        colors.put("S","#a747e4");
        colors.put("T","#cc47e4");
        colors.put("U","#e447d7");
        colors.put("V","#e447ab");
        colors.put("W","#e49f47");
        colors.put("X","#1fb51b");
        colors.put("Y","#1bb5a1");
        colors.put("Z","#8b1bb5");

    }

    public HashMap<String, String> getColors() {
        return colors;
    }

    public void setColors(HashMap<String, String> colors) {
        this.colors = colors;
    }

    public String getColorFromChar(String character){
        return colors.get(character);
    }
}
