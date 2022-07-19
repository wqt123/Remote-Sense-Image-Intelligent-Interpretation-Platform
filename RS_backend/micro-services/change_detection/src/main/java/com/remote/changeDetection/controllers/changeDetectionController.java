package com.remote.changeDetection.controllers;

import com.alibaba.fastjson.JSONObject;
import com.remote.changeDetection.services.HistoryService;
import com.remote.changeDetection.services.UserService;
import com.remote.models.History;
import com.remote.models.User;
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
@RequestMapping("/api/changeDetection")
@RefreshScope
@Api(value="changeDetection",tags = "changeDetection")
public class changeDetectionController {
    //文件名列表
    public List<String>ls=new ArrayList<>();

    @Autowired
    HistoryService historyService;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Result<byte[]> test(){
        String path="micro-services/change_detection/src/main/resources";
        String absolute=new File(path).getAbsolutePath();

        //读取python运行文件，并以字符流返还至前端
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new FileInputStream(new File(absolute+"/result/result.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.wrapErrorResult("读取结果图片失败");
        }
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

    @RequestMapping(value = "/work",method = RequestMethod.POST)
    public Result<ExtractAns> pic(@RequestParam(value = "pictures") List<MultipartFile> pictures) {
        //获取
        if(pictures.size()<=1){
            return Result.wrapErrorResult("图片数量必须为2");
        }

        //获取随机不重复的文件名
        String a=Radom.getRandomNumber(6,ls);
        String b=Radom.getRandomNumber(6,ls);
        String r=Radom.getRandomNumber(6,ls);

        //保存至本地
        String path="micro-services/change_detection/src/main/resources";
        String absolute=new File(path).getAbsolutePath();
        File dest1 = new File(absolute+ "/input/" + a +".png");
        File dest2 = new File(absolute+ "/input/" + b +".png");

        // 保存文件
        try {
            pictures.get(0).transferTo(dest1);
            pictures.get(1).transferTo(dest2);
        } catch (Exception e) {
            e.printStackTrace();
            //删除文件
            MyFile.DeleteFolder(absolute+ "/input/" + a +".png");
            MyFile.DeleteFolder(absolute+ "/input/" + b +".png");
            ls.remove(a);ls.remove(b);
            return Result.wrapErrorResult("保存图片失败");
        }

        //调用flask端url
        String res=null;
        float rate=0;
        String url="http://127.0.0.1:8300/ChangeDetector";
        Http http=new Http();
        try {
            //传入文件名的参数
            Map<String, Object> map=new HashMap<>();
            map.put("a",a+".png");
            map.put("b",b+".png");
            map.put("r",r+".jpg");
            map.put("dir",absolute+"/result/");
            //执行请求
            res=http.doGet(url,map);
            System.out.println(res);
            rate= JSONObject.parseObject(res, Ans.class).getRate();
            rate=ExtractAns.GetRate(rate);
        } catch (Exception e) {
            e.printStackTrace();
            //删除文件
            MyFile.DeleteFolder(absolute+ "/input/" + a +".png");
            MyFile.DeleteFolder(absolute+ "/input/" + b +".png");
            MyFile.DeleteFolder(absolute+ "/result/"+ r +".jpg");
            ls.remove(a);ls.remove(b);ls.remove(r);
            return Result.wrapErrorResult("目标url请求失败");
        }

        //读取python运行的结果文件，并以字符流返还至前端
        BufferedImage bufferedImage = null;
        try {
            FileInputStream readPic=new FileInputStream(new File(absolute+"/result/"+r+".jpg"));
            bufferedImage = ImageIO.read(readPic);
            readPic.close();//关闭读入流
        } catch (IOException e) {
            e.printStackTrace();
            return Result.wrapErrorResult("读取结果图片失败");
        }finally {
            //删除文件
            MyFile.DeleteFolder(absolute+ "/input/" + a +".png");
            MyFile.DeleteFolder(absolute+ "/input/" + b +".png");
            MyFile.DeleteFolder(absolute+ "/result/"+ r +".jpg");
            ls.remove(a);ls.remove(b);ls.remove(r);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            assert bufferedImage != null;
            ImageIO.write(Objects.requireNonNull(bufferedImage), "jpg", out);
            return Result.wrapSuccessfulResult(ExtractAns.GetAns(out.toByteArray(),rate));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.wrapErrorResult("失败");
        }
    }


    @RequestMapping(value = "/batch_work",method = RequestMethod.GET)
    public Result<String> batchWork(@RequestParam(value = "his_id")String hisId) throws FileNotFoundException {
        //获取目标记录
        History history = historyService.getById(hisId);
        User user = history.getUser();
        String userEmail = user.getEmail();

        String fileName1=history.getOriginName1();
        String fileName2=history.getOriginName2();

        //异步线程，避免接口阻塞
        Thread thread = new Thread(()->{
            //修改状态
            history.setStatus(HistoryStatus.isRunning.toString());
            historyService.createOrUpdate(history);
            String path="micro-services/change_detection/src/main/resources";
            String absolute=new File(path).getAbsolutePath();

            //建立oss连接
            OSSConnection oss = new OSSConnection();

            //下载文件
            if(!oss.downLoadMatipart("change-detection", fileName1)
                    ||!oss.downLoadMatipart("change-detection", fileName2)){
                //更新数据库状态
                history.setStatus(HistoryStatus.runError.toString());//失败的情况
                historyService.createOrUpdate(history);
                try {
                    throw new Exception("下载文件失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //解压文件
            //获取解压的目录名（须保证解压文件的内部目录和解压文件名一致）
            String fName1 = fileName1.split("\\.")[0];
            String fName2 = fileName2.split("\\.")[0];
            File inputFolder1 = new File(absolute + "/input/"+ fName1);
            File inputFolder2 = new File(absolute + "/input/"+ fName2);
            inputFolder1.mkdir();
            inputFolder2.mkdir();
            File file1 = new File(absolute+"/input/"+fileName1);
            File file2 = new File(absolute+"/input/"+fileName2);
            ZipUtil.unPackZip(file1,absolute+"/input/"+fName1);
            ZipUtil.unPackZip(file2,absolute+"/input/"+fName2);

            //创建结果目录
            String resultFolderDir = Radom.getRandomNumber(6,ls);
            File resultFolder = new File(absolute+ "/result/"+resultFolderDir);
            resultFolder.mkdir();

            //获取目标文件夹下的所有图片
            File[] images1 = inputFolder1.listFiles();
            File[] images2 = inputFolder2.listFiles();
            //遍历运行
            assert images1 != null;
            assert images2 != null;
            for(int i=0;i<images1.length;i++){
                File image1=images1[i];
                File image2=images2[i];
                String url="http://127.0.0.1:8300/ChangeDetector";
                Http http=new Http();
                try {
                    //传入文件名的参数
                    Map<String, Object> map=new HashMap<>();
                    map.put("a",fName1 +"\\" + image1.getName());
                    map.put("b",fName2 +"\\" + image2.getName());
                    map.put("r",image1.getName());
                    map.put("dir",absolute+"/result/"+resultFolderDir);
                    //执行请求
                    String res=http.doGet(url,map);
                } catch (Exception e) {
                    e.printStackTrace();
                    MyFile.DeleteFolder(absolute+ "/input/"+fileName1);
                    MyFile.DeleteFolder(absolute+ "/input/"+fName1);
                    MyFile.DeleteFolder(absolute+ "/input/"+fileName2);
                    MyFile.DeleteFolder(absolute+ "/input/"+fName2);
                    MyFile.DeleteFolder(absolute+ "/result/"+resultFolderDir);
                    ls.remove(resultFolderDir);

                    //更新数据库状态
                    history.setStatus(HistoryStatus.runError.toString());//失败的情况
                    historyService.createOrUpdate(history);

                    try {
                        emailService.sendEmail(userEmail,"批量式推理失败通知","您的批量式推理："+history.getTitle()+"因系统原因推理失败，请尝试重新上传并推理，为您带来的不便敬请谅解。");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        throw e;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
            if(!oss.uplopadMatipart(fName1+"_result.zip",absolute+"/result/"+resultFolderDir+".zip")){
                //更新数据库状态
                history.setStatus(HistoryStatus.runError.toString());//失败的情况
                historyService.createOrUpdate(history);
                try {
                    throw new Exception("上传文件失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //更新数据库状态
            history.setStatus(HistoryStatus.runEnd.toString());
            history.setResultName(fName1+"_result.zip");
            historyService.createOrUpdate(history);

            try {
                emailService.sendEmail(userEmail,"批量式推理成功通知","您的批量式推理："+history.getTitle()+"已推理完成，请前往历史记录中下载结果文件。");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //删除tmp目录及文件
            MyFile.DeleteFolder(absolute+ "/input/"+fileName1);
            MyFile.DeleteFolder(absolute+ "/input/"+fName1);
            MyFile.DeleteFolder(absolute+ "/input/"+fileName2);
            MyFile.DeleteFolder(absolute+ "/input/"+fName2);
            MyFile.DeleteFolder(absolute+ "/result/" + resultFolderDir);
            MyFile.DeleteFolder(absolute+ "/result/"+resultFolderDir+".zip");
            ls.remove(resultFolderDir);
        });

        thread.start();
        return Result.wrapSuccessfulResult("已开始运算，在历史记录中查看运算状态，下载运算结果。");
    }

}

//调用并运行python文件
//        String[] arg = new String[] { "python", absolute+"/ChangeDetector.py",absolute};
//        String res=ExeCute.execCmd(arg);
//        float rate=0;
//        if(res==null){
//            return Result.wrapErrorResult("python 脚本执行失败");
//        }
//        else {
//            if(!res.equals("success")){
//                rate= ExtractAns.GetRate(res);
//            }
//        }