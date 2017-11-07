package com.dgut.MyApp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.app.pck.JSONUtils;
import com.dgut.common.image.ImageScale;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.upload.UploadUtils;
import com.dgut.common.util.DateUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.TownMng;

@Controller
public class MyRegistOld {
	
	@RequestMapping(value = "/RegistOld.do",method = RequestMethod.POST)
	public void MyRigistOld(HttpServletRequest request,@RequestParam(value="photo" , required=false) MultipartFile photo,HttpServletResponse response) throws IOException{
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String town_id = request.getParameter("town_id");
		String diploma = request.getParameter("diploma");
		String religion = request.getParameter("religion");
		String marriage = request.getParameter("marriage");
		String[] living = request.getParameterValues("living[]");
		String income = request.getParameter("income");
		String[] incomeways = request.getParameterValues("incomways[]");
		String[] payways = request.getParameterValues("payways[]");
		String oafish = request.getParameter("oafish");
		String[] insanity = request.getParameterValues("insanity[]");
		String[] illness = request.getParameterValues("illness[]");
		String[] accident = request.getParameterValues("accident[]");
		
		
		if(username == null || gender == null || birthday == null || town_id == null || diploma == null ||
				marriage == null || living == null || income == null || incomeways == null || 
				payways == null || oafish == null || insanity.length == 0 ||  illness == null ||
				accident == null){
			jsonMap .put("error", "-3");
			jsonMap.put("message", "信息填写不完整");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		if(photo == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "相片不能为空");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		
		String photoUrl = "";
		String miniPhotoUrl = "";
		String  origName = photo.getOriginalFilename();
		String photoExt = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
		List<String> photoExts = new ArrayList<String>(Arrays.asList("jpg", "img", "bmp","jpeg","png","gif"));
		if(!photoExts.contains(photoExt)){
		     jsonMap.put("error", "-3");
		     jsonMap.put("message", "上传图片格式有错,请上传(jpg,img,bmp,jepg,png,gif)的格式照片");
		     ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		     return;
		}
		try {
			
		photoUrl=fileRepository.storeByExt("/upload/file", photoExt, photo);
		ServletContext  context=request.getSession().getServletContext();
		File fi = new File(context.getRealPath(photoUrl)); //大图文件  

		String miniPath=context.getRealPath("/upload/file");
		String miniName=UploadUtils.generateFilename("mini", photoExt);
		File fo = new File(miniPath,miniName); //将要转换出的小图文件
		miniPhotoUrl="/upload/file/"+miniName;

		imageScale.resizeFix(fi, fo,180,180);
		} catch (Exception e) {
			jsonMap.put("error", "-3");
			jsonMap.put("message", "上传老人相片失败");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		old = new Old();
		town = townMng.findById(Integer.parseInt(town_id));
		if(town == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "镇区不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		old.setUsername(username);
		old.setGender(Integer.parseInt(gender));
		   Date birth;
		try {
			birth = DateUtils.format.parse(birthday);
			 old.setBirthday(birth);
		} catch (ParseException e) {
			jsonMap.put("error", "-3");
			jsonMap.put("message", "日期解析异常");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		 old.setDiploma(diploma);
		 old.setReligion(Integer.parseInt(religion));
		 old.setMarry_status(marriage);
		
		 old.setTown(town);
		 old.setPayways(ArToStr(payways));
		 old.setIncome(Integer.parseInt(income));
		 old.setOafish(oafish);
		 old.setLiving(ArToStr(living));
		 
		 old.setInsantity(ArToStr(insanity));
		 old.setAccident(ArToStr(accident));
		 old.setIllness(ArToStr(illness));
		 old.setIncome_ways(ArToStr(incomeways));
		 old = deleteArray(request,old);
		 
		 old.setPhotoUrl(photoUrl);
		 old.setMiniUrl(miniPhotoUrl);
		 oldMng.save(old);
		 cityMng.updateOldList();
		 jsonMap.put("error", "-1");
		 jsonMap.put("message", "录入成功");
		 ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		 return;
	}
	
	
	private Old deleteArray(HttpServletRequest request, Old old2) {
		List<String> list = new ArrayList<String>();
		String[] insantity = request.getParameterValues("insantity[]");
		Collections.addAll(list, insantity);
		if(list.contains("A7.8")){
			String insantity_others = request.getParameter("insanity_others");
			if(insantity_others == null || insantity_others.trim().equals("")){
				old.setInsantity_others(null);
			}else{
				old.setInsantity_others(insantity_others);
			}
		}
		list.remove("A7.8");
		old.setInsantity(ArToStr(insantity));
		list = new ArrayList<String>();
		String[] incomeways = request.getParameterValues("incomeways");
		Collections.addAll(list, incomeways);
		if(list.contains("A5.4")){
			String income_others = request.getParameter("income_others");
			if(income_others == null || income_others.trim().equals("")){
				old.setIncome_others(null);
			}else{
				old.setIncome_others(income_others);
			}
		}
		list.remove("A5.4");
		old.setIncome_ways(ArToStr(incomeways));
		list = new ArrayList<String>();
		String[] payways = request.getParameterValues("payways[]");
		Collections.addAll(list, payways);
		if(list.contains("A4.8")){
			String pay_others = request.getParameter("pay_others");
			if(pay_others == null || pay_others.trim().equals("")){
				old.setPay_others(null);
			}else{
				old.setPay_others(pay_others);
			}
		}
		list.remove("A4.8");
		old.setPayways(ArToStr(payways));
		list = new ArrayList<String>();
		String[] illness = request.getParameterValues("illness[]");
		Collections.addAll(list, illness);
		if(list.contains("A8.13")){
			String illness_others = request.getParameter("illness_others");
			if(illness_others == null || illness_others.trim().equals("")){
				old.setIllness_others(null);
			}else{
				old.setIllness_others(illness_others);
			}
		}
		list.remove("A8.13");
		old.setIllness(ArToStr(illness));
		list = new ArrayList<String>();
		String[] accident = request.getParameterValues("accident[]");
		Collections.addAll(list, accident);
		if(list.contains("A9.5")){
			String accident_others = request.getParameter("accident_others");
			if(accident_others == null || accident_others.trim().equals("")){
				old.setAccident_others(null);
			}else{
				old.setAccident_others(accident_others);
			}
		}
		list.remove("A8.13");
		old.setAccident(ArToStr(accident));
		return old;
	}


	private String ArToStr(String[] living) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < living.length ; i ++){
			sb.append(living[i] + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
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
	
	
	private Town town;
	private Old  old;
}
