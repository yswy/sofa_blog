package com.zuoer.sofa.blog.facade.impl;

import com.zuoer.sofa.blog.base.annotation.RestServer;
import com.zuoer.sofa.blog.facade.api.ByeFacade;
import com.zuoer.sofa.blog.facade.api.request.HelloRequest;
import com.zuoer.sofa.blog.facade.api.result.HelloOperateResult;

/**
 * @author zuoer
 * @version $Id: ByeFacadeImpl.java, v 0.1 2019/12/13 12:25 zuoer Exp $$
 */
@RestServer
public class ByeFacadeImpl implements ByeFacade {
    @Override
    public String byeStr(String str) {
        return "byeStr "+str;
    }

    @Override
    public HelloOperateResult byeObject(HelloRequest request) {
        HelloOperateResult result=new HelloOperateResult();
        result.setSuccess(true);
        result.setResultStr("byeObject "+request.getName());
        return result;
    }
}
