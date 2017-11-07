package com.dgut.MyApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.app.pck.Encrypt;
import com.dgut.app.pck.JSONUtils;
import com.dgut.common.image.ImageScale;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.upload.UploadUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.Constants;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;

@Controller
public class MyAddRecord {
	private static final Logger log = LoggerFactory.getLogger(MyAddRecord.class);
	
	@RequestMapping(value="/addRecord.do",method=RequestMethod.POST)
	public void addRecord(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="photo", required=false) MultipartFile photo,
			@RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
			String cid,String uid,Date record_date) throws IOException{
		
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(cid)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "老人编号不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		if(StringUtils.isBlank(uid)){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "记录人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		if(record_date == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "记录时间不能为空");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return ;
		}
		if(photo == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "相片不能空");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return ;
		}
		if(videoFile == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "视频不能为空");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return ;
		}
		
		old = oldMng.findById(Integer.parseInt(cid));
		if(old == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "老人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return ;

		}
		
		uid = Encrypt.decrypt3DES(uid, Constants.ENCRYPTION_KEY);
		admin = adminMng.findById(Integer.parseInt(uid));
		if(admin ==  null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "记录人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		
		
		String photoUrl = "";
		String miniPhotoUrl = "";
		record = new Record();
		picture = new RecordPicture();
		Set<RecordPicture> photoSet = new HashSet<RecordPicture>();
		if(photo != null){
			String photoOriName = photo.getOriginalFilename();
			String extName = FilenameUtils.getExtension(photoOriName).toLowerCase(Locale.ENGLISH);
		    List<String> extNameList  = new ArrayList<String>(Arrays.asList("jpg", "img", "bmp","jpeg","png","gif"));
		    if(!extNameList.contains(extName.toLowerCase(Locale.ENGLISH))){
		    	jsonMap.put("error", "-3");
		    	jsonMap.put("message", "上传照片格式有误，请上传后缀名为（jpg, img, bmp,jpeg,png,gif）的照片");
		    	ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		    	return ;
		    }
			try {
				photoUrl = fileRepository.storeByExt("/upload/app/file/photo", extName, photo);
				ServletContext  context=request.getSession().getServletContext();
				File fi = new File(context.getRealPath(photoUrl)); //大图文件  

				String miniPath=context.getRealPath("/upload/app/file/photo");
				String miniName=UploadUtils.generateFilename("mini", extName);
				File fo = new File(miniPath,miniName); //将要转换出的小图文件
				miniPhotoUrl="/upload/app/file/photo"+miniName;
				imageScale.resizeFix(fi, fo,180,180);
			} catch (Exception e) {
				log.error("上传照片时:" + e.getMessage());
				jsonMap.put("error", "-3");
				jsonMap.put("message", "上传老人相片失败");
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			    return ;
			}
			picture.setImgPath(photoUrl);
			picture.setMiniRecordUrl(miniPhotoUrl);
			picture.setRecord(record);
			photoSet.add(picture);
		}
		
		String videoUrl = "";
		if(!videoFile.isEmpty() && videoFile != null){
			String videoOrigName = videoFile.getOriginalFilename();
			String videoExtName = FilenameUtils.getExtension(videoOrigName).toLowerCase(Locale.ENGLISH);
			List<String> extNameList  = new ArrayList<String>(Arrays.asList("MOV","AVI","RMVB","WMV","MP4"));
			if(!extNameList.contains(videoExtName.toUpperCase(Locale.ENGLISH))){
				jsonMap.put("error", "-3");
				jsonMap.put("message", "上传视频后缀名有误，请上传后缀名为（MOV,AVI,RMVB,WMV,MP4）的视频");
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		     }else{
			     videoUrl = fileRepository.storeByExt("/upload/myapp/file/video",videoExtName , videoFile);
		     }
		}
		record.setEnroller(admin);
		record.setOld(old);
		record.setPhotos(photoSet);
		record.setVideo_url(videoUrl);
		record.setRecord_time(record_date);
		Set<AssesItem> scores = getItemSet(record,request);
		if(scores == null){
			jsonMap.put("error", "-3");
			jsonMap.put("message", "传过参数失败");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		record.setScores(scores);
		recordMng.save(record);
		jsonMap.put("error", "0");
		jsonMap.put("message", "添加记录成功");
		ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		return;
	}
	
	
	
	private Set<AssesItem> getItemSet(Record record2, HttpServletRequest request) {
		Set<AssesItem> itemSet = new HashSet<AssesItem>(); 
		String itemIdsStr = request.getParameter("items");
		String[] itemIds = itemIdsStr.split(",");
		AssesItem  item = null;
		if(itemIds == null){
			return null;
		}
		for(String id : itemIds){
			item = itemMng.findById(Integer.parseInt(id));
			if(item != null){
				item.setRecord(record);
				itemSet.add(item);
			}
		}
		return itemSet;
	}



	@Autowired
	private ImageScale imageScale;
	
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private AssesItemMng itemMng;
	
	@Autowired
	private OldMng oldMng;
	
	@Autowired
	private AdminMng adminMng;
	
	@Autowired
	private RecordMng recordMng;
	
	private Admin admin;
	
	private RecordPicture picture;
	
	private Old old;
	
	private Record record;
}
