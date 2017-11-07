package com.dgut.main.action.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import com.dgut.main.entity.FirstType;
import com.dgut.main.manager.main.FirstTypeMng;
import com.dgut.main.web.WebErrors;

@Controller
public class FirstTypeAct {

	@RequestMapping("/firstType/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<FirstType> list = firstTypeMng.getList();
		model.addAttribute("list", list);
		return "firstType/list";
	}
	
	@RequestMapping("/firstType/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		return "firstType/add";
	}
	
	@RequestMapping("/firstType/o_save.do")
	public String save(FirstType bean,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
//			return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return "firstType/add";
		}
		bean = firstTypeMng.save(bean);
		return "redirect:v_list.do";
	}
	
	@RequestMapping("/firstType/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			//return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return "firstType/edit";
		}
		model.addAttribute("firstType",firstTypeMng.findById(id));
		return "firstType/edit";
	}
	
	

	@RequestMapping("/firstType/o_update.do")
	public String update(FirstType bean,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			model.addAttribute("errors", errors.getErrors());
			return "firstType/list";
		}
		bean = firstTypeMng.update(bean);
		
		return list(request, model);
	}
	
	
	
	private WebErrors validateEditOrDelete(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		
		return errors;
	}

	

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		FirstType entity = firstTypeMng.findById(id);
		if (errors.ifNotExist(entity, FirstType.class, id)) {
			return true;
		}
		return false;
	}

	private WebErrors validateSave(FirstType bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (bean.getTypeName().length()<3) {
			errors.addErrorString("dayu3");
		}
		else if(bean.getTypeName().trim().equals("")){
			errors.addErrorString("类名不能为空白符");
		}
		return errors;
	}

	@Autowired
	private FirstTypeMng firstTypeMng;
}
