/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import com.zuoer.sofa.blog.base.utils.ConverterUtils;
import com.zuoer.sofa.blog.core.model.enums.ErrorCodeEnum;
import com.zuoer.sofa.blog.core.service.UserManageComponent;
import com.zuoer.sofa.blog.dal.dao.UserBaseMapper;
import com.zuoer.sofa.blog.dal.dateobject.UserBaseDO;
import com.zuoer.sofa.blog.facade.api.request.UserRegisterRequest;
import com.zuoer.sofa.blog.facade.api.result.UserOperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zuoer
 *
 * @version $Id: UserManageComponentImpl.java, v 0.1 2019年12月6日 下午3:20:43 zuoer Exp $
 */
@Component
public class UserManageComponentImpl implements UserManageComponent {

	@Autowired
	private UserBaseMapper userBaseMapper;
	
	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.UserManageComponent#register(com.zuoer.sofa.blog.facade.api.request.UserRegisterRequest)
	 */
	@Override
	public UserOperateResult register(UserRegisterRequest request) {
		// TODO Auto-generated method stub
		UserOperateResult result=new UserOperateResult();
		result.setSuccess(true);
		UserBaseDO exsitUser= userBaseMapper.selectByUserName(request.getUsername());
		if(exsitUser!=null) {
			result.setSuccess(false);
    		result.setError(ErrorCodeEnum.USER_NAME_EXSITED.errorCode());
    		return result;
		}
		UserBaseDO userDO=new UserBaseDO();
		ConverterUtils.convert(request, userDO);
		userBaseMapper.insert(userDO);
		return result;
	}

}
