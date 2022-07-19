package com.remote.targetExtraction.controllers;

import com.remote.models.History;
import com.remote.models.User;
import com.remote.targetExtraction.services.HistoryService;
import com.remote.targetExtraction.services.UserService;
import com.remote.tools.enums.HistoryStatus;
import com.remote.tools.utils.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/targetExtraction")
@RefreshScope
@Api(value="targetExtraction",tags = "targetExtraction")
public class targetExtractionController {
    //文件名列表
    public List<String> ls=new ArrayList<>();

    @Autowired
    HistoryService historyService;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Result<String> test(){
        return Result.wrapSuccessfulResult("It's successful!");
    }

    @RequestMapping(value = "/work",method = RequestMethod.POST)
    public Result<byte[]> pic(@RequestParam(value = "picture") MultipartFile picture) {
        //获取随机不重复的文件名
        String a= Radom.getRandomNumber(6,ls);
        String r= Radom.getRandomNumber(6,ls);

        //保存至本地
        String path="micro-services/target_extraction/src/main/resources";
        String absolute=new File(path).getAbsolutePath();
        File dest1 = new File(absolute+ "/input/" + a +".tiff");

        try {
            picture.transferTo(dest1); // 保存文件
        } catch (Exception e) {
            e.printStackTrace();
            MyFile.DeleteFolder(absolute+ "/input/" + a +".tiff");
            ls.remove(a);
            return Result.wrapErrorResult("保存图片失败");
        }

        String url="http://127.0.0.1:8300/TargetExtraction";
        Http http=new Http();
        try {
            //传入文件名的参数
            Map<String, Object> map=new HashMap<>();
            map.put("a",a+".tiff");
            map.put("r",r+".jpg");
            map.put("dir",absolute+"/result/");
            //执行请求
            String res=http.doGet(url,map);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            //删除图片
            MyFile.DeleteFolder(absolute+ "/input/" + a +".tiff");
            MyFile.DeleteFolder(absolute+ "/result/"+ r +".jpg");
            ls.remove(a);ls.remove(r);
            return Result.wrapErrorResult("目标url请求失败");
        }

        //读取python运行文件，并以字符流返还至前端
        BufferedImage bufferedImage = null;
        try {
            FileInputStream readPic=new FileInputStream(new File(absolute+"/result/"+ r +".jpg"));
            bufferedImage = ImageIO.read(readPic);
            readPic.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.wrapErrorResult("读取结果图片失败");
        }finally {
            //删除图片
            MyFile.DeleteFolder(absolute+ "/input/" + a +".tiff");
            MyFile.DeleteFolder(absolute+ "/result/"+ r +".jpg");
            ls.remove(a);ls.remove(r);
        }

        //将字符流返回至前端
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            assert bufferedImage != null;
            ImageIO.write(Objects.requireNonNull(bufferedImage), "jpg", out);
            return Result.wrapSuccessfulResult(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.wrapErrorResult("失败");
        }
    }

    @RequestMapping(value = "/batch_work",method = RequestMethod.GET)
    public Result<String> batchWork(@RequestParam(value = "his_id")String hisId) throws FileNotFoundException {
        //获取目标记录
        History history = historyService.getById(hisId);
        String fileName=history.getOriginName1();
        User user = history.getUser();
        String userEmail = user.getEmail();

        //异步线程，避免接口阻塞
        Thread thread = new Thread(()->{
            //修改状态
            history.setStatus(HistoryStatus.isRunning.toString());
            historyService.createOrUpdate(history);
            String path="micro-services/target_extraction/src/main/resources";
            String absolute=new File(path).getAbsolutePath();

            //建立oss连接
            OSSConnection oss = new OSSConnection();

            //下载文件
            if(!oss.downLoadMatipart("target-extraction", fileName)){
                //更新数据库状态
                history.setStatus(HistoryStatus.runError.toString());//失败的情况
                historyService.createOrUpdate(history);
                Thread.interrupted();
            }

            //解压文件
            //获取解压的目录名（须保证解压文件的内部目录和解压文件名一致）
            String fName = fileName.split("\\.")[0];
            File inputFolder = new File(absolute + "/input/"+ fName);
            inputFolder.mkdir();
            File file = new File(absolute+"/input/"+fileName);
            ZipUtil.unPackZip(file,absolute+"/input/"+fName);

            //创建结果目录
            String resultFolderDir = Radom.getRandomNumber(6,ls);
            File resultFolder = new File(absolute+ "/result/"+resultFolderDir);
            resultFolder.mkdir();

            //获取目标文件夹下的所有图片
            File[] images = inputFolder.listFiles();
            //遍历运行
            assert images != null;
            for(File image:images){
                String url="http://127.0.0.1:8300/TargetExtraction";
                Http http=new Http();
                try {
                    //传入文件名的参数
                    Map<String, Object> map=new HashMap<>();
                    map.put("a",fName +"\\" + image.getName());
                    map.put("r",image.getName());
                    map.put("dir",absolute+"/result/"+resultFolderDir);
                    //执行请求
                    String res=http.doGet(url,map);
                } catch (Exception e) {
                    e.printStackTrace();
                    MyFile.DeleteFolder(absolute+ "/input/"+fileName);
                    MyFile.DeleteFolder(absolute+ "/input/"+fName);
                    MyFile.DeleteFolder(absolute+ "/result/"+resultFolderDir);
                    ls.remove(resultFolderDir);

                    try {
                        emailService.sendEmail(userEmail,"批量式推理失败通知","您的批量式推理："+history.getTitle()+"因系统原因推理失败，请尝试重新上传并推理，为您带来的不便敬请谅解。");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    //更新数据库状态
                    history.setStatus(HistoryStatus.runError.toString());//失败的情况
                    historyService.createOrUpdate(history);
                    Thread.interrupted();
                }
            }

            //压缩结果目录
            FileOutputStream fos= null;
            try {
                fos = new FileOutputStream(new File(absolute+"/result/"+resultFolderDir+".zip"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ZipUtil.toZip(absolute+ "/result/" + resultFolderDir,fos,true);
            if(!oss.uplopadMatipart(fName+"_result.zip",absolute+"/result/"+resultFolderDir+".zip")){
                //更新数据库状态
                history.setStatus(HistoryStatus.runError.toString());//失败的情况
                historyService.createOrUpdate(history);
                Thread.interrupted();
            }

            //更新数据库状态
            history.setStatus(HistoryStatus.runEnd.toString());
            history.setResultName(fName+"_result.zip");
            historyService.createOrUpdate(history);

            try {
                emailService.sendEmail(userEmail,"批量式推理成功通知","您的批量式推理："+history.getTitle()+"已推理完成，请前往历史记录中下载结果文件。");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //删除tmp目录及文件
            MyFile.DeleteFolder(absolute+ "/input/"+fileName);
            MyFile.DeleteFolder(absolute+ "/input/"+fName);
            MyFile.DeleteFolder(absolute+ "/result/" + resultFolderDir);
            MyFile.DeleteFolder(absolute+ "/result/"+resultFolderDir+".zip");
            ls.remove(resultFolderDir);
        });

        thread.start();
        return Result.wrapSuccessfulResult("已开始运算，在历史记录中查看运算状态，下载运算结果。");
    }
}

//        String[] arg = new String[] { "python", absolute+"/TargetExtraction.py",absolute};
//        if(ExeCute.execCmd(arg)==null) {
//            return Result.wrapErrorResult("目标检测脚本执行失败");
//        }
//