package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.taotao.common.utils.FtpUtil;

public class FTPTest {

	@Test
	public void testName() throws Exception {
		//创建一个FTPClient对象
		FTPClient ftpClient=new FTPClient();
		//创建ftp连接,默认是21端口
		ftpClient.connect("192.168.79.128", 21);
		//登录ftp服务器,使用服务器的用户名和密码
		ftpClient.login("ftpuser", "ftpuser");
		//设置上传的路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/images");
		//修改上传文件的格式(二进制格式)
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		InputStream in=new FileInputStream(new File("C:/Users/Public/Pictures/Sample Pictures/0025.jpg"));
		//ftpClient.storeFile(remote, local)
		//remote:保存到服务器端的文件名
		//local:需要上传文件的输入流
		ftpClient.storeFile("hello.jpg", in);
		//关闭连接
		ftpClient.logout();
	}
	
	@Test
	public void testFTPUtil() throws Exception {
		InputStream in=new FileInputStream(new File("C:/Users/Public/Pictures/Sample Pictures/0025.jpg"));
		FtpUtil.uploadFile("192.168.79.128", 21, "ftpuser", "ftpuser", "/home/ftpuser/images"	//
				, "/2019/05/29", "hello.jpg", in);
	}
}
