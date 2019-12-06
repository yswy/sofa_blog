/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.web.home.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zuoer.sofa.blog.dal.entity.WebAccessLogDealDO;
import com.zuoer.sofa.blog.dal.mapper.WebAccessLogDealMapper;

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

	@RequestMapping("hello.json")
	@ResponseBody
	public String hello() {
		
		WebAccessLogDealDO webAccessLogDealDO =webAccessLogDealMapper.selectById("4d12b17718794474842efab9c341299c");
		
		return "hello world!"+webAccessLogDealDO.getId();
	}
}
