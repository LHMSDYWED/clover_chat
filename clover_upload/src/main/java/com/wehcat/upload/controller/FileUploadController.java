package com.wehcat.upload.controller;
import com.wehcat.upload.util.FileUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
public class FileUploadController {

//    @Autowired
//    private Map<String,Object> map;
    //处理文件上传
    @RequestMapping(value="/uploadimg", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImg(@RequestParam("file") MultipartFile file,
                            HttpServletRequest request) {
        System.out.println(file);
        String contentType = file.getContentType();   //图片文件类型
        String fileName = file.getOriginalFilename();  //图片名字

        //文件存放路径
        String filePath =  "C:/images/";;
        String imagePath = "http://127.0.0.1:9125/images/"+fileName;
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
