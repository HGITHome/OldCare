package com.dgut.MyApp.Mypck;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dgut.MyApp.MyAppController;
import com.dgut.app.pck.Encrypt;


public class MyGeneralRestGateway {
	public static int handle(String aPP_ENCRYPTION_KEY,
			int allowTimespanSeconds, MyAppController myAppController,
			HttpServletRequest request, HttpServletResponse response,
			StringBuilder errorDescription) throws IOException {
		
		errorDescription.delete(0, errorDescription.length());
		request.setCharacterEncoding("UTF-8");
		if(StringUtils.isBlank(aPP_ENCRYPTION_KEY)){
			throw new RuntimeException("秘钥不能为空!");
		}
		if(request == null || response == null){
			errorDescription.append("上下文request和response不能为空!");
			return -2;
		}
		Set<String> prematerKeys = request.getParameterMap().keySet();
		if(prematerKeys == null){
			errorDescription.append("请求参数为空!");
			return -3;
		}
		Map<String,String> parameterMap = new HashMap<String, String>();
		String _s = "";
		String _t = "";
		for(String key : prematerKeys){ 
			if("_s".equals(key)){
				_s = request.getParameter("_s");
			}else{
				if(key.equals("_t")){
					_t = URLDecoder.decode(request.getParameter("_t"), "utf-8");
					parameterMap.put(key, _t);
				}else{
					parameterMap.put(key, URLDecoder.decode(request.getParameter(key),"utf-8"));
				}
			}
		}
		
		if(StringUtils.isBlank(_s) || StringUtils.isBlank(_t)){
			errorDescription.append("缺少_s或_t的参数!");
			return -4;
		}
		
		StringBuffer signData = new StringBuffer();
		List<String> parameterKeyList = new ArrayList<String>(parameterMap.keySet());
		Collections.sort(parameterKeyList);
		
		for(int i = 0; i < parameterKeyList.size(); i ++){
			signData.append(parameterKeyList.get(i) + "=" + parameterMap.get(parameterKeyList.get(i)) + (i < parameterKeyList.size() - 1 ? "&" : "")); 
		}
		
		Date Result = getLongGoneDate();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	    try
	    {
	      Result = formatter.parse(_t);
	    }
	    catch (Exception localException) {
	    }
	    
	    Date timestamp = Result;
	    long requestTime = Math.abs((timestamp.getTime() - System.currentTimeMillis())/1000L);
	    if((allowTimespanSeconds > 0)&& (requestTime > allowTimespanSeconds)){
	    	errorDescription.append("请求超时!");
	    	return -5;
	    	
	    }
		if(!_s.equalsIgnoreCase(Encrypt.MD5(signData + aPP_ENCRYPTION_KEY, "utf-8"))){
			errorDescription.append("非法访问!");
			return -6;
		}
		parameterMap.remove("_t");
		String result = "";
	    try
	    {
	      result = myAppController.MydeleteRequestData(request,response,parameterMap);
	    } catch (Exception e) {
	      e.printStackTrace();
	      errorDescription.append("应用程序的代理回调程序遇到异常，详细原因是：" + e.getMessage());

	      return -7;
	    }

	    if (!StringUtils.isBlank(result)) {
	      response.setCharacterEncoding("UTF-8");
	      PrintWriter out = response.getWriter();
	      out.write(result);
	      out.flush();
	      out.close();
	    }
		
		
		return 0;
	}

	private static Date getLongGoneDate() {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(new Date());
		    calendar.add(1, -30);

		    return calendar.getTime();
	}
}
