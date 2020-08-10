package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;
	@Value("${SSO_USER_TOKEN}")
	private String SSO_USER_TOKEN;
	@Value("${SSO_PAGE_LOGIN}")
	public String SSO_PAGE_LOGIN;
	
	public TbUser getUserByToken(String token){
		try {
			String json = HttpClientUtil.doGet(SSO_BASE_URL+SSO_USER_TOKEN+token);
			TaotaoResult ret = TaotaoResult.formatToPojo(json, TbUser.class);
			if(ret.getStatus()==200){
				return (TbUser)ret.getData();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
