package com.dgut.main.web;


import static com.dgut.main.web.AdminContextInterceptor.PERMISSION_MODEL;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dgut.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

/**
 * 后台管理员权限许可
 * 
 * @author liufang
 * 
 * 实现自定义的freeMark的标签，要实现TemplateDirectiveModel该接口
 */
public class PermistionDirective implements TemplateDirectiveModel {
	/**
	 * 此url必须和perm中url一致。
	 * 
	 * @param env 系统环境变量，通常用它来输出相关内容，如Writer out = env.getOut();
     * @param params 自定义标签传过来的对象，其key=自定义标签的参数名，value值是TemplateModel类型，
     * 而TemplateModel是一个接口类型，通常我们都使用TemplateScalarModel接口来替代它获取一个String 值，
     * 如TemplateScalarModel.getAsString();当然还有其它常用的替代接口，如TemplateNumberModel获取number，TemplateHashModel等
     * @param loopVars 循环替代变量
     * @param body 用于处理自定义标签中的内容;当标签是<@myDirective />格式时，body=null

	 */
	public static final String PARAM_URL = "url";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// 此处的权限判断有可能和拦截器的不一致，有没有关系？大部分应该没有关系，因为不需要判断权限的可以不加这个标签。
		// 光一个perms可能还不够，至少还有一个是否只浏览的问题。这个是否可以不管？可以！
		// 是否控制权限这个总是要的吧？perms为null代表无需控制权限。
		String url = DirectiveUtils.getString(PARAM_URL, params);
		
		boolean pass = false;
		if (StringUtils.isBlank(url)) {
			// url为空，则认为有权限。
			pass = true;
		} else {
			TemplateSequenceModel perms = getPerms(env);
			if (perms == null) {
				// perms为null，则代表不需要判断权限。
				pass = true;
			} else {
				String perm;
				for (int i = 0, len = perms.size(); i < len; i++) {
					perm = ((TemplateScalarModel) perms.get(i)).getAsString();
					if (url.startsWith(perm)) {
						pass = true;
						break;
					}
				}
			}
		}
		if (pass) {
			body.render(env.getOut());
		}
	}

	private TemplateSequenceModel getPerms(Environment env)
			throws TemplateModelException {
		TemplateModel model = env.getDataModel().get(PERMISSION_MODEL);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateSequenceModel) {
			return (TemplateSequenceModel) model;
		} else {
			throw new TemplateModelException(
					"'perms' in data model not a TemplateSequenceModel");
		}

	}
}
