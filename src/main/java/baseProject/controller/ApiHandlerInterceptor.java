package baseProject.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.web.servlet.HandlerInterceptor;

import baseProject.utils.LogUtil;

public class ApiHandlerInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.getParameterMap().forEach((k,v)->{
			LogUtil.debug("request params key:"+k+" value:"+v);
		});
		LogUtil.debug(request.getMethod());
		LogUtil.debug(request.getRequestURL().toString());
		return true;
	}

}
