package com.dgut.member.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.security.encoder.PwdEncoder;
import com.dgut.core.entity.Config.ConfigLogin;
import com.dgut.core.manager.ConfigMng;
import com.dgut.member.dao.MemberDao;
import com.dgut.member.entity.Member;
import com.dgut.member.manager.MemberMng;

@Service
@Transactional
public class MemberMngImpl implements MemberMng {
	@Transactional(readOnly = true)
	public Pagination getPage(String username,
			Boolean disabled, int pageNo, int pageSize) {
		Pagination page = dao.getPage(username, disabled, pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List getList(String username, Boolean disabled) {
		List list = dao.getList(username,disabled);
		return list;
	}



	@Transactional(readOnly = true)
	public Member findById(Integer id) {
		Member entity = dao.findById(id);
		return entity;
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		Member entity = dao.findByUsername(username);
		return entity;
	}


	public void updateLoginInfo(Integer userId, String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		Member user = findById(userId);
		if (user != null) {
			user.setLoginCount(user.getLoginCount() + 1);
			user.setLastLoginIp(ip);
			user.setLastLoginTime(now);
		}
	}


	public boolean isPasswordValid(Integer id, String password) {
		 Member user = findById(id);
		return pwdEncoder.isPasswordValid(user.getPassword(), password);
	}

	public void updatePwdRealName(Integer id, String password, String realName) {
		Member user = findById(id);
		user.setRealname(realName);
		if (!StringUtils.isBlank(password)) {
			user.setPassword(pwdEncoder.encodePassword(password));
		}
	}

	public Member saveMember(String username,  String password,
			String ip, Boolean gender,String realname) {
		Date now = new Timestamp(System.currentTimeMillis());
		Member user = new Member();
		user.setUsername(username);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setGender(gender);
		user.setRealname(realname);		
		user.init();
		dao.save(user);
		return user;
	}


	public Member updateMember(Member bean, String password) {
		Updater<Member> updater = new Updater<Member>(bean);
		Member user = dao.updateByUpdater(updater);
		if (!StringUtils.isBlank(password)) {
			user.setPassword(pwdEncoder.encodePassword(password));
		}
		return user;
	}

	public Member getByUsername(String username) {
		return dao.findByUsername(username);
	}
	public Member login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException {
		Member user = getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username not found: "
					+ username);
		}
		if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
			updateLoginError(user.getId(), ip);
			throw new BadCredentialsException("password invalid");
		}
		updateLoginSuccess(user.getId(), ip);
		return user;
	}
	public void updateLoginSuccess(Integer userId, String ip) {
		Member user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());

		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);

		user.setErrorCount(0);
		user.setErrorTime(null);
		user.setErrorIp(null);
	}
	
	public void updateLoginError(Integer userId, String ip) {
		Member user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());
		ConfigLogin configLogin = configMng.getConfigLogin();
		int errorInterval = configLogin.getErrorInterval();
		Date errorTime = user.getErrorTime();

		user.setErrorIp(ip);
		if (errorTime == null
				|| errorTime.getTime() + errorInterval * 60 * 1000 < now
						.getTime()) {
			user.setErrorTime(now);
			user.setErrorCount(1);
		} else {
			user.setErrorCount(user.getErrorCount() + 1);
		}
	}

	
	public Member deleteById(Integer id) {
		Member bean = dao.deleteById(id);
		return bean;
	}

	public Member[] deleteByIds(Integer[] ids) {
		Member[] beans = new Member[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public boolean usernameNotExist(String username) {
		return dao.countByUsername(username) <= 0;
	}
	
	public Integer errorRemaining(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		Member user = getByUsername(username);
		if (user == null) {
			return null;
		}
		long now = System.currentTimeMillis();
		ConfigLogin configLogin = configMng.getConfigLogin();
		int maxErrorTimes = configLogin.getErrorTimes();
		int maxErrorInterval = configLogin.getErrorInterval() * 60 * 1000;
		Integer errorCount = user.getErrorCount();
		Date errorTime = user.getErrorTime();
		if (errorCount <= 0 || errorTime == null
				|| errorTime.getTime() + maxErrorInterval < now) {
			return maxErrorTimes;
		}
		return maxErrorTimes - errorCount;
	}

	private MemberDao dao;

	@Autowired
	public void setDao(MemberDao dao) {
		this.dao = dao;
	}
	private ConfigMng configMng;

	private PwdEncoder pwdEncoder;
	
	@Autowired
	public void setPwdEncoder(PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}
	@Autowired
	public void setConfigMng(ConfigMng configMng) {
		this.configMng = configMng;
	}

}
