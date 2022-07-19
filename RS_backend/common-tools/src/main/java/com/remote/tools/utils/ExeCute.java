package com.remote.tools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExeCute {

    public static String execCmd(String[] args) {
        StringBuilder ans = new StringBuilder("");
        try {
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                ans.append(line).append("\n");
                System.out.println(line);
            }
            in.close();//关闭流
            int endFlag = 0;//判断process对象是否还在执行
            try {
                endFlag = proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            if (endFlag == 0) {
                System.out.println("The process is ended normally.");
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (ans.toString().equals("")) {
            return "success";
        }
        return ans.substring(0, ans.length() - 1);
    }
}
