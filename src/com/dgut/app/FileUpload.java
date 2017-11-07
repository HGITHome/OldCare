package com.dgut.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.Constants;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;
import com.dgut.main.entity.Summary;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AssesCategoryMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.AssesTypeMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.member.entity.Member;
import com.dgut.member.manager.MemberMng;



@Controller
public class FileUpload {

	//上传文档
	@RequestMapping("/upload.do")
	public void o_upload(String cid, String uid,Date record_date,
			@RequestParam(value = "photoFile", required = false) MultipartFile[] photoFile,
			@RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
			@RequestParam(value = "videoMiniFile", required = false) MultipartFile videoMiniFile,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		
				
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		Set<RecordPicture> pSet=new HashSet<RecordPicture>();
		String[] photoUrl=null;
		String videoUrl=null,miniUrl=null,video_mini=null;
		Integer i=0;
		RecordPicture picture=null;
		record=new Record();
		String ctx = request.getContextPath();
		if(photoFile!=null){


			photoUrl=new String[photoFile.length];
			for(MultipartFile file:photoFile){
				miniUrl=null;
				if(!file.isEmpty()){
					String photoOrigName = file.getOriginalFilename();
					String photoExt = FilenameUtils.getExtension(photoOrigName).toLowerCase(
							Locale.ENGLISH);

					if (!"GIF,JPG,JPEG,PNG,BMP".contains(photoExt.toUpperCase())) {
						jsonMap.put("error", -1);
						jsonMap.put("msg", "文件格式有误，请上传图片(gif,jpg,jpeg,png,bmp)文件");
						ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
						return;
					}
					else{
						picture=new RecordPicture();

						photoUrl[i] = fileRepository.storeByExt("/upload/app/file/photo", photoExt, file);
						ServletContext  context=request.getSession().getServletContext();
						File fi = new File(context.getRealPath(photoUrl[i])); //大图文件  
						System.out.println(fi);
						
						String miniPath=context.getRealPath("/upload/app/file/photo");
						String miniName=UploadUtils.generateFilename("", photoExt);
						File fo = new File(miniPath,miniName); //将要转换出的小图文件
						miniUrl="/upload/app/file/photo"+miniName;
						try {
							
							imageScale.resizeFix(fi, fo,180,180);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							jsonMap.put("error", "-3");
							jsonMap.put("msg", e.getMessage());
							ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
							return;
						}
						
						picture.setImgPath(photoUrl[i]);
						picture.setMiniRecordUrl(miniUrl);
						picture.setRecord(record);
						pSet.add(picture);
						i++;
					}
				}

			}
		}
		if(videoFile!=null && !videoFile.isEmpty()){
			String videoOrigName = videoFile.getOriginalFilename();
			String videoExt= FilenameUtils.getExtension(videoOrigName).toLowerCase(Locale.ENGLISH);
			if(!"MOV,AVI,RMVB,WMV,MP4".contains(videoExt.toUpperCase())){
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "视频格式有误,请上传(mov,avi,rmvb,wmv,mp4)视频文件");
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			}
			else{
				videoUrl = fileRepository.storeByExt("/upload/app/file/video", videoExt, videoFile);
				String photoOrigName = videoMiniFile.getOriginalFilename();
				String photoExt = FilenameUtils.getExtension(photoOrigName).toLowerCase(
						Locale.ENGLISH);

				if (!"GIF,JPG,JPEG,PNG,BMP".contains(photoExt.toUpperCase())) {
					jsonMap.put("error", -1);
					jsonMap.put("msg", "视频缩略图文件格式有误，请上传图片(gif,jpg,jpeg,png,bmp)文件");
					ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
					return;
				}
				else{
					video_mini = fileRepository.storeByExt("/upload/app/file/video/photo", photoExt, videoMiniFile);
					

				}

			}
		}
		if (StringUtils.isBlank(cid)) {
			jsonMap.put("error", -2);
			jsonMap.put("msg", "老人编号不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		Integer old_id = Integer.parseInt(cid);
		Old old = oldMng.findById(old_id);
		if (old == null) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "老人不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		if (StringUtils.isBlank(uid)) {
			jsonMap.put("error", "-2");
			jsonMap.put("msg", "登记人员不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		uid = Encrypt.decrypt3DES(uid, Constants.ENCRYPTION_KEY);
		Integer userId = Integer.parseInt(uid);
		Admin admin = adminMng.findById(userId);
		if (admin == null) {
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "登记人员不存在");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}



		
		record.setOld(old);
		record.setRecord_time(record_date);
		//record.setPhotoUrl(photoUrl);
		record.setPhotos(pSet);
		record.setVideo_url(videoUrl);
		record.setEnroller(admin);
		record.setVideo_mini(video_mini);
		Set<AssesItem> set = getItems(record, request);
		record.setScores(set);
		Set<Summary> scoreSet=getScore(record,request);
		if(scoreSet==null){
			jsonMap.put("error", "-3");
			jsonMap.put("msg", "统计数量出错");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;

		}
		record.setSummary(scoreSet);
		recordMng.save(record);
		jsonMap.put("error", "-1");
		jsonMap.put("msg", "操作成功");
		jsonMap.put("photoUrl", photoUrl);
		jsonMap.put("videoUrl", videoUrl);
		ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
	}


	private Set<Summary> getScore(Record record2, HttpServletRequest request) {
		Set<Summary> scores=new HashSet<Summary>();
		AssesCategory category=null;
		Summary summary=null;
		String[] categorySet=request.getParameterValues("category_set");
		String[] totalGrade=request.getParameterValues("scores");
		if(categorySet==null || totalGrade==null ||(categorySet.length!=totalGrade.length)){
			return null;
		}
		for(int i=0;i<categorySet.length;i++){
			category=categoryMng.findById(Integer.parseInt(categorySet[i]));
			summary=new Summary();
			summary.setCategory(category);
			summary.setGrade(Integer.parseInt(totalGrade[i]));
			scores.add(summary);
			
		}
		return scores;
	}


	private Set<AssesItem> getItems(Record record, HttpServletRequest request) {
		
		Set<AssesItem> set=new HashSet<AssesItem>();
		AssesItem item=null;

		


		Integer i=0;
		String[] items=request.getParameterValues("items");
		for(String key:items){
			
			
			item=itemMng.findById(Integer.parseInt(key));
			//item=itemMng.findById(Integer.parseInt(request.getParameter(key)));
			item.setRecord(record);
			set.add(item);

			/*if(typeIdList.contains(resultStr)){
				item=itemMng.findById(Integer.parseInt(request.getParameter(key)));
				item.setRecord(record);
				set.add(item);
				i++;
			}*/
		}
		return set;
	}

	@Autowired
	private ImageScale imageScale;
	
	@Autowired
	private AssesItemMng itemMng;

	@Autowired
	private AssesCategoryMng categoryMng;

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private RecordMng recordMng;

	@Autowired
	private OldMng oldMng;
	@Autowired
	private AdminMng adminMng;

	private Record record;
}
