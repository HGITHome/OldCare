package com.dgut.MyApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dgut.MyApp.Mypck.MyGeneralRestGateway;
import com.dgut.app.pck.JSONUtils;
import com.dgut.main.Constants;
import com.dgut.main.web.CmsUtils;

@Controller
public class MyAppController {
	
private final static Logger log =LoggerFactory.getLogger(MyAppController.class);

	
	
	@RequestMapping(value="/index.do",method=RequestMethod.GET)
	public void getIndex(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		Map<String,String> jsonMap =new HashMap<String, String>();
		jsonMap.put("error", "-3");
		jsonMap.put("message", "请使用POST请求方式来请求！");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out  =  response.getWriter();
		out.write(JSONUtils.printObject(jsonMap));
		out.flush();
		out.close();
	}
	
	@RequestMapping(value="/index.do" , method=RequestMethod.POST)
	public void getIndex(HttpServletRequest request,HttpServletResponse response) throws IOException{
		StringBuilder errorDescription = new StringBuilder();
		int code = MyGeneralRestGateway.handle(Constants.APP_ENCRYPTION_KEY,
				3000, this, request, response, errorDescription);
		if(code < 0){
			log.error("app请求失败:" + errorDescription.toString());
			Map<String,String> jsonMap = new HashMap<String, String>();
			jsonMap.put("error", "-3");
			jsonMap.put("message", errorDescription.toString());
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(JSONUtils.printObject(jsonMap));
			out.flush();
			out.close();
		}
	}
	
	public String MydeleteRequestData(HttpServletRequest request,HttpServletResponse response ,Map<String,String> parameterMap){
		String result = "";
		Integer opt = Integer.parseInt(parameterMap.get("opt"));
		
		if(opt != MyAppConstants.APP_Login && opt != MyAppConstants.App_check && opt != MyAppConstants.App_recordCheck && opt != MyAppConstants.App_recordInfo
				&& opt != MyAppConstants.App_Regist){
			if(CmsUtils.getMember(request) == null){
				Map<String,String> jsonMap = new HashMap<String, String>();
				jsonMap.put("error", "-2");
				jsonMap.put("message", "太久登录了,需重新登录!");
				try {
					return JSONUtils.printObject(jsonMap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		switch(opt){
		  case MyAppConstants.APP_Login :
			  try {
				result = requestData.login(request,response,parameterMap);
			} catch (IOException e) {
				log.error("登录时:" + e.getMessage());
				e.printStackTrace();
			}
			  break;
		  case MyAppConstants.App_check :
			  try {
				result = requestData.check(request,response,parameterMap);
			} catch (IOException e) {
			   log.error("查询指定老人时" + e.getMessage());
			   e.printStackTrace();
			}
			  break;
		  case MyAppConstants.App_recordCheck :
			  try {
				result = requestData.recordCheck(request,response,parameterMap);
			} catch (IOException e) {
				log.error("查看记录时:" + e.getMessage());
				e.printStackTrace();
			}
			  break;
		  case MyAppConstants.App_recordInfo :
			  try {
				result = requestData.getRecordInfo(request,response,parameterMap);
			} catch (IOException e) {
			   log.error("获取记录选项时:" + e.getMessage());
				e.printStackTrace();
			}
			  break;
		}
		
		return result;
	}
	
	@Autowired
	private MyRequestData requestData;
	
}
