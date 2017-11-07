package com.dgut.main.action.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.asm.commons.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;











import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.manager.main.AddRecordItem;
import com.dgut.main.manager.main.AssesCategoryMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.AssesTypeMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.web.WebErrors;
@Controller
public class AddItemsAct {
	@RequestMapping("/record/v_addRecordFrame.do")
	public String addRecordFrame(HttpServletRequest request, ModelMap model) {
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("cList", cList);
		return "old/addFrame";
		
	}
	@RequestMapping("/record/u_updateItems")
	public String updateItems(HttpServletRequest request, ModelMap model){
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("cList", cList);
		return "old/updateItems";
	}
	@RequestMapping("/record/A_topName.do")
	public String saveCategory(HttpServletRequest request,AssesCategory assesCategory,ModelMap model){
		WebErrors errors = validateCategoryType(assesCategory,request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		addRecordItem.saveItem(assesCategory);
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("cList", cList);
		return "old/addFrame";
	}
	@RequestMapping ("/record/v_addAsses_types.do")
	public String saveAssesTypes(HttpServletRequest request,AssesType assesType,Integer category_id,ModelMap model){
		WebErrors errors = validateAssesType(assesType,category_id,request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		assesType.setCategory(categoryMng.findById(category_id));
		assesTypeMng.save(assesType);
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("cList", cList);
		return "old/addFrame";
		
	}
	
	@RequestMapping(value = "/record/v_ajax.do", method=RequestMethod.POST)
	public void ajax(Integer categoryId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//response.getOutputStream().write("中国,日本".getBytes("UTF-8"));
		StringBuffer assesType =  assesTypeMng.findAssesByCategoryId(categoryId);
		response.getOutputStream().write(assesType.toString().getBytes("UTF-8"));
	}
	
	
	@RequestMapping("/record/v_addAsses_Item.do")
	public String saveAsseItem(HttpServletRequest request,AssesItem assesItem,Integer type_id,ModelMap model){
		WebErrors errors = validateAssesItem(request,assesItem,type_id);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		assesItem.setType(assesTypeMng.findById(type_id));
		assesItemMng.save(assesItem);
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("cList", cList);
		return "old/addFrame";
	}
	
	@RequestMapping(value="/record/v_updateCategory.do",method = RequestMethod.POST)
	public void updateCategory(HttpServletRequest request,HttpServletResponse response,AssesCategory category){
		category.setPriority((int)(Math.random()*8+1));
		categoryMng.update(category);
	}
	
	@RequestMapping(value="/record/v_deleteCategory.do",method=RequestMethod.POST)
	public void deleteCategory(HttpServletRequest request,HttpServletResponse response,Integer id){
		categoryMng.deleteById(id);
	}
	
	@RequestMapping(value="/record/v_updateType.do",method = RequestMethod.POST)
	public void updateType(HttpServletRequest request,HttpServletResponse response,AssesType assesType){
		AssesCategory category = assesTypeMng.findById(assesType.getId()).getCategory();
		assesType.setCategory(category);
		assesTypeMng.update(assesType);
	}
	
	@RequestMapping(value="/record/v_deleteType.do",method=RequestMethod.POST)
	public void deleteType(HttpServletRequest request,HttpServletResponse response,Integer id){
		AssesType bean = assesTypeMng.deleteType(id);
	}
	
	@RequestMapping(value="/record/v_updateItem.do", method=RequestMethod.POST)
	public void updateItem(HttpServletRequest request,HttpServletResponse response,AssesItem assesItem,Integer typeId,ModelMap model) throws IOException{	  
		   assesItem.setType(assesTypeMng.findById(typeId));
		   assesItemMng.updateItem(assesItem);
	}
	
	@RequestMapping(value="/record/v_deleteItem.do",method=RequestMethod.POST)
	public void deleteItem(HttpServletRequest request,HttpServletResponse response,AssesItem assesItem,ModelMap model)throws IOException{
		assesItemMng.deleteItem(assesItem);
	}
	

	
	
	private WebErrors validateCategoryType(AssesCategory assesCategory,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if(StringUtils.isBlank(assesCategory.getName())){
			errors.addErrorString("顶级分类名不能为空!");
		}else{
		   if(!(assesCategory.getName().matches("^[\\u4e00-\\u9fa5]{0,5}$") || assesCategory.getName().matches("^[A-Za-z]+$"))){
			    errors.addErrorString("顶级分类名必须是全是5个中文或英文组成!");
		   }
		}
		
		return errors;
	}
	private WebErrors validateAssesType(AssesType assesType,Integer category_id,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if(StringUtils.isBlank(assesType.getTypeName())){
			errors.addErrorString("分类名不能为空!");
		}else{
			if(!(assesType.getTypeName().matches("^[\\u4e00-\\u9fa5]{0,5}$") || assesType.getTypeName().matches("^[A-Za-z]+$"))){
			    errors.addErrorString("分类名必须是全是5个中文或英文组成!");
		   }
		}
		if(category_id == null || category_id.toString().trim().equals("")){
			errors.addErrorString("所属分类必须不为空!");
		}
		return errors;
	}
	private WebErrors validateAssesItem(HttpServletRequest request,AssesItem assesItem,Integer type_id){
		WebErrors errors = WebErrors.create(request);
		if(StringUtils.isBlank(assesItem.getDescription())){
			errors.addErrorString("条目名称不能为空!");
		}else{
			if(!(assesItem.getDescription().matches("^[\\u4e00-\\u9fa5]{0,}$") || assesItem.getDescription().matches("^[A-Za-z]+$"))){
			    errors.addErrorString("条目名必须是全是中文或英文组成!");
		   }
		}
		if(assesItem.getGrade() == null || assesItem.getGrade().toString().trim().equals("")){
			errors.addErrorString("分数不能为空!");
		}
		if(type_id == null || type_id.toString().trim().equals("")){
			errors.addErrorString("所属分类不能为空!");
		}
		return errors;
	}
	
	
	
	
	@Autowired
	private OldMng oldMng;
	@Autowired
	private AssesCategoryMng categoryMng;
	@Autowired
	private AddRecordItem addRecordItem;
	@Autowired
	private AssesTypeMng assesTypeMng;
	@Autowired
	private AssesItemMng assesItemMng;
	
	@Autowired
	private RecordMng recordMng;
}
