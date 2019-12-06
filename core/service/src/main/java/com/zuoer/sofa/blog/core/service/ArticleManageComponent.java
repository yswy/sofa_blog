/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service;

import java.util.List;

import com.zuoer.sofa.blog.core.model.request.ArticleCreateRequest;
import com.zuoer.sofa.blog.core.model.request.ArticleModifyRequest;
import com.zuoer.sofa.blog.core.model.result.ArticleOperateResult;

/**
 * 文章管理组件
 * @author zuoer
 *
 * @version $Id: ArticleManageComponent.java, v 0.1 2019年11月28日 下午5:47:02 zuoer Exp $
 */
public interface ArticleManageComponent {
	
	/**
	 * 创建文章
	 * @param request
	 * @return
	 */
	public ArticleOperateResult create(ArticleCreateRequest request);
	
	/**
	 * 修改文章
	 * @param request
	 * @return
	 */
	public ArticleOperateResult modify(ArticleModifyRequest request);
	
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	public ArticleOperateResult deleteByIds(List<Integer> ids);
}
