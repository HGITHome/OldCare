package com.dgut.main.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.dgut.main.entity.City;
import com.dgut.main.entity.FirstType;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.FirstTypeMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.main.web.WebErrors;

@Controller
public class CityAct {

	@RequestMapping("/cityType.do")
	@ResponseBody
	public Map<String,Object> list(HttpServletRequest request, int id) {
		//String resultMessage="";
		City city=cityMng.findById(id);
		List<Town> towns=city.getTowns();
		if(towns.size()>0){
			request.setAttribute("towns",towns);
			//resultMessage="OK";
		}
		else{
			//resultMessage="未找到信息";
		}
		Map<String,Object> returnMap=new HashMap<String ,Object>();
		returnMap.put("towns", towns);
		return returnMap;
	}
	@Autowired
	private CityMng cityMng;
	
	
	
	@Autowired
	private TownMng townMng;
	
	
}
