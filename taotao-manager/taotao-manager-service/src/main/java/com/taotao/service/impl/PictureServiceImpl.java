package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;

/**
 * 图片上传服务
 * @author wlnner
 *
 */
@Service
public class PictureServiceImpl implements PictureService {

	@Value("${ftp.address}")
	private String ftpAddress;
	@Value("${ftp.port}")
	private Integer ftpPort;
	@Value("${ftp.userrname}")
	private String ftpUsername;
	@Value("${ftp.password}")
	private String ftpPassword;
	@Value("${ftp.basepath}")
	private String ftpBasepath;
	@Value("${image.base.url}")
	private String imageBaseUrl;
							
	@Override
	public Map<String, Object> uploadPicture(MultipartFile uploadFile){
		Map<String, Object> resultMap=new HashMap<>();
		String oldName=uploadFile.getOriginalFilename();
		String ext=oldName.substring(oldName.lastIndexOf("."));
		String newName=IDUtils.getImageName()+ext;
		String filePath=new DateTime().toString("/yyyy/MM/dd");
		try {
			
			boolean result = FtpUtil.uploadFile(ftpAddress, ftpPort, ftpUsername, ftpPassword, ftpBasepath	//
					, filePath, newName, uploadFile.getInputStream());
			if(!result){
				resultMap.put("error", 1);
				resultMap.put("message", "文件上传失败");
				return resultMap;
			}
			resultMap.put("error", 0);
			resultMap.put("url", imageBaseUrl+filePath+"/"+newName);
			return resultMap;
		} catch (IOException e) {
			resultMap.put("error", 1);
			resultMap.put("message", "文件上传异常");
			return resultMap;
		}
	}

}
