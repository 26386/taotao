package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;

//拦截器强制未登录用户前往登录页面进行登录
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserServiceImpl userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从cookie中获取当前用户的token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据token调用service判断用户是否登录
		TbUser user = userService.getUserByToken(token);
		if(user==null){
			//跳转到登录页面,把用户请求的url作为参数传递过去
			response.sendRedirect(userService.SSO_BASE_URL+userService.SSO_PAGE_LOGIN+"?redirect="+request.getRequestURL());	
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
