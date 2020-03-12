package com.zuoer.sofa.blog.base.clasz;


import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.utils.ClassNameFilter;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 类扫描器内部类
 * 
 * @author chenbug
 *
 * @version $Id: ClassScClassScannerInneranner.java, v 0.1 2018年10月12日 上午10:19:08 chenbug Exp $
 */
public class ClassScannerInner {

	protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	protected MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	public Set<Class<?>> scan(String[] basePackages, ClassNameFilter filter) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String s : basePackages)
			classes.addAll(scan(s, filter));
		return classes;
	}

	public Set<Class<?>> scan(String basePackage, ClassNameFilter filter) {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		scan(basePackage, filter, new SimpleClassVisitor() {
			@Override
			public void visitClass(Class<?> clasz) {
				// TODO Auto-generated method stub
				classes.add(clasz);
			}

		});
		return classes;
	}

	/**
	 * @param basePackages
	 * @param filter
	 * @param visitor
	 */
	public void scan(String[] basePackages, ClassNameFilter filter, SimpleClassVisitor visitor) {
		for (String basePackage : basePackages) {
			scan(basePackage, filter, visitor);
		}
	}

	/**
	 * @param basePackages
	 * @param visitor
	 */
	public void scan(String[] basePackages, SimpleClassVisitor visitor) {
		for (String basePackage : basePackages) {
			scan(basePackage, visitor);
		}
	}

	public void scan(String basePackage, SimpleClassVisitor visitor) {
		scan(basePackage, null, visitor);
	}



	/**
	 * @param basePackage
	 * @param filter
	 * @param visitor
	 */
	public void scan(String basePackage, ClassNameFilter filter, SimpleClassVisitor visitor) {
		String scanPackage = basePackage;
		// 去掉最后一个.
		if (StringUtils.endsWith(basePackage, StringUtils.DOT_SIGN)) {
			scanPackage = basePackage.substring(0, basePackage.length() - 1);
		}
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPackage)) + "/**/*.class";
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					if (metadataReader.getClassMetadata() != null && (filter == null || filter.accept(metadataReader.getClassMetadata().getClassName()))) {
						try {
							visitor.visitClass(Class.forName(metadataReader.getClassMetadata().getClassName()));
						} catch (Exception e) {
							// 忽略异常
						} catch (NoClassDefFoundError e) {
							throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "载入类异常,className=" + metadataReader.getClassMetadata().getClassName(), e);
						}

					}
				}
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
	}
}