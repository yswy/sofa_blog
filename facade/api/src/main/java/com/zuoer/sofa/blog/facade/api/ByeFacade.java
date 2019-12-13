package com.zuoer.sofa.blog.facade.api;

import com.zuoer.sofa.blog.facade.api.request.HelloRequest;
import com.zuoer.sofa.blog.facade.api.result.HelloOperateResult;

/**
 * @author zuoer
 * @version $Id: HelloFacade.java, v 0.1 2019/12/9 17:00 zuoer Exp $$
 */
//@Path("/rest")
//@Consumes("application/json;charset=UTF-8")
//@Produces("application/json;charset=UTF-8")
public interface ByeFacade {

    /**
     * 字符串类型hello方法
     * @param str
     * @return
     */
//    @GET
//    @Path("byeStr")
    public String byeStr(String str);

    /**
     * 对象类型的hello方法
     * @param request
     * @return
     */
//    @GET
//    @Path("byeObject")
    public HelloOperateResult byeObject(HelloRequest request);
}
