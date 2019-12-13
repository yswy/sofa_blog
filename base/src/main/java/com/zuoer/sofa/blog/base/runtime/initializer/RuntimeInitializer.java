/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.runtime.initializer;

/**
 * 环境初始化
 * 到时候可能需要排序，暂时先不排
 * 
 * @author zuoer
 *
 * @version $Id: RuntimeInitializer.java, v 0.1 2019年12月11日 下午3:37:56 zuoer Exp $
 */
public interface RuntimeInitializer {

	/**
	 * 初始化环境
	 */
	public void initialize();

}
