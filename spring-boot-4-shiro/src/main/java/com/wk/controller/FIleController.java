package com.wk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class FIleController {

	@RequestMapping("upload")
	@ResponseBody
	public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
		Map<String,Object> map = new HashMap<>();
		if(file.isEmpty()){
			map.put("code",1);
			map.put("msg","请选择文件");
			return map;
		}
		String fileName = generateFileName(file.getOriginalFilename());
		String filePath = "d:/uploadPhoto/";
		File dest = new File(filePath+fileName);
		try {
			if (!dest.exists()){
				dest.createNewFile();	//创建文件
			}
			file.transferTo(dest);
			map.put("code",0);
			map.put("msg","上传成功");
			Map<String,Object> data = new HashMap<>();
			data.put("src",dest.toString());
			map.put("data",data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.put("code",1);
		map.put("msg","上传失败");
		return map;
	}

	/**
	 * 生成文件名
	 * @param originalFileName 原始文件名
	 * @return
	 */
	public static String generateFileName(String originalFileName) {

		// 截取扩展名部分
		String extensibleName = "";

		if(originalFileName.contains(".")) {
			extensibleName = originalFileName.substring(originalFileName.lastIndexOf("."));
		}

		return UUID.randomUUID().toString().replaceAll("-", "")+extensibleName;
	}


}
