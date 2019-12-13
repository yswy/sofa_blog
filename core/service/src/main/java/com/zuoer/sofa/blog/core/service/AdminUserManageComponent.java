/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service;


import com.zuoer.sofa.blog.facade.api.request.AdminUserPasswordModifyRequest;
import com.zuoer.sofa.blog.facade.api.result.AdminUserOperateResult;

import java.util.List;

/**
 * 管理员用户查询组件
 * @author zuoer
 *
 * @version $Id: AdminUserQueryComponent.java, v 0.1 2019年11月28日 下午5:47:02 zuoer Exp $
 */
public interface AdminUserManageComponent {

	/**
	 * 根据id集合删除
	 * @param ids
	 * @return
	 */
	public AdminUserOperateResult deleteByIds(List<Integer> ids);
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	public AdminUserOperateResult modifyPassword(AdminUserPasswordModifyRequest request);
}
