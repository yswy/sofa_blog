//package com.zuoer.sofa.blog.base;
//
//import com.bench.common.enums.CommonErrorCodeEnum;
//import com.bench.common.lang.ArrayUtils;
//import com.bench.common.lang.StringUtils;
//import com.bench.common.lang.exception.BaseRuntimeException;
//import com.bench.common.lang.nill.Null;
//import com.bench.common.trace.BenchTracerHolder;
//import com.bench.common.trace.enums.TraceEntryTypeEnum;
//import com.bench.platform.base.webservice.hello.HelloFacade;
//import com.bench.platform.common.webservice.count.server.WebServiceServerMethodCallCountComponent;
//import com.bench.platform.common.webservice.cxf.CXFServiceExport;
//import com.bench.platform.common.webservice.cxf.autoconfig.CXFServiceConfig;
//import com.bench.platform.common.webservice.cxf.interceptor.WebServiceCallInfoGetInterceptor;
//import com.bench.platform.webservice.utils.WebServiceUtils;
//import com.bench.runtime.core.BenchRuntime;
//import com.bench.runtime.core.configuration.remote.webservice.WebServiceServerConfiguration;
//import com.bench.runtime.core.lifecycle.BenchLifeCycle;
//import com.bench.runtime.core.webservice.annotation.RestServer;
//import org.aopalliance.intercept.MethodInterceptor;
//import org.aopalliance.intercept.MethodInvocation;
//import org.apache.cxf.Bus;
//import org.apache.cxf.aegis.databinding.AegisDatabinding;
//import org.apache.cxf.feature.FastInfosetFeature;
//import org.apache.cxf.frontend.spring.ServerFactoryBeanDefinitionParser.SpringServerFactoryBean;
//import org.apache.cxf.interceptor.Interceptor;
//import org.apache.cxf.message.Message;
//import org.apache.cxf.transport.common.gzip.GZIPFeature;
//import org.apache.log4j.Logger;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * CXF服务导出
// *
// * @author chenbug
// *
// * @version $Id: CXFServiceExportImpl.java, v 0.1 2015年11月12日 下午6:19:50 Administrator Exp $
// */
//@Service
//public class CXFServiceExportImpl implements CXFServiceExport, ApplicationContextAware, BenchLifeCycle {
//
//	private static final Logger log = Logger.getLogger(CXFServiceExportImpl.class);
//
//	private ApplicationContext applicationContext;
//
//	@Autowired
//	private Bus cxf;
//
//	/** 待输出服务的描述 */
//	private List<CXFServiceConfig> cxfServiceConfigs = new ArrayList<CXFServiceConfig>();
//
//	/** 已经输出服务的bean **/
//	private List<SpringServerFactoryBean> serviceFactoryBeans = new ArrayList<SpringServerFactoryBean>();
//
//	@Autowired
//	private WebServiceServerMethodCallCountComponent webServiceServerMethodCallCountComponent;
//
//	// 设置服务调用信息
//	private WebServiceCallInfoGetInterceptor webServiceCallInfoGetInterceptor = new WebServiceCallInfoGetInterceptor();
//
//	/**
//	 * 暴露成webservice的类集合
//	 */
//	private List<Class<?>> webserviceClassList = new ArrayList<Class<?>>();
//
//	/**
//	 * 服务端调用统计interceptor
//	 */
//
//	protected void publishService() {
//		// TODO Auto-generated method stub
//
//		// 查找所有基于注解的服务发布
//		Map<String, Object> servieBeanMap = BenchRuntime.getInstance().getContext().getBeansWithAnnotation(RestServer.class);
//		for (Map.Entry<String, Object> entry : servieBeanMap.entrySet()) {
//			RestServer webserviceServerAnno = entry.getValue().getClass().getAnnotation(RestServer.class);
//			CXFServiceConfig serviceExport = new CXFServiceConfig();
//
//			// 获取重载类
//			Set<Class<?>> overrideTypesSet = WebServiceUtils.getServerOverrideTypes(entry.getValue().getClass());
//			if (overrideTypesSet.size() > 0) {
//				serviceExport.setOverrideTypes(new ArrayList<String>());
//				for (Class<?> overrideType : overrideTypesSet) {
//					serviceExport.getOverrideTypes().add(overrideType.getName());
//				}
//			}
//
//			Class<?> serviceInterface = webserviceServerAnno.serviceInterface();
//			if (serviceInterface == Null.class) {
//				if (entry.getValue().getClass().getInterfaces().length > 0) {
//					if (entry.getValue().getClass().getInterfaces().length > 1) {
//						throw new BaseRuntimeException(CommonErrorCodeEnum.SYSTEM_ERROR, "当前类有多个接口，必须指定在WebServiceServer注解中指定serviceInterface");
//					}
//					serviceInterface = entry.getValue().getClass().getInterfaces()[0];
//				} else {
//					serviceInterface = entry.getValue().getClass();
//				}
//			}
//			serviceExport.setServiceBean(entry.getValue());
//			serviceExport.setServiceInterface(serviceInterface);
//			serviceExport.setServiceUrlName(webserviceServerAnno.serviceUrlName());
//			serviceExport.setServiceUrlRootPath(webserviceServerAnno.serviceUrlRootPath());
//			cxfServiceConfigs.add(serviceExport);
//		}
//
//		CXFServiceConfig helloFacadeServiceConfig = null;
//		// 查找Hello服务,该服务必须在最后发布，以便设置软负载上线
//		for (final CXFServiceConfig serviceConfig : cxfServiceConfigs) {
//			if (serviceConfig.getServiceInterface().equals(HelloFacade.class)) {
//				helloFacadeServiceConfig = serviceConfig;
//				break;
//			}
//		}
//		// 处理扩展点: 服务输出组件
//		for (final CXFServiceConfig serviceExport : cxfServiceConfigs) {
//			if (serviceExport != helloFacadeServiceConfig) {
//				try {
//					exportSingle(serviceExport);
//				} catch (Exception e) {
//					throw new BaseRuntimeException(CommonErrorCodeEnum.SYSTEM_ERROR, "发布WebService服务异常,serviceExport=" + serviceExport, e);
//				}
//			}
//		}
//
//		// 配置Facade在最后输出
//		exportSingle(helloFacadeServiceConfig);
//	}
//
//	private void exportSingle(final CXFServiceConfig serviceExport) {
//		if (webserviceClassList.contains(serviceExport.getServiceInterface())) {
//			log.warn("类" + serviceExport.getServiceInterface() + "，已经发布成WebService了，请检查代码，serviceExport=" + serviceExport);
//			return;
//		}
//		SpringServerFactoryBean serverFactoryBean = new SpringServerFactoryBean();
//		AegisDatabinding dataBinding = (AegisDatabinding) this.applicationContext.getBean("dataBinding");
//		serverFactoryBean.setDataBinding(dataBinding);
//		serverFactoryBean.setServiceClass(serviceExport.getServiceInterface());
//		serverFactoryBean.setBus(cxf);
//
//		Object serviceProxyBean = ProxyFactory.getProxy(serviceExport.getServiceInterface(), new MethodInterceptor() {
//			/*
//			 * (non-Javadoc)
//			 *
//			 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org. aopalliance .intercept.MethodInvocation)
//			 */
//			@Override
//			public Object invoke(MethodInvocation invocation) throws Throwable {
//				// TODO Auto-generated method stub
//				long start = System.currentTimeMillis();
//				try {
//					if (BenchTracerHolder.isTraceEntryEnabled()) {
//						BenchTracerHolder.getTracer().setMessage(serviceExport.getServiceInterface().getSimpleName() + "." + invocation.getMethod().getName());
//						// 开启URL Trace
//						BenchTracerHolder.startEntry(TraceEntryTypeEnum.WS_SERVER, serviceExport.getServiceInterface().getSimpleName(), invocation.getMethod().getName(),
//								"webservice server execute");
//						BenchTracerHolder.getTracer().getCurrentEntry().setParameters(ArrayUtils.toString(invocation.getArguments()));
//					}
//
//					Object returnObj = invocation.getMethod().invoke(serviceExport.getServiceBean(), invocation.getArguments());
//					// 增加成功调用计数
//					webServiceServerMethodCallCountComponent.increaseSuccessCount(invocation.getMethod(), (int) (System.currentTimeMillis() - start));
//					return returnObj;
//				} catch (Throwable e) {
//					// 增加失败调用计数
//					webServiceServerMethodCallCountComponent.increaseFailedCount(invocation.getMethod());
//					throw e;
//				} finally {
//					if (BenchTracerHolder.isTraceEntryEnabled()) {
//						BenchTracerHolder.stopEntry();
//					}
//				}
//			}
//		});
//		serverFactoryBean.setServiceBean(serviceProxyBean);
//		serverFactoryBean.setApplicationContext(applicationContext);
//		// /cxf/SserviceClass
//		// 检查接口名是否正确配置
//		if (serviceExport.getServiceInterface() == null) {
//			throw new BaseRuntimeException(CommonErrorCodeEnum.SYSTEM_ERROR, "WS接口没有正确配置,serviceExport=" + serviceExport);
//		}
//		/****************
//		 * 构造URL地址
//		 ***************/
//		StringBuffer addressBuffer = new StringBuffer("/");
//		// 增加根路径
//		if (!StringUtils.isEmpty(serviceExport.getServiceUrlRootPath())) {
//			addressBuffer.append(serviceExport.getServiceUrlRootPath()).append("/");
//		}
//		// 如果是非app类的，则增加bench路径
//		if (!StringUtils.startsWith(serviceExport.getServiceInterface().getName(), "com.bench.app.")) {
//			addressBuffer.append("bench/");
//		}
//		addressBuffer.append(StringUtils.defaultIfEmpty(serviceExport.getServiceUrlName(), serviceExport.getServiceInterface().getSimpleName()));
//		serverFactoryBean.setAddress(addressBuffer.toString());
//
//		// 初始化属性
//		if (serverFactoryBean.getProperties() == null) {
//			serverFactoryBean.setProperties(new HashMap<String, Object>());
//		}
//		if (serviceExport.getOverrideTypes() != null && serviceExport.getOverrideTypes().size() > 0) {
//			List<String> overrideTypeList = StringUtils.trimWithLine(serviceExport.getOverrideTypes());
//			Set<String> overrirdeTypeSet = new HashSet<String>();
//			overrirdeTypeSet.addAll(overrideTypeList);
//			dataBinding.setOverrideTypes(overrirdeTypeSet);
//			dataBinding.getAegisContext().setWriteXsiTypes(true);
//		}
//
//		if (serverFactoryBean.getInInterceptors() == null) {
//			serverFactoryBean.setInInterceptors(new ArrayList<Interceptor<? extends Message>>());
//		}
//		serverFactoryBean.getInInterceptors().add(webServiceCallInfoGetInterceptor);
//		serverFactoryBean.init();
//		serviceFactoryBeans.add(serverFactoryBean);
//	}
//
//	@Override
//	public void loading(BenchRuntime runtime) throws Exception {
//		// TODO Auto-generated method stub
//		// 如果无效，则不启动
//		if (!WebServiceServerConfiguration.INSTANCE.isEnabled()) {
//			return;
//		}
//		GZIPFeature gzipFeature = new GZIPFeature();
//		gzipFeature.setThreshold(2048);
//		cxf.getFeatures().add(gzipFeature);
//		FastInfosetFeature fastInfosetFeature = new FastInfosetFeature();
//		cxf.getFeatures().add(fastInfosetFeature);
//	}
//
//	/**
//	 * @param applicationContext
//	 *            The applicationContext to set.
//	 */
//	public void setApplicationContext(ApplicationContext applicationContext) {
//		this.applicationContext = applicationContext;
//	}
//
//	/**
//	 * @param cxf
//	 *            The cxf to set.
//	 */
//	public void setCxf(Bus cxf) {
//		this.cxf = cxf;
//	}
//
//	@Override
//	public void started(BenchRuntime runtime) {
//		// TODO Auto-generated method stub
//		// 如果无效，则不启动
//		if (!WebServiceServerConfiguration.INSTANCE.isEnabled()) {
//			return;
//		}
//		this.publishService();
//	}
//
//}
