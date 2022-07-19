package com.remote.user.controllers;

import com.remote.models.User;
import com.remote.tools.utils.EmailService;
import com.remote.user.services.UserService;
import com.remote.tools.utils.Encryption;
import com.remote.tools.utils.Radom;
import com.remote.tools.utils.Result;
import com.remote.user.entities.LoginInfo;
import com.remote.user.entities.RegisterInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userService/user")
@RefreshScope
@Api(value="user",tags = "user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    private final Map<String, EmailService.Code> map = new HashMap<>();//用于暂存验证码(后面可存入redis中)

    private final long outDate = 300000L;//设置过期时间为5分钟

    @ApiOperation(value = "用户登录，返回用户内部id")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result<String> login(@RequestBody LoginInfo loginInfo){
        User user = userService.getByName(loginInfo.getUserName_or_email());
        if (user == null) {
            user = userService.getByEmail(loginInfo.getUserName_or_email());
        }
        if (user == null) {
            return Result.wrapErrorResult("不存在该用户名或邮箱号！");
        }
        if (!user.getPassword().equals(Encryption.shiroEncryption(loginInfo.password, user.getSalt()))) {
            return Result.wrapErrorResult("密码错误！");
        }
        return Result.wrapSuccessfulResult("登录成功", user.getId());
    }

    @ApiOperation(value = "用户注册,成功则返回内部id")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result<String> register(@RequestBody RegisterInfo info){
        String id=userService.generateID();

        Date time = new Date();
        if (!map.containsKey(info.email)) {
            return Result.wrapErrorResult("验证码不存在，请先获取验证码");
        }

        if (time.getTime() - map.get(info.email).time.getTime() >= outDate) {
            return Result.wrapErrorResult("验证码过期，请重新获取");
        }

        if (!Objects.equals(map.get(info.email).code, info.code)) {
            return Result.wrapErrorResult("验证码错误！");
        }

        if(userService.getByName(info.name)!=null){
            return Result.wrapSuccessfulResult("该用户名已存在");
        }

        if (userService.getByEmail(info.email) != null) {
            return Result.wrapSuccessfulResult("该邮箱已被注册");
        }

        User user = new User();
        user.setId(id);
        user.setEmail(info.email);
        user.setName(info.name);
        user.setSalt(Encryption.generateSalt());
        user.setPassword(Encryption.shiroEncryption(info.pwd, user.getSalt()));
        //存储
        userService.createOrUpdate(user);

        return Result.wrapSuccessfulResult("注册成功", id);
    }

    @ApiOperation(value = "验证码发送")
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public Result<String> getCode(@RequestParam("email") String email) {
        String code = Radom.getSixBitRandom();//获取六位随机验证码
        //邮件发送接口
        try {
            emailService.sendMimeMail(email, map, code);
        } catch (Exception e) {
            return Result.wrapErrorResult(e.getMessage());
        }

        return Result.wrapSuccessfulResult("发送成功");
    }
}
