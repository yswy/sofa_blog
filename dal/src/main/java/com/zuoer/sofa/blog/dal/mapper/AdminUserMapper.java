package com.zuoer.sofa.blog.dal.mapper;

import com.zuoer.sofa.blog.dal.entity.AdminUserDO;

/**
 * @author James
 */
public interface AdminUserMapper extends CommonMybatisMapper<AdminUserDO,Integer> {

	/**
	 * 根据用户名查询
	 * @param userName
	 * @return
	 */
	public AdminUserDO selectByUserName(String userName);
}