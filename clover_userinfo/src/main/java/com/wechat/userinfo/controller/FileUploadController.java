package com.wechat.userinfo.controller;

import com.wechat.userinfo.util.FileUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@CrossOrigin
public class FileUploadController {

    //获取当前日期时间的string类型用于文件名防重复
    public String dates(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
//    @Autowired
//    private Map<String,Object> map;
    //处理文件上传
    @RequestMapping(value="/uploadimg", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImg(@RequestParam("file") MultipartFile file,
                            HttpServletRequest request) {
        System.out.println(file);
        String contentType = file.getContentType();   //图片文件类型
        String fileName = dates()+file.getOriginalFilename();  //图片名字

        //文件存放路径
        String filePath =  "D:/images/";;
        String imagePath = "/images/"+fileName;
        System.out.println(imagePath);
        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // 返回图片的存放路径
        return imagePath;
    }
}
