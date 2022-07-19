package com.remote.tools.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class OSSConnectionTest {

    @Test
    public void downloadFileTest() {

    }

    @Test
    public void downLoadMatipartTest() {
        OSSConnection ossConnection=new OSSConnection();
        ossConnection.downLoadMatipart("change-detection","test/2.zip");
    }

    @Test
    public void uplopadMatipartTest() {
        OSSConnection ossConnection=new OSSConnection();
        ossConnection.uplopadMatipart("test/2.zip","E:\\Programs\\RemoteSensing\\test\\td2.zip");
    }

    @Test
    public void removeFileTest() {
        OSSConnection ossConnection=new OSSConnection();
        ossConnection.removeFile("test/1");
    }
}