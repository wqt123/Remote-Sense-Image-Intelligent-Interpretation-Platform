package com.remote.tools.utils;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service("MailService")
@EnableAutoConfiguration
@Component
public class EmailService {

    @Resource
    private JavaMailSender mailSender;//一定要用@Autowired

    @Resource
    private MailProperties mailProperties;

    /**
     * 给前端输入的邮箱，发送验证码
     *
     * @param email 要给发送的邮箱的地址
     * @param map   来存一对邮箱和验证码的键值对
     * @return 发送验证码成功与否
     */
    public boolean sendMimeMail(String email, Map<String, Code> map, String code) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("验证码邮件");//主题

            Code cd= new Code(code);

            //将随机数放置到session中
            map.put(email, cd);

            mailMessage.setText("您收到的验证码是：" + code);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(mailProperties.getUsername());//你自己的邮箱

            mailSender.send(mailMessage);//发送

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean sendEmail(String email,String title,String content)throws Exception{
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject(title);//主题
            mailMessage.setText(content);//内容
            mailMessage.setTo(email);//发给谁
            mailMessage.setFrom(mailProperties.getUsername());//你自己的邮箱
            mailSender.send(mailMessage);//发
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static class Code{
        public String code;//六位验证码
        public Date time;//验证码的时间

        public Code(String code){
            this.code=code;
            this.time=new Date();
        }
    }
}
