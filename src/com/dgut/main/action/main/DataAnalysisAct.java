package com.dgut.main.action.main;

import static com.dgut.common.page.SimplePage.cpn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.web.WebErrors;




@Controller
public class DataAnalysisAct {
	@RequestMapping("/dataAnalysis/v_analysis_dendrogram.do")
	public String analysis_dendrogram(ModelMap model) {
		Map<String, List<Object[]>> map = oldMng.getDataCount();
		model.put("DataTotal", oldMng.DataTotal());
		model.addAttribute("map", map);
        return "data/analysis_dendrogram";  
	}
	
	
	
	@RequestMapping("/dataAnalysis/v_analysis_piechart.do")
	public String analysis_piechart(ModelMap model) {
		Map<String, List<Object[]>> map = oldMng.getDataCount();
		model.put("DataTotal", oldMng.DataTotal());
		model.addAttribute("map", map);
        return "data/analysis_piecharts";  
	}
	
	
	
	@RequestMapping("/dataAnalysis/v_list.do")
	public String analysis_LineChar(Integer pageNo,String enrollerName,String old_name,
			Integer sex_id,String diploma_id,Integer religion_id,String marryStatus_id,
			Integer town_id,HttpServletRequest request,ModelMap model){
//		List<Old> recordList =recordMng.findAllRecord();
//		List<City> list=cityMng.getList();

//		model.addAttribute("townList", cityMng.findById(1).getTowns()); 
//		model.put("recordList", recordList);
//		
		
 Pagination pagination = recordMng.getRecordList(enrollerName,old_name,sex_id,diploma_id,religion_id,marryStatus_id,town_id,cpn(pageNo),  CookieUtils.getPageSize(request));
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		model.addAttribute("old_name", old_name);
		model.addAttribute("enrollerName",enrollerName);
		model.addAttribute("townList",cityMng.findById(1).getTowns());
		
		if(sex_id!=null){
			model.addAttribute("sex_id", sex_id);
		}else{
			model.addAttribute("sex_id", "");
		}
		
		
		if(diploma_id!=null){
			model.addAttribute("diploma_id", diploma_id);
		}else{
			model.addAttribute("diploma_id", "");
		}
		
		
		if(religion_id!=null){
			model.addAttribute("religion_id", religion_id);
		}else{
			model.addAttribute("religion_id", "");
		}
		
		
	    if(marryStatus_id != null){
	    	model.addAttribute("marryStatus_id", marryStatus_id);
	    }else{
	    	model.addAttribute("marryStatus_id", "");
	    }
	    
		
		if(town_id!=null){
			model.addAttribute("town_id",town_id);
		}
		else{
			model.addAttribute("town_id","");
		}
		
		return "data/analysis_Line_Char";
	}
	
	
	@RequestMapping("/dataAnalysis/v_analysis_LineCharShow.do")
	public String analysis_LineCharShow(Integer old_id,HttpServletRequest request,ModelMap model){
	
	     	Old old=oldMng.findById(old_id);
	        Map<String, List> oldRecordMap = oldMng.getOldRecordList(old_id);
	        model.addAttribute("categoryNameList", oldRecordMap.get("categoryNameList"));
	        model.addAttribute("RecordTimeList", oldRecordMap.get("Timelist"));
	        oldRecordMap.remove("Timelist");
	        oldRecordMap.remove("categoryNameList");
	        model.addAttribute("oldRecordMap", oldRecordMap);
	    
	        model.addAttribute("old_username", old.getUsername());
	       
		
		
		return "data/analysis_Line_Char_Show";
	}
	
	
	
	
	@Autowired
	private OldMng oldMng;
	
	@Autowired
	private RecordMng recordMng;
	
	@Autowired
	private CityMng cityMng;

}
