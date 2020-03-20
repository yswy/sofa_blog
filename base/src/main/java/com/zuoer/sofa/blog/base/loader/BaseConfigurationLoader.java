package com.zuoer.sofa.blog.base.loader;

import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.zuoer.sofa.blog.base.config.BaseConfiguration;
import com.zuoer.sofa.blog.base.datasource.DataSourceConfig;
import com.zuoer.sofa.blog.base.order.Ordered;
import com.zuoer.sofa.blog.base.runtime.initializer.RuntimeInitializer;
import org.springframework.stereotype.Component;

/**
 * 基础配置加载器
 *
 * @author zuoer
 * @version $Id: BaseConfigurationLoader.java, v 0.1 2019/12/13 9:54 zuoer Exp $$
 */
@Component
public class BaseConfigurationLoader implements RuntimeInitializer {

    @Override
    public void initialize() {

        BaseConfiguration baseConfiguration = BaseConfiguration.getInstance();
        System.out.println("初始化注册中心");
        //设置注册中心
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("98-2.zookeeper:2181");
        registryConfig.setProtocol("zookeeper");
        baseConfiguration.setRegistryConfig(registryConfig);
        System.out.println("初始rpc服务");
        //设置rpc服务配置
        ServerConfig boltServerConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(12200) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程
        baseConfiguration.setServerConfig(boltServerConfig);

        System.out.println("初始化datasource");
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSourceConfig.setUrl("jdbc:oracle:thin:@10.0.0.91:1521/dev");
        dataSourceConfig.setPassword("zuoer_test");
//        dataSourceConfig.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSourceConfig.setUrl("jdbc:mysql://10.0.0.91:3306/zuoer_test?serverTimezone=UTC");
//        dataSourceConfig.setPassword("zuoer_test123");
        dataSourceConfig.setUsername("zuoer_test");
        baseConfiguration.setDataSourceConfig(dataSourceConfig);
    }

    @Override
    public int order() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
