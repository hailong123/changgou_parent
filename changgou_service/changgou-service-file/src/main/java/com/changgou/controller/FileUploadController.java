package com.changgou.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class FileUploadController {

    /**
     * 文件上传
     */
    @PostMapping
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception {
        //封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),//文件名称 1.jpg
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename()) //获取文件扩展名称
        );

        //调用FastDFS工具类将文件传入到FastDFS中
         String[] uploads = FastDFSUtil.upload(fastDFSFile);
        //拼接访问地址: http://172.16.2.23:8888/group1/M00/00/00/rBACFlx_qbCAZsEPAADjtFeNj8w952.png
        String path = "http://10.211.55.11:8888/"+uploads[0]+"/"+uploads[1];

        return new Result(true, StatusCode.OK,"上传成功",path);
    }
}
