package com.zuoer.sofa.blog.base.loader;

import com.alipay.sofa.rpc.config.ServerConfig;
import com.alipay.sofa.rpc.server.bolt.BoltServer;
import com.alipay.sofa.rpc.server.rest.RestServer;
import com.zuoer.sofa.blog.base.BaseLifeCycle;
import com.zuoer.sofa.blog.base.config.BaseConfiguration;
import com.zuoer.sofa.blog.base.runtime.BaseRuntime;
import org.springframework.stereotype.Component;

/**
 * 基础配置加载器
 *
 * @author zuoer
 * @version $Id: BaseConfigurationLoader.java, v 0.1 2019/12/13 9:54 zuoer Exp $$
 */
@Component
public class BaseConfigurationLoader implements BaseLifeCycle {

    private ServerConfig getServerConfig() {
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("rest") // 设置一个协议，默认bolt
                .setPort(8431) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程
        return serverConfig;
    }

    @Override
    public void loading(BaseRuntime runtime) throws Exception {
        BaseConfiguration baseConfiguration = BaseConfiguration.getInstance();
        RestServer restServer = new RestServer();
        restServer.init(getServerConfig());
        baseConfiguration.setRestServer(restServer);

        BoltServer boltServer=new BoltServer();
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("默认bolt") // 设置一个协议，默认bolt
                .setPort(12200) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程
        serverConfig.setHost("127.0.0.1");
        boltServer.init(serverConfig);
        baseConfiguration.setBoltServer(boltServer);
    }
}
