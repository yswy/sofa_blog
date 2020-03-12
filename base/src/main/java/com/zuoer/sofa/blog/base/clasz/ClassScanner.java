package com.zuoer.sofa.blog.base.clasz;

import com.zuoer.sofa.blog.base.utils.ClassNameFilter;

import java.util.Set;

/**
 * 类扫描器
 * 
 * @author chenbug
 *
 * @version $Id: ClassScanner.java, v 0.1 2018年10月12日 上午10:19:08 chenbug Exp $
 */
public class ClassScanner {

	private static ClassScannerInner innerInstance = new ClassScannerInner();

	public static Set<Class<?>> scan(String[] basePackages, ClassNameFilter filter) {
		return innerInstance.scan(basePackages, filter);
	}

	public static Set<Class<?>> scan(String basePackage, ClassNameFilter filter) {
		return innerInstance.scan(basePackage, filter);
	}

	public static void scan(String basePackage, ClassNameFilter filter, SimpleClassVisitor visitor) {
		innerInstance.scan(basePackage, filter, visitor);
	}

	public static void scan(String[] basePackages, ClassNameFilter filter, SimpleClassVisitor visitor) {
		innerInstance.scan(basePackages, filter, visitor);
	}

	public static void scan(String basePackage, SimpleClassVisitor visitor) {
		innerInstance.scan(basePackage, visitor);
	}

	public static void scan(String[] basePackages, SimpleClassVisitor visitor) {
		innerInstance.scan(basePackages, visitor);
	}
}