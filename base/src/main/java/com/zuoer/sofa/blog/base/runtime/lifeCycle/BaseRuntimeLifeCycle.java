package com.zuoer.sofa.blog.base.runtime.lifeCycle;

import com.zuoer.sofa.blog.base.BaseLifeCycle;
import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.runtime.BaseRuntime;
import com.zuoer.sofa.blog.base.runtime.initializer.RuntimeInitializer;
import com.zuoer.sofa.blog.base.runtime.status.RuntimeStatusEnum;
import com.zuoer.sofa.blog.base.utils.ClassUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础生命周期
 * @author zuoer
 * @version $Id: BaseRuntimeLifeCycle.java, v 0.1 2019/12/12 9:49 zuoer Exp $$
 */
public class BaseRuntimeLifeCycle {

    private static final BaseRuntimeLifeCycle INSTANCE = new BaseRuntimeLifeCycle();

    /**
     * 允许环境初始化
     */
    private List<RuntimeInitializer> runtimeInitializers = new ArrayList<RuntimeInitializer>();

    private List<BaseLifeCycle> lifeCycles;

    public static BaseRuntimeLifeCycle getInstance() {
        return INSTANCE;
    }

    public BaseRuntimeLifeCycle() {

    }

    /**
     * @param clasz
     * @return
     */
    public RuntimeInitializer getRuntimeInitializer(Class<?> clasz) {
        for (RuntimeInitializer runtimeInitializer : runtimeInitializers) {
            if (clasz == runtimeInitializer.getClass()) {
                return runtimeInitializer;
            }
        }
        return null;
    }

    /**
     * 注册初始化容器
     *
     * @param initializer
     */
    public void registerInitializer(RuntimeInitializer initializer) {
        if (!runtimeInitializers.contains(initializer)) {
            this.runtimeInitializers.add(initializer);
        }

    }

    public void init() {
        BaseRuntime.getInstance().setStatus(RuntimeStatusEnum.STARTING);
        //  TODO 排序
//        Collections.sort(this.runtimeInitializers, OrderedComparator.INSTANCE);
        System.out.println("进行初始化....");
        for (RuntimeInitializer initializer : this.runtimeInitializers) {
            long start = System.currentTimeMillis();
            //TODO 等日志正好进来之后使用日志打印
            System.out.println("开始执行" + AopUtils.getTargetClass(initializer) + ".initialize()");
            initializer.initialize();
            System.out.println("执行结束" + AopUtils.getTargetClass(initializer) + ".initialize(),cost=" + (System.currentTimeMillis() - start));
//            if (CommonLogger.getSysInit().isInfoEnabled()) {
//                CommonLogger.getSysInit().info("开始执行" + AopUtils.getTargetClass(initializer) + ".initialize()");
//            }
//            if (CommonLogger.getSysInit().isInfoEnabled()) {
//                CommonLogger.getSysInit().info("执行结束" + AopUtils.getTargetClass(initializer) + ".initialize(),cost=" + (System.currentTimeMillis() - start));
//            }
        }
    }

    /**
     * 启动
     */
    public void load() {
        // 生命周期
        lifeCycles = new ArrayList<BaseLifeCycle>(BaseRuntime.getInstance().getContext().getBeansOfType(BaseLifeCycle.class).values());
        // TODO 排序
//        Collections.sort(lifeCycles, OrderedComparator.INSTANCE);
        //TODO 检查器,检查各个时间的一些必须依赖的配置是否有
//        BaseCheckComponent benchCheckComponent = BaseRuntime.getInstance().getContext().getBean(BaseCheckComponent.class);
//
//        // loading前检查
//        benchCheckComponent.checkBeforeLoading(BaseRuntime.getInstance());
        /**
         * 执行loading
         */
        System.out.println("执行loading....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "loading", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".loading");
//                if (CommonLogger.getSysInit().isInfoEnabled()) {
//                    CommonLogger.getSysInit().info("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".loading");
//                }
                lifeCycle.loading(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".loading,cost=" + (System.currentTimeMillis() - start));
//                if (CommonLogger.getSysInit().isInfoEnabled()) {
//                    CommonLogger.getSysInit().info("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".loading,cost=" + (System.currentTimeMillis() - start));
//                }
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行loading异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO loading后检查
//        benchCheckComponent.checkAfterLoading(BaseRuntime.getInstance());

        /**
         * 执行loaded
         */
        System.out.println("执行loaded....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try { // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "loaded", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".loaded");
//                if (CommonLogger.getSysInit().isInfoEnabled()) {
//                    CommonLogger.getSysInit().info("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".loaded");
//                }
                lifeCycle.loaded(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".loaded,cost=" + (System.currentTimeMillis() - start));
//                if (CommonLogger.getSysInit().isInfoEnabled()) {
//                    CommonLogger.getSysInit().info("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".loaded,cost=" + (System.currentTimeMillis() - start));
//                }
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行loaded异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO loaded后检查
//        benchCheckComponent.checkAfterLoaded(BaseRuntime.getInstance());

        /**
         * 执行preparing
         */
        System.out.println("执行preparing....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "preparing", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".preparing");
                lifeCycle.preparing(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".preparing,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行preparing异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO preparing后检查
//        benchCheckComponent.checkAfterPreparing(BaseRuntime.getInstance());

        /**
         * 执行prepared
         */
        System.out.println("执行prepared....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "prepared", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".prepared");
                lifeCycle.prepared(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".prepared,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行prepared异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO prepared后检查
//        benchCheckComponent.checkAfterPrepared(BaseRuntime.getInstance());

        //TODO initializing前检查
//        benchCheckComponent.checkBeforeInitializing(BaseRuntime.getInstance());

        /**
         * 执行initiazling
         */
        System.out.println("执行initiazling....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "initializing", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".initializing");
                lifeCycle.initializing(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".initializing,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行initiazling异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO initializing后检查
//        benchCheckComponent.checkAfterInitializing(BaseRuntime.getInstance());

        /**
         * 执行initalized
         */
        System.out.println("执行initalized....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "initialized", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".initialized");
                lifeCycle.initialized(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".initialized,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行initialized异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO initializing后检查
//        benchCheckComponent.checkAfterInitialized(BaseRuntime.getInstance());
    }

    /**
     * 启动
     */
    public void start() {
        // TODO Auto-generated method stub
        //TODO 检查器
//        BaseCheckComponent benchCheckComponent = BaseRuntime.getInstance().getContext().getBean(BaseCheckComponent.class);
        /**
         * 执行starting
         */
        System.out.println("执行starting....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "starting", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".starting");
                lifeCycle.starting(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".starting,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行starting异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO
//        benchCheckComponent.checkAfterStarting(BaseRuntime.getInstance());

        /**
         * 执行started
         */
        System.out.println("执行started....");
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "started", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".started");
                lifeCycle.started(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".started,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行started异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
        //TODO
//        benchCheckComponent.checkAfterStarted(BaseRuntime.getInstance());
        BaseRuntime.getInstance().setStatus(RuntimeStatusEnum.STARTED);
        System.out.println("系统启动成功");
        //TODO 等日志处理好进来之后使用日志打印
//        BaseConsoleLogger.log("*************************************************************************", false, true, true);
//        BaseConsoleLogger.log("**系统启动成功，cost=" + (System.currentTimeMillis() - BaseConfiguration.getInstance().getSystem().getGmtStartup().getTime()), true, false,
//                false);
//        BaseConsoleLogger.log("*************************************************************************", false, true, true);
    }

    /**
     * 关闭
     *
     *
     * @param cause
     *            关闭原因
     */
    public synchronized void shutdown(String cause) {
        // 如果已经是关闭了，则不处理
        if (BaseRuntime.getInstance().getStatus() == RuntimeStatusEnum.SHUTTING_DOWN || BaseRuntime.getInstance().getStatus() == RuntimeStatusEnum.SHUTDOWN) {
            return;
        }
        String message = "执行shutdown，系统开始关闭..原因：" + cause;
//        log.warn(message);
//        BaseConsoleLogger.log(message);
        BaseRuntime.getInstance().setStatus(RuntimeStatusEnum.SHUTTING_DOWN);
        if (lifeCycles != null) {
            for (BaseLifeCycle lifeCycle : lifeCycles) {
                try {
                    // 未重载，不执行
                    if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "shuttingDown", BaseRuntime.class)) {
                        continue;
                    }
                    long start = System.currentTimeMillis();
                    //TODO 等日志处理好进来之后使用日志打印
                    System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".shuttingDown");
                    lifeCycle.shuttingDown(BaseRuntime.getInstance());
                    System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".shuttingDown,cost=" + (System.currentTimeMillis() - start));
                } catch (Exception e) {
//                    log.error("执行shuttingDown异常，忽略，继续执行,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
                }
            }
            for (BaseLifeCycle lifeCycle : lifeCycles) {
                try {
                    // 未重载，不执行
                    if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "shutdown", BaseRuntime.class)) {
                        continue;
                    }
                    long start = System.currentTimeMillis();
                    //TODO 等日志处理好进来之后使用日志打印
                    System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".shutdown");
                    lifeCycle.shutdown(BaseRuntime.getInstance());
                    System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".shutdown,cost=" + (System.currentTimeMillis() - start));
                } catch (Exception e) {
//                    log.error("执行shutdown异常，忽略，继续执行,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
                }
            }
        }

        // 退出
        SpringApplication.exit(BaseRuntime.getInstance().getContext(), new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                // TODO Auto-generated method stub
                return 200;
            }
        });
        BaseRuntime.getInstance().setStatus(RuntimeStatusEnum.SHUTDOWN);
//        BaseConsoleLogger.log("系统已关闭");
        System.out.println("系统已关闭");
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                while (BaseRuntime.getInstance().getContext().isRunning()) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
                System.exit(0);
            }
        }.start();

    }

    public void failed() {
        /**
         * 执行failed
         */
        for (BaseLifeCycle lifeCycle : lifeCycles) {
            try {
                // 未重载，不执行
                if (!ClassUtils.isOverride(lifeCycle.getClass(), BaseLifeCycle.class, "failed", BaseRuntime.class)) {
                    continue;
                }
                long start = System.currentTimeMillis();
                //TODO 等日志处理好进来之后使用日志打印
                System.out.println("开始执行" + AopUtils.getTargetClass(lifeCycle) + ".failed");
                lifeCycle.failed(BaseRuntime.getInstance());
                System.out.println("执行结束" + AopUtils.getTargetClass(lifeCycle) + ".failed,cost=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "执行failed异常,lifeCycle=" + AopUtils.getTargetClass(lifeCycle), e);
            }
        }
    }

}
