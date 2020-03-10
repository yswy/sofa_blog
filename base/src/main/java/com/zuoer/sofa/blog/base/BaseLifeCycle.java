package com.zuoer.sofa.blog.base;

import com.zuoer.sofa.blog.base.order.Ordered;
import com.zuoer.sofa.blog.base.runtime.BaseRuntime;

/**
 * @author zuoer
 * @version $Id: BaseLifeCycle.java, v 0.1 2019/12/12 9:57 zuoer Exp $$
 */
public interface BaseLifeCycle extends Ordered {
    /**
     * 系统正在配置，此时可以进行系统调整，如注册spring bean，容器未加载
     *
     * @param runtime
     */
    public default void configuring(BaseRuntime runtime) {

    }

    /**
     * 系统已配置完毕，容器未加载
     *
     * @param runtime
     */
    public default void configured(BaseRuntime runtime) {

    }

    /**
     * 系统正在加载，一般是读入缓存，从本地加载配置等<br>
     *
     * @param runtime
     * @throws Exception
     */
    public default void loading(BaseRuntime runtime) throws Exception {
    };

    /**
     * 系统加载完成<br>
     *
     * @param runtime
     * @throws Exception
     */
    public default void loaded(BaseRuntime runtime) throws Exception {
    };

    /**
     * 准备阶段<br>
     *
     * @param runtime
     * @throws Exception
     */
    public default void preparing(BaseRuntime runtime) throws Exception {
    };

    /**
     * 准备后阶段<br>
     *
     * @param runtime
     * @throws Exception
     */
    public default void prepared(BaseRuntime runtime) throws Exception {
    };

    /**
     * 初始化阶段
     */
    public default void initializing(BaseRuntime runtime) throws Exception {
    };

    /**
     * 初始化后阶段
     *
     * @param runtime
     * @throws Exception
     */
    public default void initialized(BaseRuntime runtime) throws Exception {

    }

    /**
     * 正在启动
     */
    public default void starting(BaseRuntime runtime) throws Exception {
    };

    /**
     * 启动成功
     */
    public default void started(BaseRuntime runtime) throws Exception {
    };

    /**
     * 正在关闭
     *
     * @param runtime
     */
    public default void shuttingDown(BaseRuntime runtime) {

    }

    /**
     * 关闭
     *
     * @param runtime
     */
    public default void shutdown(BaseRuntime runtime) {

    }

    /**
     * 失败
     *
     * @param runtime
     */
    public default void failed(BaseRuntime runtime) {

    }

    @Override
    default int order() {
        // TODO Auto-generated method stub
        return Ordered.DEFAULT_ORDER;
    }
}
