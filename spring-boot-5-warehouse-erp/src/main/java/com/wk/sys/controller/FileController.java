package com.wk.sys.controller;

import cn.hutool.core.date.DateUtil;
import com.wk.sys.common.AppFileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传下载
 */
@RestController
@RequestMapping("file")
public class FileController {

    /**
     * 上传
     */
    @RequestMapping("uploadFile")
    public Map<String,Object> uploadFile(MultipartFile mf){
        //得到上传文件的文件名
        String oldName = mf.getOriginalFilename();
        //根据文件名后缀生成新的文件名，避免文件名冲突
        String newFileName="";
        if (oldName != null) {
            newFileName = AppFileUtils.createNewFileName(oldName);
        }
        //得到当前日期字符串
        String dirName = DateUtil.format(new Date(), "yyyy-MM-dd");
        //构建文件夹
        File dirFile = new File(AppFileUtils.UPLOAD_PATH,dirName);
        //判断当前文件夹是否存在
        if(!dirFile.exists()){
            dirFile.mkdirs();       //如果不存在就创建
        }
        //后缀添加_temp构建临时文件对象，和其他数据一起提交到后台再把后缀取消 如果没有取消后缀就用定时器清理
        File file = new File(dirFile,newFileName+"_temp");
        //把mf中的图片信息写入file
        try {
            mf.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("path",dirName+"/"+newFileName+"_temp");
        return map;
    }

    /**
     * 文件下载
     */
    @RequestMapping("showImageByPath")
    public ResponseEntity<Object> showImageByPath(String path){
        return AppFileUtils.createResponseEntity(path);
    }

}
