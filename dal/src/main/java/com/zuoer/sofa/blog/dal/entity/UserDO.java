package com.zuoer.sofa.blog.dal.entity;

import java.util.Date;

/**
 * 普通用户类
 *
 * @author James
 */
public class UserDO {

	/**
	 * PrimaryKey
	 */
	private Integer id;

	/**
	 * 用户名
	 */
	private String username;

	private String password;

	private Date birthDate;

	private String nickname;

	private String email;

	private String github;

	private String qq;

	private String wecaht;

	/**
	 * 头像url
	 */
	private String avatar;

	/**
	 * 状态/0:正常/1:封禁
	 */
	private Integer status;

	/**
	 * 等级
	 */
	private Integer level;

	private Date gmtCreate;

	private Date gmtModified;

	private String hobby;

	private String motto;

	private String mdInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGithub() {
		return github;
	}

	public void setGithub(String github) {
		this.github = github;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWecaht() {
		return wecaht;
	}

	public void setWecaht(String wecaht) {
		this.wecaht = wecaht;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getMdInfo() {
		return mdInfo;
	}

	public void setMdInfo(String mdInfo) {
		this.mdInfo = mdInfo;
	}

}