/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import com.zuoer.sofa.blog.core.service.ResumeManageComponent;
import com.zuoer.sofa.blog.dal.dao.ResumeMapper;
import com.zuoer.sofa.blog.dal.dateobject.ResumeDO;
import com.zuoer.sofa.blog.facade.api.request.ResumeModifyRequest;
import com.zuoer.sofa.blog.facade.api.result.ResumeOperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zuoer
 *
 * @version $Id: ResumeManageComponentImpl.java, v 0.1 2019年12月6日 下午3:14:52 zuoer Exp $
 */
@Component
public class ResumeManageComponentImpl implements ResumeManageComponent {
	
	@Autowired
	private ResumeMapper resumeMapper;

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ResumeManageComponent#modify(com.zuoer.sofa.blog.facade.api.request.ResumeModifyRequest)
	 */
	@Override
	public ResumeOperateResult modify(ResumeModifyRequest request) {
		// TODO Auto-generated method stub
		ResumeOperateResult result=new ResumeOperateResult();
		result.setSuccess(true);
		ResumeDO resumeDO =resumeMapper.selectById(request.getId());
		resumeDO.setHtmlMaterial(request.getHtmlMaterial());
		resumeDO.setIntroduction(request.getIntroduction());
		resumeDO.setMdMaterial(request.getMdMaterial());
		resumeDO.setTitle(request.getTitle());
		resumeMapper.updateById(resumeDO);;
		return result;
	}

}
