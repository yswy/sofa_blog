/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.order.comparator;


import com.zuoer.sofa.blog.base.order.Ordered;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Comparator;

/**
 * 
 * @author chenbug
 *
 * @version $Id: OrderedComparator.java, v 0.1 2018年3月13日 下午3:15:56 chenbug Exp $
 */
public class OrderedComparator implements Comparator<Ordered> {

	public static final OrderedComparator INSTANCE = new OrderedComparator();

	@Override
	public int compare(Ordered o1, Ordered o2) {
		// TODO Auto-generated method stub
		return NumberUtils.compare(o1.order(), o2.order());
	}
}
