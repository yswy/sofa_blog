package com.zuoer.sofa.blog.base.rpc;

/**
 * @author zuoer
 * @version $Id: RpcServiceComponent.java, v 0.1 2019/12/11 18:05 zuoer Exp $$
 */
public interface RpcServiceComponent {

    /**
     * 发布服务
     * @param serviceClass
     * @param <T>
     */
    public <T> void publicSerice(Class<T> serviceClass);

    /**
     * 获取服务
     * @param serviceClass
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> serviceClass,String providerAppName,String consumerAppName);
}
