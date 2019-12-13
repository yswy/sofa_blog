package com.zuoer.sofa.blog.base.runtime;

import com.zuoer.sofa.blog.base.BaseClassUtils;
import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.runtime.initializer.RuntimeInitializer;
import com.zuoer.sofa.blog.base.runtime.lifeCycle.BaseRuntimeLifeCycle;
import com.zuoer.sofa.blog.base.runtime.status.RuntimeStatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Date;

/**
 * 运行时<br>
 *     高于spring生命周期，无法使用spring的实例化
 * @author zuoer
 * @version $Id: BaseRuntime.java, v 0.1 2019/12/12 9:45 zuoer Exp $$
 */
public class BaseRuntime {

    /**
     * 运行状态
     */
    private RuntimeStatusEnum status = RuntimeStatusEnum.UNKNOWN;

    /**
     * 开始时间
     */
    private Date gmtBegin = new Date();

    /**
     * 启动成功时间
     */
    private Date gmtStarted;

    public static final BaseRuntime INSTANCE=new BaseRuntime();

    /**
     * 是否已初始化
     */
    private boolean initialized=false;

    /**
     * 生命周期
     */
    private BaseRuntimeLifeCycle lifeCycle;

    /**
     * 运行的应用
     */
    private SpringApplication application;

    /**
     * 运行参数
     */
    private String[] args;

    /**
     * 注意，改类只有在spring容器启动后才有值
     */
    private ConfigurableApplicationContext context;

    /**
     * 配置环境，应用启动才有
     */
    private ConfigurableEnvironment environment;

    /**
     * 初始化方法
     */
    private void initialize(){
        registerInitializer();
    }

    public static BaseRuntime getInstance(){
        // 因为这个在启动阶段就初始化，不存在多线程问题
        if (!INSTANCE.initialized) {
            INSTANCE.initialize();
            INSTANCE.initialized = true;
        }
        return INSTANCE;
    }

    /**
     * 注册初始化器
     */
    private void registerInitializer() {
//        this.lifeCycle.registerInitializer(this.bunldComponent);
//        this.lifeCycle.registerInitializer(this.httpCommandServerComponent);
//        this.lifeCycle.registerInitializer(new PrintStartupProcessLog());

        //这里由于高于spring生命周期，只能用包的方式获取，然后直接实例化
        for (Class<?> clasz : BaseClassUtils.getClasses()) {
            if (!RuntimeInitializer.class.isAssignableFrom(clasz)) {
                continue;
            }
            if (clasz.isInterface() ) {
                continue;
            }
            if (lifeCycle.getRuntimeInitializer(clasz) != null) {
                continue;
            }
            try {
                RuntimeInitializer initializer = (RuntimeInitializer) clasz.newInstance();
                if (initializer != null) {
                    lifeCycle.registerInitializer(initializer);
                }
            }catch (Exception e){
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "实例化base中的类异常" , e);
            }
        }
    }

    public RuntimeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RuntimeStatusEnum status) {
        this.status = status;
    }

    public Date getGmtBegin() {
        return gmtBegin;
    }

    public void setGmtBegin(Date gmtBegin) {
        this.gmtBegin = gmtBegin;
    }

    public Date getGmtStarted() {
        return gmtStarted;
    }

    public void setGmtStarted(Date gmtStarted) {
        this.gmtStarted = gmtStarted;
    }

    public BaseRuntimeLifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(BaseRuntimeLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public SpringApplication getApplication() {
        return application;
    }

    public void setApplication(SpringApplication application) {
        this.application = application;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public ConfigurableApplicationContext getContext() {
        return context;
    }

    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }
}
