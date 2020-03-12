package com.zuoer.sofa.blog.base.clasz;

/**
 * 类访问
 * 
 * @author chenbug
 *
 * @version $Id: ClassVisitor.java, v 0.1 2017年4月19日 下午10:10:29 chenbug Exp $
 */
public interface SimpleClassVisitor {

	/**
	 * 访问到一个类
	 * 
	 * @param clasz
	 */
	public void visitClass(Class<?> clasz);

}
