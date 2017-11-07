package com.dgut.main.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dgut.common.web.session.SessionProvider;
import com.dgut.member.entity.Member;
import com.dgut.member.manager.MemberAuthenticationMng;
import com.dgut.member.manager.MemberMng;

/**
 * 上下文信息拦截器
 * 
 */
public class FrontContextInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		

		Member user = null;
//		Integer userId = memberAuthMng.retrieveUserIdFromSession(session, request);
		//根据token去数据库查找用户 时间是2天
		String token = request.getParameter("token");
		Integer userId = memberAuthMng.retrieveUserIdFromToken(token, request);
		//System.out.println("请求的用户id:"+userId);
		if (userId != null) {
			user = memberMng.findById(userId);
		}
		if (user != null) {
			CmsUtils.setMember(request, user);
		}
		return true;
	}

	private SessionProvider session;
	private MemberMng memberMng;
	private MemberAuthenticationMng memberAuthMng;

	@Autowired
	public void setSession(SessionProvider session) {
		this.session = session;
	}
	@Autowired
	public void setMemberMng(MemberMng memberMng) {
		this.memberMng = memberMng;
	}
	@Autowired
	public void setMemberAuthMng(MemberAuthenticationMng memberAuthMng) {
		this.memberAuthMng = memberAuthMng;
	}

}