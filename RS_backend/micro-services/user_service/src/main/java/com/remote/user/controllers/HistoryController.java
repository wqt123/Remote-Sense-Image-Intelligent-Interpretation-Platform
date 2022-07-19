package com.remote.user.controllers;

import com.remote.models.History;
import com.remote.models.User;
import com.remote.tools.utils.OSSConnection;
import com.remote.user.services.HistoryService;
import com.remote.user.services.UserService;
import com.remote.tools.enums.HistoryStatus;
import com.remote.tools.utils.Result;
import com.remote.tools.utils.TimeConvert;
import com.remote.user.entities.HisInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userService/history")
@RefreshScope
@Api(value="history",tags = "history")
public class HistoryController {
    @Autowired
    UserService userService;

    @Autowired
    HistoryService historyService;

    @ApiOperation(value = "根据历史id获取单条历史记录")
    @RequestMapping(value = "/getSingle",method = RequestMethod.GET)
    public Result<History> getSingle(@RequestParam("his_id") String hisId){
        History his=new History();
        his=historyService.getById(hisId);
        if(his==null){
            return Result.wrapErrorResult("不存在此id的历史记录");
        }
        //敏感信息置空
        User u=his.getUser();
        u.setPassword(null);
        u.setSalt(null);

        his.setUser(u);
        return Result.wrapSuccessfulResult(his);
    }

    @ApiOperation(value = "根据用户id获取该用户的所有历史记录")
    @RequestMapping(value = "/getAllByUserId",method = RequestMethod.GET)
    public Result<List<History>> getAllByUserId(@RequestParam("user_id") String userId){
        if(userService.getById(userId)==null){
            return Result.wrapErrorResult("不存在该用户");
        }
        List<History> ls=new ArrayList<>();
        ls=historyService.getAllByUserId(userId);
        for(History l:ls){
            //敏感信息置空
            User u=l.getUser();
            u.setPassword(null);
            u.setSalt(null);
            l.setUser(u);
        }
        return Result.wrapSuccessfulResult(ls);
    }

    @ApiOperation(value = "上传（创建）单条历史记录，返回his_id")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Result<String> create(@RequestBody HisInfo hisInfo){
        User u=userService.getById(hisInfo.userId);
        if(u==null){
            return Result.wrapErrorResult("不存在该用户");
        }
        //传入信息
        History his=new History();
        his.setUser(u);
        his.setId(historyService.generateID());
        his.setCreateTime(TimeConvert.getNowTime());
        his.setStatus(HistoryStatus.uploadSuccess.toString());
        his.setIsRemove(false);
        his.setOriginName1(hisInfo.originName1);
        his.setOriginName2(hisInfo.originName2);
        his.setResultName(null);
        his.setSize(hisInfo.size);
        his.setTitle(hisInfo.title);
        his.setType(hisInfo.type);

        historyService.createOrUpdate(his);

        return Result.wrapSuccessfulResult("创建成功",his.getId());
    }

    @ApiOperation(value = "移除单条历史记录")
    @RequestMapping(value = "/remove",method = RequestMethod.GET)
    public Result<String> remove(@RequestParam("his_id") String hisId){
        History his=historyService.getById(hisId);
        if(his==null){
            return Result.wrapErrorResult("该历史记录id不存在");
        }

        //从oss中移除对象
        OSSConnection ossConnection=new OSSConnection();
        if(!his.getOriginName1().isEmpty()){
            if(!ossConnection.removeFile(his.getOriginName1())){
                return Result.wrapErrorResult("oss中文件删除失败");
            }
        }
        if(!his.getOriginName2().isEmpty()){
            if(!ossConnection.removeFile(his.getOriginName2())){
                return Result.wrapErrorResult("oss中文件删除失败");
            }
        }
        if(!his.getResultName().isEmpty()){
            if(!ossConnection.removeFile(his.getResultName())){
                return Result.wrapErrorResult("oss中文件删除失败");
            }
        }

        //在mysql中使用标记删除
        his.setIsRemove(true);
        historyService.createOrUpdate(his);

        return Result.wrapSuccessfulResult("删除成功");
    }

}
