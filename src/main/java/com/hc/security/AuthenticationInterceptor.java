package com.hc.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hc.security.annotation.LoginRequired;
import com.hc.util.TokenUtil;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		LoginRequired loginRequired = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
		
		if(loginRequired == null) {
			return true;
		}
		
		String token = request.getHeader("x-token");
		if(TokenUtil.isTokenValid(token)) {
			return true;
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		/*return super.preHandle(request, response, handler);*/
	}
}
