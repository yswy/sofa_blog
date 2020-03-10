package com.zuoer.sofa.blog.base.datasource;

/**
 * 数据源配置
 * @author zuoer
 * @version $Id: DataSourceConfig.java, v 0.1 2019/12/17 18:23 zuoer Exp $$
 */
public class DataSourceConfig {

    /**
     * 驱动名
     */
    private String driverClassName;

    /**
     * 链接url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
