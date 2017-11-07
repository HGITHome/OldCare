package com.dgut.app;

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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dgut.app.pck.GeneralRestGateway;
import com.dgut.app.pck.GeneralRestGatewayInterface;
import com.dgut.app.pck.JSONUtils;
import com.dgut.common.upload.FileRepository;
import com.dgut.main.Constants;
import com.dgut.main.web.CmsUtils;

@Controller
public class AppController implements GeneralRestGatewayInterface {
	private static final Logger log = LoggerFactory
			.getLogger(AppController.class);

	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public void getIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("error", "-3");
		jsonMap.put("msg", "请使用post请求");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(JSONUtils.printObject(jsonMap));
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/index.do", method = RequestMethod.POST)
	public void index(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		StringBuilder errorDescription = new StringBuilder();
		int code = GeneralRestGateway.handle(Constants.APP_ENCRYPTION_KEY,
				3000, this, request, response, errorDescription);
		if (code < 0) {
			log.error("app请求失败:" + errorDescription.toString());
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("error", "-3");
			jsonMap.put("msg", errorDescription.toString());
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(JSONUtils.printObject(jsonMap));
			out.flush();
			out.close();
		}
	}

	@Override
	public String delegateHandleRequest(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> parameters,
			StringBuilder paramStringBuilder) throws RuntimeException {

		String result = null;
		Integer OPT = Integer.valueOf(parameters.get("opt"));
		
		//需要登录
		if (OPT != AppConstants.APP_REGISTER && OPT != AppConstants.APP_Login&& OPT != AppConstants.APP_Citys
				&& OPT!=AppConstants.APP_ForgetPassword && OPT!=AppConstants.APP_OLD_RECORDS 
				&& OPT!=AppConstants.APP_OLD_INFO && OPT!=AppConstants.APP_RECORD_INFO
				&& OPT!=AppConstants.APP_TOWN_OLDS) {
			if (CmsUtils.getMember(request) == null) {
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("error", -2);
				jsonMap.put("msg", "太久未登录，请重新登录");
				try {
					return JSONUtils.printObject(jsonMap);
				} catch (IOException e) {
				}
			}
		}

		switch (OPT) {
		case AppConstants.APP_REGISTER:
			try {
				result = requestData.regist(request, response, parameters);
			} catch (IOException e) {
				log.error("注册时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_Login:
			try {
				result = requestData.login(request, response, parameters);
			} catch (IOException e) {
				log.error("登录时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_ForgetPassword:
			try {
				result = requestData.forgetPwd(request, response, parameters);
			} catch (IOException e) {
				log.error("忘记密码时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_OLD_INFO:
			try {
				result = requestData.findOldInfo(request, response, parameters);
			} catch (IOException e) {
				log.error("查看老人信息时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_TOWN_OLDS:
			result = requestData.findOldByTown(request, response, parameters);
			break;
		case AppConstants.APP_OLD_RECORDS:
			try {
				result = requestData.recordList(request, response, parameters);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("查询老人记录圈时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_RECORD_INFO:
			try {
				result = requestData.recordInfo(request, response, parameters);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("查询老人记录时：" + e.getMessage());
			}
			break;
		case AppConstants.APP_RECORD_Delete:
			
				result = requestData.recordDelete(request, response, parameters);
			
			break;
		case AppConstants.APP_TEST:
			try {
				result = requestData.test(request, response, parameters);
			} catch (IOException e) {
				log.error("测试时：" + e.getMessage());
			}
			break;

		// case AppConstants.APP_CodeVerify :
		// try {
		// result = requestData.codeVerify(request, response, parameters);
		// } catch (IOException e) {
		// log.error("发送手机验证码时："+e.getMessage());
		// }
		// break;
		default:
			break;
		}
		// System.out.println(" 返回结果："+result);
		return result;
	}

	@Autowired
	private RequestData requestData;
	
	
}
