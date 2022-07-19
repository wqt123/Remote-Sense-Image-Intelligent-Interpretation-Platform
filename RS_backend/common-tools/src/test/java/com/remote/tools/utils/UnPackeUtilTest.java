package com.remote.tools.utils;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UnPackeUtilTest {

    @Test
    public void unPackZipTest(){
        // 解压目录
        String packFileStr = "E:\\Programs\\zip";
        //不存在则创建
        File packFile = new File(packFileStr);
        if (!packFile.exists()) {
            boolean mkdirs = packFile.mkdirs();
        }

        String packFilePath="E:\\Programs\\img_testA.zip";
        File file = new File(packFilePath);

        //zip压缩包
        ZipUtil.unPackZip(file, packFileStr);
        System.out.println("success");
    }

    @Test
    public void toZipTest(){
        try {
            FileOutputStream fos=new FileOutputStream(new File("E:\\Programs\\RemoteSensing\\test\\td.zip"));
            ZipUtil.toZip("E:\\Programs\\RemoteSensing\\test\\target_detection",fos,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toZipTest2(){
        try {
            List<File> files=new ArrayList<>();
            files.add(new File("E:\\Programs\\RemoteSensing\\test\\target_detection\\A.jpg"));
            files.add(new File("E:\\Programs\\RemoteSensing\\test\\target_detection\\B.jpg"));
            FileOutputStream fos=new FileOutputStream(new File("E:\\Programs\\RemoteSensing\\test\\td2.zip"));
            ZipUtil.toZip(files,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}