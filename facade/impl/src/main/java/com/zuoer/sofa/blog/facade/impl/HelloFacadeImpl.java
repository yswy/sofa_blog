package com.zuoer.sofa.blog.facade.impl;

import com.zuoer.sofa.blog.base.annotation.RestServer;
import com.zuoer.sofa.blog.base.rpc.RpcServiceComponent;
import com.zuoer.sofa.blog.facade.api.HelloFacade;
import com.zuoer.sofa.blog.facade.api.request.HelloRequest;
import com.zuoer.sofa.blog.facade.api.result.HelloOperateResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zuoer
 * @version $Id: HelloFacadeImpl.java, v 0.1 2019/12/9 17:08 zuoer Exp $$
 */

//@Component
@RestServer
public class HelloFacadeImpl implements HelloFacade {

    @Autowired
    private RpcServiceComponent rpcServiceComponent;

    @Override
    public String helloStr(String str) {
        return "helloStr "+str;
    }

    @Override
    public HelloOperateResult helloObject(HelloRequest request) {
        HelloOperateResult result=new HelloOperateResult();
        result.setSuccess(true);
        result.setResultStr("helloObject "+request.getName());
        return result;
    }

    public void init(){
        System.out.println("通过xml初始化HelloFacadeImpl");

//        rpcServiceComponent.publicSerice(HelloFacade.class);



    }
}
