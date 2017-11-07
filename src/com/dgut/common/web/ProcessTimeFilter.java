package com.dgut.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行时间过滤器
 * 
 * @author liufang
 * 
 */
public class ProcessTimeFilter implements Filter {
	
	/**
	 * 使用指定类初始化日志对象,在日志输出的时候，可以打印出日志信息所在类
	 * 如：Logger logger = LoggerFactory.getLogger(com.Book.class);
       logger.debug("日志信息");
       将会打印出: com.Book : 日志信息
	 */
	
	protected final Logger log = LoggerFactory
			.getLogger(ProcessTimeFilter.class);
	/**
	 * 请求执行开始时间
	 */
	public static final String START_TIME = "_start_time";

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		long time = System.currentTimeMillis();
		request.setAttribute(START_TIME, time);
		chain.doFilter(request, response);
		time = System.currentTimeMillis() - time;
		log.debug("process in {} ms: {}", time, request.getRequestURI());
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
