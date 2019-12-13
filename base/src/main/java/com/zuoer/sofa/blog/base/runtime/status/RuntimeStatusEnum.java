package com.zuoer.sofa.blog.base.runtime.status;


import com.zuoer.sofa.blog.base.enums.EnumBase;

/**
 * 运行状态枚举
 * 
 * @author zuoer
 *
 * @version $Id: RuntimeStatusEnum.java, v 0.1 2019年12月12日 下午5:22:45 zuoer Exp $
 */
public enum RuntimeStatusEnum implements EnumBase {

	UNKNOWN("未知"),

	STARTING("正在启动"),

	STARTED("已启动"),

	SHUTTING_DOWN("正在关闭"),

	SHUTDOWN("已关闭"),

	;

	private String message;

	private RuntimeStatusEnum(String message) {
		this.message = message;
	}

	@Override
	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public Number value() {
		// TODO Auto-generated method stub
		return null;
	}

}
