package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable("param") String content, @PathVariable Integer type, String callback) {
		TaotaoResult result = null;
		if (StringUtils.isBlank(content)) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		// 校验出错
		if (result != null) {
			if (StringUtils.isNotBlank(callback)) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result;
			}
		}
		result = userService.checkData(content, type);
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user) {
		return userService.createUser(user);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password,HttpServletRequest request,HttpServletResponse response) {
		try {

			TaotaoResult result = userService.userLogin(username, password,request,response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		try {
			TaotaoResult result = userService.getUserByToken(token);			
			if(StringUtils.isNoneBlank(callback)){
				MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object userLogout(@PathVariable String token, String callback) {
		try {
			TaotaoResult result = userService.userLogout(token);
			if(StringUtils.isNotBlank(callback)){
				MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
}
