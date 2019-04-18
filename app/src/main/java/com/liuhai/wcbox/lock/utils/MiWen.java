package com.liuhai.wcbox.lock.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenliangj2ee on 2017/8/20.
 */

public class MiWen {


    public static String jiami(String gbString) {
       return  toChina(toUicode1(gbString));
    }

    public static String jiemi(String gbString) {
        return  toChina(toUicode2(gbString));
    }

    private static String toUicode1(String gbString) {
        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < gbString.length(); i++) {
            char c = gbString.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        String result="";
        for(int i=0;i<unicode.length();i++){
            String nu=unicode.substring(i,i+1);
            if(isNumeric(nu)){
                int num=Integer.parseInt(nu);
                num++;
                Log.i("MiWen", ""+num);
                if(num==10)
                    num=0;
                result=result+num;
            }else{
                result=result+nu;
            }


        }
        Log.i("MiWen", "toUicode1"+result);
        return result;
    }

    private static String toUicode2(String gbString) {
        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < gbString.length(); i++) {
            char c = gbString.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        String result="";
        for(int i=0;i<unicode.length();i++){
            String nu=unicode.substring(i,i+1);
            if(isNumeric(nu)){
                int num=Integer.parseInt(nu);
                    num--;
                if(num==-1)
                    num=9;
                result=result+num;
            }else{
                result=result+nu;
            }


        }
        Log.i("MiWen", "toUicode2"+result);
        return result;
    }

    private static String toChina(String unicode) {
        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            int data = Integer.parseInt(hex[i], 16);

            string.append((char) data);
        }

        return string.toString();
    }

    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
