package com.zuoer.sofa.blog.dal.mapper;

import com.zuoer.sofa.blog.base.mybatis.CommonMybatisMapper;
import com.zuoer.sofa.blog.dal.entity.UserDO;
import org.apache.ibatis.annotations.Select;

/**
 * @author James
 */
public interface UserMapper extends CommonMybatisMapper<UserDO,Integer> {

	String ALL_COLUMN_STR="id, username, password, birth_date, nickname, email, github, qq, wecaht, avatar,status,user_level, gmt_create, gmt_modified, hobby, motto, md_info";


	/**
	 * 
	 * @param userName
	 * @return
	 */
	@Select({
			"SELECT ",
			ALL_COLUMN_STR,
			" FROM user_base WHERE username = #{userName}"
	})
	public UserDO selectByUserName(String userName);
}