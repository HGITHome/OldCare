package com.dgut.app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.app.pck.Encrypt;
import com.dgut.app.pck.JSONUtils;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.DisabledException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.sns.spi.SmsWebApiKit;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.util.DateUtils;
import com.dgut.common.util.StrUtils;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.core.dao.MessageDao;
import com.dgut.main.Constants;

import com.dgut.main.dao.OldDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.Authentication;
import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.AdminLogMng;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AuthenticationMng;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.main.web.CmsUtils;
import com.dgut.member.entity.Member;
import com.dgut.member.entity.MemberAuthentication;
import com.dgut.member.manager.MemberAuthenticationMng;
import com.dgut.member.manager.MemberLogMng;
import com.dgut.member.manager.MemberMng;
import com.dgut.member.manager.impl.MemberAuthenticationMngImpl;


@Service
public class RequestData {

	/**
	 * 注册 opt = 1
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		String mobile = parameters.get("mobile");
		String password = parameters.get("pwd");
		String code = parameters.get("code");
		if (StringUtils.isBlank(password)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "密码不能为空");
			return JSONUtils.printObject(jsonMap);
		}


		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
		Map<String, String> m = RequestData.codeVerify(mobile, code);
		if (m.get("error").equals("-3")) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", m.get("msg"));
			return JSONUtils.printObject(jsonMap);
		}

		boolean notExist = memberMng.usernameNotExist(mobile);
		if(notExist==false){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "该手机号码已注册");
			return JSONUtils.printObject(jsonMap);
		}
		String ip = RequestUtils.getIpAddr(request);
		Member member = memberMng.saveMember(mobile, password, ip, null	, null);
		jsonMap.put("error", "0");
		jsonMap.put("msg", "注册成功");
		jsonMap.put("phone", member.getUsername());
		jsonMap.put("realname", member.getRealname());
		jsonMap.put("uid", Encrypt.encrypt3DES(member.getId() + "",
				Constants.ENCRYPTION_KEY));
		return JSONUtils.printObject(jsonMap);
	}
	/**
	 * 手机验证码验证
	 * @return
	 */
	public static Map<String, String> codeVerify(String mobile,String code)
	{
		Map<String, String> jsonMap = new HashMap<String, String>();
		if (!StrUtils.isMobileNum(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "手机号码错误");
			return jsonMap;
		}
		if (StringUtils.isBlank(code)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "验证码不能为空");
			return jsonMap;
		}
		try {
			String c = SmsWebApiKit.checkcode(mobile, "86", code);
			if (c.equals("200")) {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "验证成功");
			}else if(c.equals("468")){
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "验证码错误");
			}else if(c.equals("467")){
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "请求校验验证码频繁");
			}else {
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "code:"+c);
			}
		} catch (Exception e) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请求失败"+e.getLocalizedMessage());
		}

		return jsonMap;
	}
	/**
	 * 用户登录(opt=2) 
	 * 登录成功后返回的token时长是2天，也就是2天内没操作需要重新登录
	 * 如果在2天内的时候登录的话，token时长重新计算
	 * 
	 * @param mobile
	 *            用户名   
	 * @param pwd  
	 *            密码
	 * @throws IOException
	 */
	public String login(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String mobile = parameters.get("mobile");
		String password = parameters.get("pwd");

		if (StringUtils.isBlank(mobile)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入手机号码");
			return JSONUtils.printObject(jsonMap);
		}
		if(!StrUtils.isMobileNum(mobile)){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入正确的手机号码");
			return JSONUtils.printObject(jsonMap);
		}

		if (StringUtils.isBlank(password)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "请输入密码");
			return JSONUtils.printObject(jsonMap);
		}

		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);

		String ip = RequestUtils.getIpAddr(request);
		Member member;
		Authentication auth;
		try {
			auth = adminAuthenMng
					.login(mobile, password, ip, request, response, session);
			adminMng.updateLoginInfo(auth.getUid(), ip);
			admin = adminMng.findById(auth.getUid());
			if (admin.getDisabled()) {
				// 如果已经禁用，则退出登录。
				memberAuthenMng.deleteById(auth.getId());
				session.logout(request, response);
				throw new DisabledException("user disabled");
			}
			adminLogMng.loginSuccess(request, admin, "登录成功");
		} catch (UsernameNotFoundException e) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "用户名不存在");
			return JSONUtils.printObject(jsonMap);
		} catch (BadCredentialsException e) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "用户名或密码错误");
			return JSONUtils.printObject(jsonMap);
		} catch (DisabledException e) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "账户被禁用，请联系管理员");
			return JSONUtils.printObject(jsonMap);
		}
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "登录成功");
		jsonMap.put("phone", admin.getUsername());
		//jsonMap.put("realname", member.getRealname());
		jsonMap.put("uid", Encrypt.encrypt3DES(admin.getId() + "",
				Constants.ENCRYPTION_KEY));
		jsonMap.put("loginCount", admin.getLoginCount());
		jsonMap.put("lastLoginIp", admin.getLastLoginIp());
		jsonMap.put("lastLoginTime",
				DateUtils.dateToString(admin.getLastLoginTime()));
		jsonMap.put("token", auth.getId());
		jsonMap.put("expires_in", MemberAuthenticationMngImpl.timeout/1000);
		return JSONUtils.printObject(jsonMap);
	}
	/**
	 * 忘记密码 opt = 3
	 */
	public String forgetPwd(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		String mobile = parameters.get("mobile");
		String password = parameters.get("pwd");
		String code = parameters.get("code");
		if (StringUtils.isBlank(password)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "密码不能为空");
			return JSONUtils.printObject(jsonMap);
		}

		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);

		Map<String, String> m = RequestData.codeVerify(mobile, code);
		if (m.get("error").equals("-3")) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", m.get("msg"));
			return JSONUtils.printObject(jsonMap);
		}

		Member member = memberMng.findByUsername(mobile);
		if(member==null){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "手机号码未注册");
			return JSONUtils.printObject(jsonMap);
		}
		member = memberMng.updateMember(member, password);
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "更新密码成功");
		return JSONUtils.printObject(jsonMap);
	}





	public String recordList(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		JsonConfig config = new JsonConfig();


		config.setExcludes(new String[]{"town","old",
				"video_url","enroller","map","record","assesItems","types","scores","handler","hibernateLazyInitializer",
		"hashCode"});

		//config.setExcludes(new String[]{"handler","hibernateLazyInitializer"}); 
		//config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {

			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object processObjectValue(String key, Object value,
					JsonConfig config) {
				// TODO Auto-generated method stub
				if(value==null){
					return "";
				}
				if(value instanceof java.util.Date){
					String str=DateUtils.format2.format((java.util.Date)value);
					return str;
				}
				return null;
			}


		});
		String id=request.getParameter("cid");
		if (StringUtils.isBlank(id)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "老人不存在");
			return JSONUtils.printObject(jsonMap);
		}
		Integer oldId = Integer.parseInt(id);
		Old old = oDao.findById(oldId);
		if (old == null) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "老人不存在");
			return JSONUtils.printObject(jsonMap);
		}
		String client=parameters.get("recordsCode");
		if(client==null ||!client.trim().equals(old.getHashCode())){
			List<Record> recordList=old.getRecords();
			List<Record> returnList=null;
			String pageNo=request.getParameter("pageNo");
			String pageSize=request.getParameter("pageSize");
			Integer page_No,page_size;
			if(pageNo==null){
				page_No=1;
			}
			else{
				page_No=Integer.parseInt(pageNo);
			}
			if(pageSize==null){
				page_size=10;
			}
			else{
				page_size=Integer.parseInt(pageSize);
			}
			if(recordList.size()==0){
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "查询成功,暂无记录");
				return JSONUtils.printObject(jsonMap);
			}
			else{
				int total=recordList.size()/page_size==0? 1:recordList.size()/page_size+1;
				if(page_No>total){
					jsonMap.put("error", "-1");
					jsonMap.put("msg", "请求的页码不存在");
					return JSONUtils.printObject(jsonMap);
				}
				else{
					if(total==1){
						returnList=recordList.subList((page_No-1)*page_size,recordList.size());
					}
					else{
						int surplus=recordList.size()-(page_No-1)*page_size;
						if(surplus/page_size==0){
							returnList=recordList.subList((page_No-1)*page_size,recordList.size());
						}
						else{
							returnList=recordList.subList((page_No-1)*page_size,(page_No)*page_size);
						}

					}



				}
			}
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			jsonMap.put("pageSize", pageSize);
			jsonMap.put("pageNo", pageNo);
			jsonMap.put("records", returnList);
			jsonMap.put("recordsCode", old.getHashCode());
		}
		else{
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功,暂无资料更新");
			jsonMap.put("recordsCode", old.getHashCode());
		}


		return JSONObject.fromObject(jsonMap,config).toString();

	}



	public String findOldInfo(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"records","olds","towns","hashCode","handler","hibernateLazyInitializer"}); 
		//config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {

			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object processObjectValue(String key, Object value,
					JsonConfig config) {
				// TODO Auto-generated method stub
				if(value==null){
					return "";
				}
				if(value instanceof java.util.Date){
					String str=DateUtils.format2.format((java.util.Date)value);
					return str;
				}
				return null;
			}
		});
		String id=request.getParameter("cid");
		if (StringUtils.isBlank(id)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "老人不存在");
			return JSONUtils.printObject(jsonMap);
		}
		Integer oldId = Integer.parseInt(id);
		Old old = oDao.findById(oldId);
		String client=parameters.get("infoCode");

		if(old==null){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "老人不存在");
			return JSONUtils.printObject(jsonMap);   
		}
		if(client==null || !client.trim().equals(old.getInfoCode())){
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");

			jsonMap.put("old", old);

		}
		else{
			
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "暂无资料更新");

		}
		jsonMap.put("infoCode", old.getInfoCode());
		return JSONObject.fromObject(jsonMap,config).toString();
		

	}

	public String recordInfo(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		JsonConfig config = new JsonConfig();


		config.setExcludes(new String[]{"old","enroller","map","record","assesItems","types","summary","handler","hibernateLazyInitializer",
		"hashCode"});
		config.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {

			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object processObjectValue(String key, Object value,
					JsonConfig config) {
				// TODO Auto-generated method stub
				if(value==null){
					return "";
				}
				if(value instanceof java.util.Date){
					String str=DateUtils.format2.format((java.util.Date)value);
					return str;
				}
				return null;
			}


		});
		String id=request.getParameter("rid");
		if (StringUtils.isBlank(id)) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "记录不存在");
			return JSONUtils.printObject(jsonMap);
		}
		Integer record_id = Integer.parseInt(id);
		Record record = rMng.findById(record_id);
		if(record==null){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "记录不存在");
			return JSONUtils.printObject(jsonMap);
		}
		else{
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			jsonMap.put("recordInfo", record);
		}

		return JSONObject.fromObject(jsonMap,config).toString();
	}


	public String findOldByTown(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		JsonConfig config = new JsonConfig();
		//config.setExcludes(new String[]{"towns","birthday","records","town","hashCode","handler","hibernateLazyInitializer"}); 
		config.setExcludes(new String[]{"city","birthday","accident","accident_others","photoUrl",
				"diploma","gender","illness","illness_others","income","income_others",
				"income_ways","insantity","insantity_others","living","marry_status","oafish",
				"pay_others","payways","religion","town",
				"records","hashCode","handler","hibernateLazyInitializer"}); 


		/*List<City> list=new ArrayList<City>();
		Set<Town> towns=null;
		Set<Town> resultTown=null;

		List<City> cityList=cityMng.getList();
		for(City city:cityList){
			resultTown=new HashSet<Town>();
			towns=city.getTowns();
			for(Town town:towns){
				if(town.getOlds().size()>0){
					resultTown.add(town);
				}
			}
			if(resultTown.size()>0){
				city.setTowns(resultTown);
				list.add(city);
			}
		}
		 */

		City city=cityMng.findById(1);
		String client=parameters.get("oldsCode");
		if(client==null || !client.trim().equals(city.getCacheCode())){

			List<Town> townList=new ArrayList<Town>();
			List<Town> towns=city.getTowns();
			for(Town t:towns){
				if(t.getOlds().size()>0){
					townList.add(t);
				}
			}

			jsonMap.put("oldsCode", city.getCacheCode());

			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			jsonMap.put("townList", townList);
			jsonMap.put("cityName", city.getName());
		}
		else{
			jsonMap.put("oldsCode", city.getCacheCode());
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功,暂无更新");

		}

		return JSONObject.fromObject(jsonMap,config).toString();

	}


	public String recordDelete(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String rid=request.getParameter("rid");
		String oid=request.getParameter("oid");
		rMng.deleteById(Integer.parseInt(rid));
		oldMng.updateOldInfo(Integer.parseInt(oid));
		cityMng.updateOldList();
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "删除记录成功");

		return JSONObject.fromObject(jsonMap).toString();
	}


	public String test(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters)
					throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("error", -1);
		jsonMap.put("msg", "请求成功");

		return JSONUtils.printObject(jsonMap);
	}


	@Autowired
	private FileRepository fileRepository;


	@Autowired
	private MemberAuthenticationMng memberAuthenMng;

	@Autowired
	private AuthenticationMng adminAuthenMng;

	@Autowired
	private com.dgut.common.web.session.SessionProvider session;

	@Autowired
	private MemberMng memberMng;

	@Autowired
	private MemberLogMng memberLogMng;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private AdminLogMng adminLogMng;

	@Autowired
	private AdminMng adminMng;

	@Autowired
	private CityMng cityMng;

	@Autowired
	private OldMng oldMng;

	@Autowired
	private MessageDao mDao;

	@Autowired
	private OldDao oDao;

	@Autowired
	private RecordMng rMng;

	private Admin admin;













	/**/
	/*@Autowired
	private CityMng cityMng;*/

}
