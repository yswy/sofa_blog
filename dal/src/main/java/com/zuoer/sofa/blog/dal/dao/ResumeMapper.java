package com.zuoer.sofa.blog.dal.dao;

import com.zuoer.sofa.blog.dal.dateobject.ResumeDO;

public interface ResumeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RESUME
     *
     * @mbg.generated
     */
    ResumeDO selectById(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RESUME
     *
     * @mbg.generated
     */
    int updateById(ResumeDO insertDO);
}