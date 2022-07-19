package com.remote.tools.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

//加密
public class Encryption {
    /***
     * 对用户的密码进行MD5加密
     */
    public static String shiroEncryption(String password,String salt) {
        // shiro 自带的工具类生成salt
        //String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 加密次数
        int times = 2;
        // 算法名称
        String algorithmName = "md5";
        // 返回加密后的密码
        return new SimpleHash(algorithmName,password,salt,times).toString();
    }

    public static String generateSalt()
    {
        // shiro 自带的工具类生成salt
        return new SecureRandomNumberGenerator().nextBytes().toString();
    }
}
