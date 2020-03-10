package com.zuoer.sofa.blog.dal.mapper;

import com.zuoer.sofa.blog.base.mybatis.CommonMybatisMapper;
import com.zuoer.sofa.blog.dal.entity.AdminUserDO;
import org.apache.ibatis.annotations.Select;

/**
 * @author James
 */
public interface AdminUserMapper extends CommonMybatisMapper<AdminUserDO,Integer> {
	String ALL_COLUMN_STR="id, username, password, birth_date, nickname, email, github, qq, wecaht, avatar, gmt_create, gmt_modified, hobby, motto, md_info";

	/**
	 * 根据用户名查询
	 * @param userName
	 * @return
	 */
	@Select({
			"SELECT ",
			ALL_COLUMN_STR,
			" FROM admin_user WHERE username = #{userName}"
	})
	public AdminUserDO selectByUserName(String userName);
}