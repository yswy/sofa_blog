/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.web.home.json;

import com.zuoer.sofa.blog.dal.entity.WebAccessLogDealDO;
import com.zuoer.sofa.blog.dal.mapper.WebAccessLogDealMapper;
import com.zuoer.sofa.blog.facade.api.HelloFacade;
import com.zuoer.sofa.blog.facade.api.request.HelloRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author zuoer
 *
 * @version $Id: HelloWorldConntroller.java, v 0.1 2019年11月12日 上午10:55:35 zuoer Exp $
 */

@Controller
public class HelloWorldJson {
	
	@Autowired
	private WebAccessLogDealMapper webAccessLogDealMapper;

//	@SofaReferenceBinding(bindingType="rest")
//	@SofaReference
//	@Autowired
	private HelloFacade helloFacade;

	@RequestMapping("hello.json")
	@ResponseBody
	public String hello() {

		WebAccessLogDealDO webAccessLogDealDO =webAccessLogDealMapper.selectById("4d12b17718794474842efab9c341299c");

		return "hello world!"+webAccessLogDealDO.getId();
	}

	@RequestMapping("helloStr.json")
	@ResponseBody
	public String helloStr() {
		return helloFacade.helloStr("测试");
	}

	@RequestMapping("helloObject.json")
	@ResponseBody
	public String helloObject() {
		HelloRequest request=new HelloRequest();
		request.setName("测试");
		return helloFacade.helloObject(request).toString();
	}
}
