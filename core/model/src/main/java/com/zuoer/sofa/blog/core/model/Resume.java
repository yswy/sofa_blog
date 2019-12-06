package com.zuoer.sofa.blog.core.model;

import java.util.Date;

/**
 * 简历类
 *
 * @author James
 */
public class Resume {

  /**
   * 主键id
   */
  private Integer id;

  /**
   * 标题
   */
  private String title;

  /**
   * 创建时间
   */
  private Date gmtCreate;

  /**
   * 修改时间
   */
  private Date gmtModified;

  /**
   * 简介
   */
  private String introduction;

  /**
   * 文章内容
   */
  private String mdMaterial;

  /**
   * html 文章内容
   */
  private String htmlMaterial;

  /**
   * 获取PrimaryKey
   *
   * @return id - PrimaryKey
   */
  public Integer getId() {
    return id;
  }

  /**
   * 设置PrimaryKey
   *
   * @param id PrimaryKey
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title == null ? null : title.trim();
  }

  /**
   * @return gmt_create
   */
  public Date getGmtCreate() {
    return gmtCreate;
  }

  /**
   * @param gmtCreate
   */
  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  /**
   * @return gmt_modified
   */
  public Date getGmtModified() {
    return gmtModified;
  }

  /**
   * @param gmtModified
   */
  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  /**
   * @return introduction
   */
  public String getIntroduction() {
    return introduction;
  }

  /**
   * @param introduction
   */
  public void setIntroduction(String introduction) {
    this.introduction = introduction == null ? null : introduction.trim();
  }

  /**
   * 获取文章内容
   *
   * @return md_material - 文章内容
   */
  public String getMdMaterial() {
    return mdMaterial;
  }

  /**
   * 设置文章内容
   *
   * @param mdMaterial 文章内容
   */
  public void setMdMaterial(String mdMaterial) {
    this.mdMaterial = mdMaterial == null ? null : mdMaterial.trim();
  }

  public String getHtmlMaterial() {
    return htmlMaterial;
  }

  public void setHtmlMaterial(String htmlMaterial) {
    this.htmlMaterial = htmlMaterial;
  }
}