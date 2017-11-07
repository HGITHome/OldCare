package com.dgut.main.action.main;


import static com.dgut.common.page.SimplePage.cpn;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.common.image.ImageScale;
import com.dgut.common.page.Pagination;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.upload.UploadUtils;
import com.dgut.common.util.DateUtils;
import com.dgut.common.util.RandomGeneration;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.AdminLogMng;
import com.dgut.main.manager.main.AssesCategoryMng;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.main.web.FrontUtils;
import com.dgut.main.web.WebErrors;


@Controller
public class OldAct {



	@RequestMapping("/old/v_list.do")
	public String list(Integer pageNo,Integer town_id,String username,HttpServletRequest request, ModelMap model) {
		//List<Old> list = oldMng.getList(town_id,username);
		Pagination pagination = oldMng.getList(town_id,username, cpn(pageNo),  CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		model.addAttribute("username", username);
		model.addAttribute("town_id", town_id);

		model.addAttribute("townList",cityMng.findById(1).getTowns());
		//model.addAttribute("list", list);
		//cmsLogMng.operating(request, "cmsOld.log.search",null);
		String currentDate = DateUtils.format2.format(new Date());
		model.addAttribute("currentDate", currentDate);
		return "old/list";
	}


	@RequestMapping("/old/v_delete.do")
	public String delete(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			//return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return "redirect:v_list.do";
		}
		old=oldMng.deleteById(id);
		cityMng.updateOldList();
		adminLogMng.operating(request, "cms.old.delete", "id=" + old.getId()
				+ ";username=" + old.getUsername());
		return "redirect:v_list.do";
	}

	@RequestMapping("old/o_info.do")
	public String info(Integer id,HttpServletRequest request,ModelMap model){
		WebErrors errors = validateEditOrDelete(id, request);
		if(errors.hasErrors()){
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);
		}
		Old old=oldMng.findById(id);
		if(old==null){
			errors.addErrorString("老人不存在");
			return errors.showErrorPage(model);
		}
		
		String dateString = DateUtils.format2.format(new Date());
		oldMng.UpdateCheckTime(dateString,id);
		oldMng.convertOldEntity(old);
		model.addAttribute("old", old);
		return "old/show";
	}
	@RequestMapping("old/o_lineChart.do")
	public String lineChart(Integer id,HttpServletRequest request,ModelMap model){
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);

		}
		Old old=oldMng.findById(id);
		if(old==null){
			errors.addErrorString("老人不存在");
			return errors.showErrorPage(model);
		}
		 List list =recordMng.findByOldId(old.getId());
		 if(list.size()==0){
			 errors.addErrorString("记录不存在");
				return errors.showErrorPage(model);
		 }
		
		Map<String, List> oldRecordMap = oldMng.getOldRecordList(id);
		model.addAttribute("categoryNameList", oldRecordMap.get("categoryNameList"));
		model.addAttribute("RecordTimeList", oldRecordMap.get("Timelist"));
		oldRecordMap.remove("Timelist");
		oldRecordMap.remove("categoryNameList");
		model.addAttribute("oldRecordMap", oldRecordMap);
		model.addAttribute("OldName", old.getUsername());
		return "old/LineChartShow";
	}
	
	@RequestMapping("/old/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		List<City> list=cityMng.getList();

		model.addAttribute("townList", cityMng.findById(1).getTowns());
		return "old/add";
	}

	@RequestMapping("/old/o_save.do")
	public String save(@RequestParam(value = "photo", required = false) MultipartFile photo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(photo, request);
		Town town=null;
		String photoUrl=null,miniUrl=null;
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
  


		town=townMng.findById(Integer.parseInt(request.getParameter("town")));


		if(!photo.isEmpty()){
			String origName = photo.getOriginalFilename();
			String ext = FilenameUtils.getExtension(origName).toLowerCase(
					Locale.ENGLISH);
			try {
				photoUrl=fileRepository.storeByExt("/upload/file", ext, photo);
				ServletContext  context=request.getSession().getServletContext();
				File fi = new File(context.getRealPath(photoUrl)); //大图文件  


				String miniPath=context.getRealPath("/upload/file");
				String miniName=UploadUtils.generateFilename("mini", ext);
				File fo = new File(miniPath,miniName); //将要转换出的小图文件
				miniUrl="/upload/file/"+miniName;


				imageScale.resizeFix(fi, fo,180,180);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				errors.addErrorString("上传老人相片失败");
				return errors.showErrorPage(model);
			} catch (Exception e) {
				errors.addErrorString("上传老人相片失败");
				return errors.showErrorPage(model);
			}
		}

		old=new Old();

		old.setUsername(request.getParameter("username").replace(" ", ""));
		old.setTown(town);
		old.setPhotoUrl(photoUrl);
		old.setMiniUrl(miniUrl);
		old.setGender(Integer.parseInt(request.getParameter("sex")));
		try {
			old.setBirthday(DateUtils.format.parse(request.getParameter("age")));
		} catch (ParseException e) {
			errors.addErrorString("日期解析出错");
			return errors.showErrorPage(model);
		}
		old.setDiploma(request.getParameter("diploma"));

		old.setReligion(Integer.parseInt(request.getParameter("religion")));
		old.setMarry_status(request.getParameter("marriage"));
		old.setOafish(request.getParameter("oafish"));
		old.setIncome(Integer.parseInt(request.getParameter("income")));
		String []living=request.getParameterValues("living[]");
		old.setLiving(handleMsg(living));
		old=multiSelectMsg(request, old);

		old = oldMng.save(old);
		cityMng.updateOldList();
		adminLogMng.operating(request, "cms.old.add", "id=" + old.getId()
				+ ";username=" + old.getUsername());

		return  "redirect:v_list.do";
	}


	private Old multiSelectMsg(HttpServletRequest request,Old old) {
		List<String> list=new ArrayList<String>();
		String others;
		String[] income_ways=request.getParameterValues("incomeways[]");
		Collections.addAll(list, income_ways);
		if(list.contains("A5.4")  ){
			others=request.getParameter("income_others");
			if(others!=null && !others.trim().equals("")){
				old.setIncome_others(others);
			}
			else{
				old.setIncome_others("其他收入方式");
			}
			list.remove("A5.4");	
		}
		else{
			old.setIncome_others("");
		}

		old.setIncome_ways(handleMsg(list));

		list=new ArrayList<String>();
		String[] payways=request.getParameterValues("payways[]");
		Collections.addAll(list, payways);
		if(list.contains("A4.8")){
			others=request.getParameter("pay_others");
			if(others!=null&&!others.trim().equals("")){
				old.setPay_others(others);
			}
			else{
				old.setPay_others("其他支付方式");
			}
			list.remove("A4.8");
		}
		else{
			old.setPay_others("");
		}
		old.setPayways(handleMsg(list));

		list=new ArrayList<String>();
		String[] insanity=request.getParameterValues("insanity[]");
		Collections.addAll(list, insanity);
		if(list.contains("A7.8")){
			others=request.getParameter("insanity_others");
			if(others!=null && !others.trim().equals("")){
				old.setInsantity_others(others);
			}
			else{
				old.setInsantity_others("其他精神病表现");
			}
			list.remove("A7.8");
		}
		else{
			old.setInsantity_others("");
		}
		old.setInsantity(handleMsg(list));

		list=new ArrayList<String>();
		String[] illness=request.getParameterValues("illness[]");
		Collections.addAll(list, illness);
		if(list.contains("A8.13")){
			others=request.getParameter("illness_others");
			if(others!=null && !others.trim().equals("")){
				old.setIllness_others(others);
			}
			else{
				old.setIllness_others("其他疾病");
			}
			list.remove("A8.13");
		}
		else{
			old.setIllness_others("");
		}
		old.setIllness(handleMsg(list));

		list=new ArrayList<String>();

		String[] accident=request.getParameterValues("accident[]");
		Collections.addAll(list, accident);
		if(list.contains("A9.5")){
			others=request.getParameter("accident_others");
			if(others!=null && !others.trim().equals("")){
				old.setAccident_others(others);
			}
			else{
				old.setAccident_others("其他意外");
			}
			list.remove("A9.5");
		}
		else{
			old.setAccident_others("");
		}
		old.setAccident(handleMsg(list));
		return old;
	}




	private String handleMsg(String[] list) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		for(String str:list){
			sb.append(str+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}


	private String handleMsg(List<String> list) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		for(String str:list){
			sb.append(str+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}


	@RequestMapping("/old/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			//return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);
		}
		List<City> list=cityMng.getList();
		model.addAttribute("cityList", list);
		model.addAttribute("old",oldMng.findById(id));
		model.addAttribute("townList", cityMng.findById(1).getTowns());

		return "old/edit";
	}



	@RequestMapping("/old/o_update.do")
	public String update(Integer id,@RequestParam(value = "photo", required = false) MultipartFile photo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave( photo,request);
		String photoUrl=null,miniUrl=null;
		Town town;
		if (errors.hasErrors()) {
			model.addAttribute("errors", errors.getErrors());
			return "old/list";
		}
		Old bean=oldMng.findById(id);
		if(!photo.isEmpty()){
			String origName = photo.getOriginalFilename();
			//获取扩展，获得文件扩展名
			String ext = FilenameUtils.getExtension(origName).toLowerCase(
					Locale.ENGLISH);
			try {
				photoUrl=fileRepository.storeByExt("/upload/file", ext, photo);

				ServletContext  context=request.getSession().getServletContext();
				File fi = new File(context.getRealPath(photoUrl)); //大图文件  


				String miniPath=context.getRealPath("/upload/file");
				String miniName=UploadUtils.generateFilename("mini", ext);
				File fo = new File(miniPath,miniName); //将要转换出的小图文件
				miniUrl="/upload/file/"+miniName;


				imageScale.resizeFix(fi, fo,180,180);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				errors.addErrorString("上传老人相片失败");
				return errors.showErrorPage(model);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				errors.addErrorString("上传老人相片失败");
				return errors.showErrorPage(model);
			}
		}
		town=townMng.findById(Integer.parseInt(request.getParameter("town")));

		bean.setUsername(request.getParameter("username"));
		try {
			bean.setBirthday(DateUtils.format2.parse(request.getParameter("age")));
		} catch (ParseException e) {
			errors.addErrorString("出生日期格式有误");
			return errors.showErrorPage(model);

		}
		bean.setGender(Integer.parseInt(request.getParameter("sex")));
		bean.setDiploma(request.getParameter("diploma"));
		bean.setTown(town);
		bean=multiSelectMsg(request, bean);
		bean.setPhotoUrl(photoUrl);
		bean.setMiniUrl(miniUrl);
		oldMng.updateOldInfo(id);
		cityMng.updateOldList();
		bean = oldMng.update(bean);
		
		adminLogMng.operating(request, "cms.old.update", "id=" + bean.getId()
				+ ";username=" + bean.getUsername());
		//return list(request, model);
		return "redirect:v_list.do";
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
		Old entity = oldMng.findById(id);
		if (errors.ifNotExist(entity, Old.class, id)) {
			return true;
		}
		return false;
	}

	private WebErrors validateSave(@RequestParam(value = "photo", required = false) MultipartFile photo,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		String username=request.getParameter("username");

		String sex=request.getParameter("sex");
		String diploma=request.getParameter("diploma");
		String age=request.getParameter("age");
		String religion=request.getParameter("religion");
		String marriage=request.getParameter("marriage");
		String []living=request.getParameterValues("living[]");
		String []illness=request.getParameterValues("illness[]");
		String income=request.getParameter("income");
		String[] income_ways=request.getParameterValues("incomeways[]");
		String[] pay_ways=request.getParameterValues("payways[]");
		String oafish=request.getParameter("oafish");
		String[] insanity=request.getParameterValues("insanity[]"); 
		String town_id=request.getParameter("town");
		String []accident=request.getParameterValues("accident[]");
		if(username==null || photo==null ||sex==null|| age==null || town_id==null || diploma==null ||
				religion==null|| marriage==null || living==null|| income==null || income_ways==null||
				pay_ways==null|| oafish==null|| insanity==null || illness==null||
				accident==null){
			errors.addErrorString("参数不完整");
			return errors;

		}

		/**
		 * 这是一个Java操作文件的常用库，是Apache对java的IO包的封装，这里面有两个非常核心的类FilenameUtils跟FileUtils，
		 * 其中FilenameUtils是对文件名操作的封装;FileUtils是文件封装，开发中对文件的操作，几乎都可以在这个框架里面找到
		 */
		if(username.trim().equals("")||age.trim().equals("")||income.trim().equals("")){
			errors.addErrorString("请填写完信息再提交");
			return errors;
		}
		else if(!photo.isEmpty()){
			String photoOrigName = photo.getOriginalFilename();
			String photoExt = FilenameUtils.getExtension(photoOrigName).toLowerCase(
					Locale.ENGLISH);
			if (!"GIF,JPG,JPEG,PNG,BMP".contains(photoExt.toUpperCase())) {
				errors.addErrorString("文件格式有误，请上传图片(gif,jpg,jpeg,png,bmp)文件");
				return errors;
			}


		}

		return errors;
	}

	@Autowired
	private ImageScale imageScale;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private OldMng oldMng;

	@Autowired
	private CityMng cityMng;

	@Autowired
	private TownMng townMng;

	@Autowired
	private AdminLogMng adminLogMng;
	
	@Autowired
	private RecordMng recordMng;
	@Autowired
	private AssesCategoryMng categoryMng;
	private Old old;
	
	private City city;
	private Record record;
}