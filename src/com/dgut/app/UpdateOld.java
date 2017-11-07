package com.dgut.app;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.app.pck.Encrypt;
import com.dgut.app.pck.JSONUtils;
import com.dgut.common.image.ImageScale;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.upload.UploadUtils;
import com.dgut.common.util.DateUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.core.dao.MessageDao;
import com.dgut.main.Constants;
import com.dgut.main.dao.OldDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.AssesTypeMng;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.member.entity.Member;
import com.dgut.member.manager.MemberMng;



@Controller
public class UpdateOld {

	//上传文档
	@RequestMapping("/updateOld.do")
	public void o_upload(HttpServletRequest request,@RequestParam(value = "photo", required = false) MultipartFile photo,
			HttpServletResponse response) throws IOException {


		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		String cid=request.getParameter("cid");
		String username=request.getParameter("username");
		String sex=request.getParameter("gender");
		String diploma=request.getParameter("diploma");
		String age=request.getParameter("birthday");
		String religion=request.getParameter("religion");
		String marriage=request.getParameter("marriage");
		String []living=request.getParameterValues("living[]");
		String []illness=request.getParameterValues("illness[]");
		String income=request.getParameter("income");
		String[] income_ways=request.getParameterValues("incomeways[]");
		String[] pay_ways=request.getParameterValues("payways[]");
		String oafish=request.getParameter("oafish");
		String[] insanity=request.getParameterValues("insanity[]"); 
		String town_id=request.getParameter("town_id");
		String []accident=request.getParameterValues("accident[]");
		
		String photoUrl=null,miniUrl=null;
		if(cid==null){
			jsonMap.put("error", "-3");
			jsonMap.put("mag", "老人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		Integer old_id=Integer.parseInt(cid);
		old=oldMng.findById(old_id);
		if(old==null){
			jsonMap.put("error", "-3");
			jsonMap.put("mag", "老人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;

		}
		
		if(username==null  ||sex==null|| age==null || town_id==null || diploma==null ||
				religion==null|| marriage==null || living==null|| income==null || income_ways==null||
				pay_ways==null|| oafish==null|| insanity==null || illness==null
				){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "信息填写不完整");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		if(!photo.isEmpty()){
			String orignName = photo.getOriginalFilename();
			String photoExt= FilenameUtils.getExtension(orignName).toLowerCase(Locale.ENGLISH);
			if(!"GIF,JPG,JPEG,PNG,BMP".contains(photoExt.toUpperCase())){
				jsonMap.put("error", "-3");
				jsonMap.put("msg", "文件格式有误，请上传图片(gif,jpg,jpeg,png,bmp)文件");
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
				return;

			}
			else{
				photoUrl = fileRepository.storeByExt("/upload/app/file/photo", photoExt, photo);
				ServletContext  context=request.getSession().getServletContext();
				File fi = new File(context.getRealPath(photoUrl)); //大图文件  
				System.out.println(fi);
				
				String miniPath=context.getRealPath("/upload/app/file/photo");
				String miniName=UploadUtils.generateFilename("mini", photoExt);
				File fo = new File(miniPath,miniName); //将要转换出的小图文件
				miniUrl="/upload/app/file/photo/"+miniName;
				try {
					
					imageScale.resizeFix(fi, fo,180,180);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					jsonMap.put("error", "-3");
					jsonMap.put("msg", e.getMessage());
					ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
					return;
				}
				old.setPhotoUrl(photoUrl);
				old.setMiniUrl(miniUrl);
			}

		}
		Town town=townMng.findById(Integer.parseInt(town_id));
		if(town==null){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "城区编号无效");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;

		}

				
		old.setUsername(username);
		old.setTown(town);
		old.setGender(Integer.parseInt(sex));
		try {
			old.setBirthday(DateUtils.format2.parse(age));
		} catch (ParseException e) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "日期解析出错");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		}
		old.setDiploma(diploma);
		
		old.setReligion(Integer.parseInt(religion));
		old.setMarry_status(marriage);
		old.setOafish(oafish);
		old.setIncome(Integer.parseInt(income));
		
		old.setLiving(handleMsg(living));
		
		old=multiSelectMsg(request, old);
		
		old = oldMng.update(old);
		oldMng.updateOldInfo(old.getId());

		cityMng.updateOldList();
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "修改老人信息成功");
		ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));

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
				old.setIncome_others("");
			}
			list.remove("A5.4");	
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
				old.setPay_others("");
			}
			list.remove("A4.8");
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
				old.setInsantity_others("");
			}
			list.remove("A7.8");
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
				old.setIllness_others("");
			}
			list.remove("A8.13");
		}
		old.setIllness(handleMsg(list));

		/*list=new ArrayList<String>();

		String[] accident=request.getParameterValues("accident[]");
		Collections.addAll(list, accident);
		if(list.contains("A9.5")){
			others=request.getParameter("accident_others");
			if(others!=null && !others.trim().equals("")){
				old.setAccident_others(others);
			}
			else{
				old.setAccident_others("");
			}
			list.remove("A9.5");
		}
		old.setAccident(handleMsg(list));
*/		return old;
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


	private String handleMsg(String[] list) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		for(String str:list){
			sb.append(str+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private TownMng townMng;

	@Autowired
	private ImageScale imageScale;

	@Autowired
	private CityMng cityMng;

	@Autowired
	private OldMng oldMng;

	private Old old;


}
