package com.zuoer.sofa.blog.base.loader;

import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
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

    @Override
    public void loading(BaseRuntime runtime) throws Exception {

        BaseConfiguration baseConfiguration = BaseConfiguration.getInstance();

        //设置注册中心
        RegistryConfig registryConfig=new RegistryConfig();
        registryConfig.setAddress("192.168.10.120:2181");
        registryConfig.setProtocol("zookeeper");
        baseConfiguration.setRegistryConfig(registryConfig);
        //设置rpc服务配置
        ServerConfig boltServerConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(12200) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程
        baseConfiguration.setServerConfig(boltServerConfig);

    }
}
