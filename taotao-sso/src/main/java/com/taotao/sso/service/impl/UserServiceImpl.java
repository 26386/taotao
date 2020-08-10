package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;

/**
 * 用户登录注册管理
 * 
 * @author wlnner
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	
	// 对用户的注册的数据进行校验(type:1,2,3分别代表username,phone,email)
	public TaotaoResult checkData(String content, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(content);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(content);
		} else if (type == 3) {
			criteria.andEmailEqualTo(content);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}

	public TaotaoResult createUser(TbUser user) {
		// 校验用户输入
		boolean usernameCheck = (boolean) this.checkData(user.getUsername(), 1).getData();
		boolean phoneCheck = (boolean) this.checkData(user.getPhone(), 2).getData();
		if (usernameCheck && phoneCheck) {
			// MD5加密
			user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
			user.setCreated(new Date());
			user.setUpdated(new Date());
			userMapper.insertSelective(user);
			return TaotaoResult.ok();
		}
		return TaotaoResult.build(400, "注册失败. 请校验数据后请再提交数据");
	}
	
	public TaotaoResult userLogin(String username,String password,HttpServletRequest request,HttpServletResponse response){
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		//根据用户名查询是否存在此用户
		if(list==null||list.size()==0){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//判断用户输入的密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		user.setPassword(null);
		//生成一个唯一的token
		String token=UUID.randomUUID().toString();
		//token作为redis的key,用户信息作为value,设置到redis中
		String key=REDIS_USER_SESSION_KEY+":"+token;
		jedisClient.set(key, JsonUtils.objectToJson(user));
		//设置token的有效时间
		jedisClient.expire(key, SSO_SESSION_EXPIRE);
		//cookie的有效期为关闭浏览器
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		return TaotaoResult.ok(token);
	}
	
	public TaotaoResult getUserByToken(String token){
		String key=REDIS_USER_SESSION_KEY+":"+token;
		//根据token从redis中查询用户信息
		String json = jedisClient.get(key);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "token已过期,请重新登录");
		}
		//更新过期时间
		jedisClient.expire(key, SSO_SESSION_EXPIRE);
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}
	
	public TaotaoResult userLogout(String token){
		String key=REDIS_USER_SESSION_KEY+":"+token;
		//根据token从redis中查询用户信息
		String json = jedisClient.get(key);
		if(StringUtils.isNotBlank(json)){
			jedisClient.del(key);
			return TaotaoResult.ok();
		}else {
			return TaotaoResult.build(400, "未登录,不能正常退出");
		}
	}
}
