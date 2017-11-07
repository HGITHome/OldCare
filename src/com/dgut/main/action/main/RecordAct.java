package com.dgut.main.action.main;

import static com.dgut.common.page.SimplePage.cpn;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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

import org.apache.commons.collections.CollectionUtils;
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
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.common.web.springmvc.RealPathResolver;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Authentication;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;

import com.dgut.main.manager.main.AssesCategoryMng;

import com.dgut.main.manager.main.AdminLogMng;
import com.dgut.main.manager.main.AdminMng;
import com.dgut.main.manager.main.AssesItemMng;
import com.dgut.main.manager.main.AssesTypeMng;
import com.dgut.main.manager.main.AuthenticationMng;
import com.dgut.main.manager.main.CityMng;
import com.dgut.main.manager.main.FirstTypeMng;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.TownMng;
import com.dgut.main.web.CmsUtils;
import com.dgut.main.web.WebErrors;


@Controller
public class RecordAct {

	@RequestMapping("/record/v_list.do")
	public String list(Integer pageNo,String enrollerName,Date record_time,String old_name,Integer town_id,
			HttpServletRequest request, ModelMap model) {
		Pagination pagination = recordMng.getList(enrollerName,town_id,old_name,record_time,cpn(pageNo),  CookieUtils.getPageSize(request));
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		model.addAttribute("old_name", old_name);
		model.addAttribute("enrollerName",enrollerName);
		model.addAttribute("record_time", record_time);
		model.addAttribute("townList",cityMng.findById(1).getTowns());
		if(town_id!=null){
			model.addAttribute("town_id",town_id);
		}
		else{
			model.addAttribute("town_id","");
		}
		return "record/list";
	}
	@RequestMapping("/record/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		List<Old> list = (List<Old>) oldMng.getList(null,null,1,Integer.MAX_VALUE).getList();
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("list", list);
		model.addAttribute("cList", cList);
		return "record/add";
	}
	
	@RequestMapping("/record/upload.do")
	public String upload(HttpServletRequest request, ModelMap model) {
		return "record/uoloadXLS";
	}

	@RequestMapping("/record/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			//return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);		}
		    record=recordMng.findById(id);

		model.addAttribute("record",record);
		
		List<Old> list = (List<Old>) oldMng.getList(null,null,1,Integer.MAX_VALUE).getList();
		List<AssesCategory> cList=categoryMng.getList();
		model.addAttribute("list", list);
		model.addAttribute("cList", cList);
	
		return "record/edit";
	}
	
	@RequestMapping("/record/v_delete.do")
	public String delete(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			//return errors.showErrorPage(model);
			model.addAttribute("errors", errors.getErrors());
			return "redirect:v_list.do";
		}
		record=recordMng.deleteById(id);
		
		adminLogMng.operating(request, "cms.record.delete", "id=" + record.getId()
				+ ";username=" + record.getOld().getUsername());
		return "redirect:v_list.do";
	}

	@RequestMapping("/record/o_update.do")
	public String update(
			@RequestParam(value = "photoFile", required = false) MultipartFile[] photoFile, 
			@RequestParam(value = "videoFile", required = false)MultipartFile videoFile,
			Record record,Integer oldId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpload(oldId,photoFile, videoFile,record.getRecord_time(),request);
		
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		
		String ctx = request.getContextPath();
		String auth_id=(String) session.getAttribute(request, AuthenticationMng.AUTH_KEY);
		Authentication auth=authMng.findById(auth_id);
		Admin admin=adminMng.findById(auth.getUid());
		String miniUrl=null;
		record.setEnroller(admin);
		record.setScores(null);
		record.setPhotos(null);

		Old old=oldMng.findById(oldId);
		if(old==null){
			 errors.addErrorString("老人不存在");
				return errors.showErrorPage(model);
		 }
		record.setOld(old);
		
		Set<AssesItem> set = getItems(record, request);
		Set<RecordPicture> pSet=new HashSet<RecordPicture>();
		record.setScores(set);
		String videoUrl=null;
		try {
			if(photoFile!=null){
				String[] photoUrl=new String[photoFile.length];
				Integer i=0;
				RecordPicture picture=null;
				for(MultipartFile file:photoFile){
					miniUrl=null;
					if(!file.isEmpty()){
						picture=new RecordPicture();
						photoUrl[i]=getFileUrl(file,request,model);
						String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase(
								Locale.ENGLISH);

						ServletContext  context=request.getSession().getServletContext();
						File fi = new File(context.getRealPath(photoUrl[i])); //大图文件  
						System.out.println(fi);
						
						String miniPath=context.getRealPath("/upload/file");
						String miniName=UploadUtils.generateFilename("mini", ext);
						File fo = new File(miniPath,miniName); //将要转换出的小图文件
						miniUrl="/upload/file/"+miniName;
					
						try {
							imageScale.resizeFix(fi, fo,180,180);
						} catch (Exception e) {
							errors.addErrorString("上传相片失败");
							return errors.showErrorPage(model);
						}
						
						picture.setImgPath(photoUrl[i]);
						picture.setMiniRecordUrl(miniUrl);
						picture.setRecord(record);
						pSet.add(picture);
						i++;
					}
				}
				
				record.setPhotos(pSet);
			}
			if(!videoFile.isEmpty()){
				videoUrl=getFileUrl(videoFile,request,model);
				record.setVideo_url(videoUrl);
			}
		} catch (IOException e) {
			errors.addErrorString(e.getMessage());
			return errors.showErrorPage(model);
		}
		record=recordMng.update(record);
		adminLogMng.operating(request, "cms.record.update", "id=" + record.getId()
				+ ";username=" + record.getOld().getUsername());	
	
		//return info(id,request, model);
		return "redirect:v_list.do";
	}
	
	@RequestMapping("/record/o_info.do")
	public String info(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);

		}
		
		 record=recordMng.findById(id);
		 if(record==null){
			 errors.addErrorString("记录不存在");
				return errors.showErrorPage(model);
		 }
		model.addAttribute("record",record);
		
		model.addAttribute("map", record.getMap());
		
		return "record/show";
	}
	@RequestMapping("/record/o_linechart.do")
	public String linechart(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEditOrDelete(id, request);
		if (errors.hasErrors()) {
			
			model.addAttribute("errors", errors.getErrors());
			return errors.showErrorPage(model);

		}

		 record=recordMng.findById(id);
		 if(record==null){
			 errors.addErrorString("记录不存在");
				return errors.showErrorPage(model);
		 }
		model.addAttribute("record",record);
		
		model.addAttribute("map", record.getMap());
		
		return "record/LineChartShow";
	}
	
	
	private final String []photoMime=new String[]{"image/bmp","image/png","image/gif","image/jpeg"};
	private final String []videoMime=new String[]{"video/x-msvideo","application/vnd.rn-realmedia-vbr","video/x-ms-wmv","video/mp4"};
	private final String [] photoSuffix=new String[]{"bmp","gif","jpg","jpeg","png"};
	private final String [] videoSuffix=new String[]{"avi","rmvb","wmv","mp4"};
	
	@RequestMapping("/record/o_upload.do")
	public String saveRecord(Record record,String[] type,Integer oldId,
			@RequestParam(value = "photoFile", required = false) MultipartFile[] photoFile, @RequestParam(value = "videoFile", required = false)MultipartFile videoFile,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpload(oldId,photoFile, videoFile,record.getRecord_time(),request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		
		/*String auth_id=(String) session.getAttribute(request, AuthenticationMng.AUTH_KEY);
		Authentication auth=authMng.findById(auth_id);
		
		Admin admin=adminMng.findById(auth.getUid());*/
		record.setEnroller(CmsUtils.getAdmin(request));
		
		Old old=oldMng.findById(oldId);
		record.setOld(old);
		String ctx = request.getContextPath();
		
		Set<AssesItem> set = getItems(record, request);
		Set<RecordPicture> pSet=new HashSet<RecordPicture>();
		record.setScores(set);
		String videoUrl=null,miniUrl=null;
		try {
			if(photoFile!=null){
				String[] photoUrl=new String[photoFile.length];
				Integer i=0;
				RecordPicture picture=null;
				for(MultipartFile file:photoFile){
					miniUrl=null;
					if(!file.isEmpty()){
						picture=new RecordPicture();
						
						photoUrl[i]=getFileUrl(file,request,model);
						
						String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase(
								Locale.ENGLISH);
						ServletContext  context=request.getSession().getServletContext();
						File fi = new File(context.getRealPath(photoUrl[i])); //大图文件  
						String miniPath=context.getRealPath("/upload/file");
						String miniName=UploadUtils.generateFilename("mini", ext);
						File fo = new File(miniPath,miniName); //将要转换出的小图文件
						miniUrl="/upload/file/"+miniName;
						try {
							imageScale.resizeFix(fi, fo,180,180);//图片缩小
						} catch (Exception e) {
							errors.addErrorString("上传相片失败");
							return errors.showErrorPage(model);
						}
						picture.setImgPath(photoUrl[i]);
						picture.setRecord(record);
						pSet.add(picture);
						i++;
					}
				}
				record.setPhotos(pSet);
			}
			if(!videoFile.isEmpty()){
				videoUrl=getFileUrl(videoFile,request,model);
				record.setVideo_url(videoUrl);
			}
		} catch (IOException e) {
			errors.addErrorString(e.getMessage());
			return errors.showErrorPage(model);
		}
		record=recordMng.save(record);
		adminLogMng.operating(request, "cms.record.add", "id=" + record.getId()
				+ ";username=" + record.getOld().getUsername());
		/*try {
			photoUrl = getFileUrl(photoFile,request,model);
			record.setPhotoUrl(ctx+photoUrl);
			if(videoFile!=null){
				videoUrl=getFileUrl(videoFile,request,model);
				//record=recordMng.saveFileByPath(ctx + photoUrl,ctx + videoUrl,record);
				record.setVideo_url(ctx+videoUrl);
			}
			// 加上部署路径
			else{
				record=recordMng.saveFileByPath(ctx + photoUrl,null,record);
			}
			
			recordMng.save(record);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errors.addErrorString(e.getMessage());
			return errors.showErrorPage(model);
		
		} */
			
		return "redirect:v_list.do";

		
	}

	private Set<AssesItem> getItems(Record record, HttpServletRequest request) {
		List<AssesType> types=typeMng.getList();
		List<String> typeIdList=new ArrayList<String>();
		for(AssesType type1:types){
			typeIdList.add(type1.getId().toString());
		}
		Set<AssesItem> set=new HashSet<AssesItem>();
		AssesItem item=null;
		
		Set<String> keys = request.getParameterMap().keySet();
		String resultStr=null;
		
		
		Integer i=0;
		for(String key:keys){
			resultStr=key;
			item=new AssesItem();
			if(key.startsWith("a")){
				resultStr=key.substring(1, key.length());
			}
			
			if(typeIdList.contains(resultStr)){
				item=itemMng.findById(Integer.parseInt(request.getParameter(key)));
				item.setRecord(record);
				set.add(item);
				i++;
			}
		}
		return set;
	}
	
	

	private String getFileUrl(@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request, ModelMap model) throws IOException{
		
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		String fileUrl = null;
		
		fileUrl = fileRepository.storeByExt("/upload/file", ext, file);

		return fileUrl;
	}
	
	private boolean isExist(String ext, String[] type2) {
		for(String type:type2){
			if(ext.equals(type)){
				return true;
			}
		}
		return false;
	}

	private WebErrors validateUpload(Integer cid,MultipartFile[] photoFile,MultipartFile videroFile,Date record_date,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (cid==null) {
			errors.addErrorString("老人编号不存在");
			
		}
		else{
			Old old =  manager.findById(cid);
			if (old==null) {
				errors.addErrorString("老人不存在");
				return errors;
			}
		}
		if(record_date==null){
			errors.addErrorString("记录时间未选择");
			return errors;
		}
				
		
		
		for(MultipartFile file:photoFile){
			if(!file.isEmpty()){
				
				errors=validPhoto(file, errors);
				return errors;
			}
		}
		
		if(!videroFile.isEmpty()){
			errors=validVideo(videroFile, errors);
		}
		return errors;
	}

	private WebErrors validPhoto(MultipartFile photoFile, WebErrors errors) {
		String contentType=photoFile.getContentType();
		String origName = photoFile.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		if(!isExist(contentType,photoMime)){
			errors.addErrorString("图片记录有误");
		}
		
		else{
			if(!isExist(ext,photoSuffix)){
				errors.addErrorString("请上传bmp,png,jpg,jpeg,gif格式的图片");		
			}
		}
		return errors;
	}

	private WebErrors validVideo(MultipartFile videroFile, WebErrors errors) {
		String contentType=videroFile.getContentType();
		String origName = videroFile.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		if(!isExist(contentType,videoMime)){
			errors.addErrorString("视频格式记录有误");
		}
		else{
			if(!isExist(ext,videoSuffix)){
				errors.addErrorString("请上传avi.rmvb,wmv格式的视频");		
			}
		}
		return errors;
		
	}

		
	
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private OldMng manager;
	
	
	
	

	
	
	
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
		Record entity = recordMng.findById(id);
		if (errors.ifNotExist(entity, Record.class, id)) {
			return true;
		}
		return false;
	}

	private WebErrors validateSave(Record bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		/*if (bean..length()<3) {
			errors.addErrorString("dayu3");
		}
		else if(bean.getTypeName().trim().equals("")){
			errors.addErrorString("类名不能为空白符");
		}
*/		return errors;
	}

	@Autowired
	private ImageScale imageScale;
	
	@Autowired
	private RecordMng recordMng;
	
	@Autowired
	private AssesCategoryMng categoryMng;
	
	@Autowired
	private OldMng oldMng;
	
	@Autowired
	private AssesTypeMng typeMng;
	
	@Autowired
	private AssesItemMng itemMng;
	
	@Autowired
	private AdminLogMng adminLogMng;
	
	@Autowired
	private AuthenticationMng authMng;
	
	@Autowired
	private SessionProvider session;
	
	@Autowired
	private AdminMng adminMng;
	
	@Autowired
	private CityMng cityMng;
	
	
	private Record record;
	
	
	
	
}
