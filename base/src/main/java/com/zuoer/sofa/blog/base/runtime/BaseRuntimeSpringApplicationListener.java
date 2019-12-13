/**
 * BaseCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.runtime;

import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.runtime.lifeCycle.BaseRuntimeLifeCycle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


/**
 * 
 * @author zuoer
 *
 * @version $Id: BaseRuntimeSpringApplicationListener.java, v 0.1 2019年12月12日 下午2:24:58 zuoer Exp $
 */
public class BaseRuntimeSpringApplicationListener implements SpringApplicationRunListener {

	public BaseRuntimeSpringApplicationListener(SpringApplication application, String[] args) {
		BaseRuntime.getInstance().setApplication(application);
		BaseRuntime.getInstance().setArgs(args);

	}

	@Override
	public void starting() {
		// TODO Auto-generated method stub
		BaseRuntimeLifeCycle.getInstance().init();
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		// TODO Auto-generated method stub
		BaseRuntime.getInstance().setEnvironment(environment);
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		BaseRuntime.getInstance().setContext(context);
	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void started(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		BaseRuntimeLifeCycle.getInstance().load();
	}

	@Override
	public void running(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		BaseRuntimeLifeCycle.getInstance().start();
	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		// TODO Auto-generated method stub
		//TODO 后面用日志
//		BaseConsoleLogger.log("应用启动失败,exception=" + exception.toString());
		System.out.println("应用启动失败,exception=" + exception.toString());
		exception.printStackTrace();
		BaseRuntimeLifeCycle.getInstance().failed();
		System.exit(0);
		throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "应用启动失败" , exception);

	}

}
