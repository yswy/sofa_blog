package com.zuoer.sofa.blog.base.config;

import com.alipay.sofa.rpc.server.bolt.BoltServer;
import com.alipay.sofa.rpc.server.rest.RestServer;

/**
 * @author zuoer
 * @version $Id: BaseConfiguration.java, v 0.1 2019/12/13 9:51 zuoer Exp $$
 */
public class BaseConfiguration {

    public static final BaseConfiguration INSTANCE = new BaseConfiguration();

    private RestServer restServer;

    private BoltServer boltServer;

    /**
     * 返回实例
     *
     * @return
     */
    public static BaseConfiguration getInstance() {
        return INSTANCE;
    }

    public RestServer getRestServer() {
        return restServer;
    }

    public void setRestServer(RestServer restServer) {
        this.restServer = restServer;
    }

    public BoltServer getBoltServer() {
        return boltServer;
    }

    public void setBoltServer(BoltServer boltServer) {
        this.boltServer = boltServer;
    }
}
