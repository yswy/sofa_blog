package com.zuoer.sofa.blog.base.order;

/**
 * 排序的
 * 
 * @author chenbug
 *
 * @version $Id: Ordered.java, v 0.1 2018年3月15日 上午11:54:16 chenbug Exp $
 */
public interface Ordered {

	/**
	 * 默认排序
	 */
	public static final int DEFAULT_ORDER = 0;

	/**
	 * 最高优先级
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * 最低优先级
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

	public int order();
}
