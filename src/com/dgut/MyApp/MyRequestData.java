package com.dgut.MyApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.transaction.JOnASTransactionManagerLookup;
import org.springframework.beans.factory.annotation.Autowired;

import com.dgut.app.pck.Encrypt;
import com.dgut.app.pck.JSONUtils;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.DisabledException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.util.DateUtils;
import com.dgut.common.util.StrUtils;
import com.dgut.common.web.RequestUtils;
import com.dgut.main.Constants;
import com.dgut.main.dao.AssesCategoryDao;
import com.dgut.main.dao.OldDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Authentication;
import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.AssesTypeMng;
import com.dgut.main.manager.main.AuthenticationMng;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.member.manager.MemberAuthenticationMng;
import com.dgut.member.manager.MemberLogMng;
import com.dgut.member.manager.MemberMng;
import com.dgut.member.manager.impl.MemberAuthenticationMngImpl;

public class MyRequestData {
	public String login(HttpServletRequest request,HttpServletResponse response,Map<String,String> parameterMap) throws IOException{
		
		String mobile = parameterMap.get("mobile");
		String password = parameterMap.get("pwd");
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(mobile)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "请输入手机号码");
			return JSONUtils.printObject(jsonMap);
		}
		
		if(!StrUtils.isMobileNum(mobile)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "请输入正确的手机号码");
			return JSONUtils.printObject(jsonMap);
		}
		
		if(StringUtils.isBlank(password)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "请输入密码");
			return JSONUtils.printObject(jsonMap);
		}
		password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
		String ip = RequestUtils.getIpAddr(request);
		
		Authentication auth;
		try {
			auth = adminAuthenMng.login(mobile, password, ip,
					request, response, session);
			adminMng.updateLoginInfo(auth.getUid(), ip);
			admin = adminMng.findById(auth.getUid());
			if (admin.getDisabled()) {
				// 如果已经禁用，则退出登录。
				memberAuthenMng.deleteById(auth.getId());
				session.logout(request, response);
				throw new DisabledException("user disabled");
			}
		} catch (UsernameNotFoundException e) {
		
		   jsonMap.put("error", "-3");
		   jsonMap.put("message", "用户不存在");
		   return JSONUtils.printObject(jsonMap);
		   
		} catch (BadCredentialsException e) {
			jsonMap.put("error","-3");
			jsonMap.put("message", "用户名或密码错误");
			return JSONUtils.printObject(jsonMap);
		}catch(DisabledException e){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "用户被禁止");
			return JSONUtils.printObject(jsonMap);
		}
		
		
		jsonMap.put("error", "-1");
		jsonMap.put("message", "登录成功");
		jsonMap.put("phone", admin.getUsername());
		//jsonMap.put("realname", member.getRealname());
		jsonMap.put("uid", Encrypt.encrypt3DES(admin.getId() + "",
				Constants.ENCRYPTION_KEY));
		jsonMap.put("loginCount", admin.getLoginCount()+"");
		jsonMap.put("lastLoginIp", admin.getLastLoginIp());
		jsonMap.put("lastLoginTime",
				DateUtils.dateToString(admin.getLastLoginTime()));
		jsonMap.put("token", auth.getId());
		jsonMap.put("expires_in", MemberAuthenticationMngImpl.timeout/1000+"");
		
		
		
		JsonConfig config = new JsonConfig();
		//config.setExcludes(new String[]{"towns","birthday","records","town","hashCode","handler","hibernateLazyInitializer"}); 
		config.setExcludes(new String[]{"city","birthday","accident","accident_others","photoUrl",
				"diploma","gender","illness","illness_others","income","income_others",
				"income_ways","insantity","insantity_others","living","marry_status","oafish",
				"pay_others","payways","religion","town",
				"records","hashCode","handler","hibernateLazyInitializer"}); 
		
		jsonMap = getTownList(request,response,parameterMap,jsonMap);
		
		return JSONObject.fromObject(jsonMap,config).toString();
	}
	
	
	public Map<String,Object> getTownList(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameterMap,Map<String,Object> jsonMap) throws IOException {
		 city = cityMng.findById(1);
		 String oldsCode = parameterMap.get("oldsCode");
		 if(oldsCode == null || !oldsCode.equalsIgnoreCase(city.getCacheCode())){
			 List<Town> townList = city.getTowns();
			 List<Town> townArrayList = new ArrayList<Town>();
		    for(Town town : townList){
			   if(town.getOlds().size() > 0){
			     townArrayList.add(town);
			  }
		   }
		 jsonMap.put("cityName", city.getName());
		 jsonMap.put("townList", townArrayList);
		 jsonMap.put("oldsCode", city.getCacheCode());
		 }else{
			 jsonMap.put("oldsCode",city.getCacheCode());
		 }
		return jsonMap;
	}
	
	
	public String check(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameterMap) throws IOException {
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		 String old_id = parameterMap.get("cid");
		 if(StringUtils.isBlank(old_id)){
			 jsonMap.put("error", "-3");
			 jsonMap.put("message", "老人id为空");
			 return JSONUtils.printObject(jsonMap);
		 }
		 old = oldMng.findById(Integer.parseInt(old_id));
		 if(old == null){
			 jsonMap.put("error", "-3");
			 jsonMap.put("message", "该老人不存在");
		 }
		 JsonConfig config = new JsonConfig();
		 config.setExcludes(new String[]{"checkTime","NowTime","city","olds","records","hashCode","handler","hibernateLazyInitializer"});
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
			
		 String dateString = DateUtils.format2.format(new Date());
		 oldMng.UpdateCheckTime(dateString,Integer.parseInt(old_id));
		 oldMng.convertOldEntity(old);
		 jsonMap.put("error", "0");
		 jsonMap.put("message", "查询成功");
		 jsonMap.put("old", old);
		 return JSONObject.fromObject(jsonMap,config).toString();
	}

	public String recordCheck(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameterMap) throws IOException {
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String old_id = parameterMap.get("cid");
		if(StringUtils.isBlank(old_id)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "老人不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		old = oldMng.findById(Integer.parseInt(old_id));
		if(old == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "老人不存在");
			return JSONUtils.printObject(jsonMap);
		}
		List<Record> recordList = dao.getOldRecordList(Integer.parseInt(old_id));
		
		if(recordList == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "老人记录不存在");
			return JSONUtils.printObject(jsonMap);
		}
		
		Map<String,Object> SumMap = null;
		Map<String,Map<String,Object>> recordMap = new LinkedHashMap<String, Map<String,Object>>();
		List<AssesCategory> cateList = categoryDao.getList();// 获得日常活动，精神状态，感知沟通，社会参与的名字
		List list = null;
		List<Date> Timelist = new ArrayList<Date>();
		List<String> categoryNameList = new ArrayList<String>();
		for(Record entity : recordList){
			Timelist.add(entity.getRecord_time());
		}
			for (Record entity : recordList) {
				SumMap = new LinkedHashMap<String,Object>();
				if (entity != null) {
					Set<AssesItem> items = entity.getScores();
				  for (AssesCategory category : cateList) {
					  categoryNameList.add(category.getName());
					  int sum = 0;
					  for (AssesItem item : items) {
						if (item.getType().getCategory().getId().equals(category.getId())) {
							sum += item.getGrade();
						 }
					   }
					  SumMap.put(category.getName(), sum);
				  }	
			  }
				recordMap.put(entity.getRecord_time()+"", SumMap);
		  }
		for(int i = 0; i < recordList.size(); i ++){
				recordList.get(i).setRecordMap(recordMap.get(recordList.get(i).getRecord_time()+""));
			}
		jsonMap.put("error", "-1");
		jsonMap.put("message", "查询成功!");
		jsonMap.put("recordList", recordList);
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
		
		return JSONObject.fromObject(jsonMap,config).toString();
	}
	
	

	public String getRecordInfo(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameterMap) throws IOException {
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		List<AssesCategory> selectList= categoryDao.getList();
		if(selectList == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "获取记录选项失败");
			return JSONUtils.printObject(jsonMap);
		}
		jsonMap.put("error","-1");
		jsonMap.put("message", "获取记录选项成功");
		jsonMap.put("selectList", selectList);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"type","record","category","handler","hibernateLazyInitializer"});
		return JSONObject.fromObject(jsonMap,config).toString();
	}

	
	private OldDao dao;

	@Autowired
	public void setDao(OldDao dao) {
		this.dao = dao;
	}
	
	
	@Autowired
	private AssesCategoryDao categoryDao;
	
	@Autowired
	private MemberAuthenticationMng memberAuthenMng;

	@Autowired
	private AuthenticationMng adminAuthenMng;

	@Autowired
	private com.dgut.common.web.session.SessionProvider session;

	@Autowired
	private AssesItemMng assesItemMng;
	
	@Autowired
	private AssesTypeMng typeMng;
	
	@Autowired
	private CityMng cityMng;

	@Autowired
	private TownMng townMng;
	
	@Autowired
	private MemberMng memberMng;

	@Autowired
	private MemberLogMng memberLogMng;
	
	@Autowired
	private AdminMng adminMng;
    
	@Autowired
    private OldMng oldMng;
	
	@Autowired
	private RecordMng recordMng;
	
	private Admin admin;
   
	private City city;

	private Old old;

	
	
	
}
