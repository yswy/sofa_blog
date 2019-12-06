package com.zuoer.sofa.blog.dal.mapper;

import com.zuoer.sofa.blog.dal.entity.UserDO;

/**
 * @author James
 */
public interface UserMapper extends CommonMybatisMapper<UserDO,Integer> {
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public UserDO selectByUserName(String userName);
}