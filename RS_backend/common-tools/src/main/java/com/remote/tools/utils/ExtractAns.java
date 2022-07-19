package com.remote.tools.utils;

import java.text.DecimalFormat;

public class ExtractAns {
    public byte[] map;
    public float rate;

    public static ExtractAns GetAns(byte[] map,float rate){
        ExtractAns ans=new ExtractAns();
        ans.map=map;
        ans.rate=rate;
        return ans;
    }

    public static float GetRate(String str){
        String[] cells = str.split("\n");
        String tmp=cells[cells.length-1];
        float res=Float.parseFloat(tmp);

        //保留四位小数
        DecimalFormat df = new DecimalFormat("#.0000");
        return Float.parseFloat(df.format(res));
    }

    public static float GetRate(float f) {
        //保留四位小数
        DecimalFormat df = new DecimalFormat("#.0000");
        return Float.parseFloat(df.format(f));
    }

}
