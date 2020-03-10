package com.zuoer.sofa.blog.base.config;

import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.zuoer.sofa.blog.base.datasource.DataSourceConfig;

/**
 * @author zuoer
 * @version $Id: BaseConfiguration.java, v 0.1 2019/12/13 9:51 zuoer Exp $$
 */
public class BaseConfiguration {

    public static final BaseConfiguration INSTANCE = new BaseConfiguration();

    /**
     * rpc服务配置
     */
    private ServerConfig serverConfig;

    /**
     * 注册中心
     */
    private RegistryConfig registryConfig;

    /**
     * 数据源配置
     */
    private DataSourceConfig dataSourceConfig;

    /**
     * 返回实例
     *
     * @return
     */
    public static BaseConfiguration getInstance() {
        return INSTANCE;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }
}
