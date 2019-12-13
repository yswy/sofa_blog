package com.zuoer.sofa.blog.base.rpc.impl;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuoer.sofa.blog.base.BaseLifeCycle;
import com.zuoer.sofa.blog.base.annotation.RestServer;
import com.zuoer.sofa.blog.base.context.BaseApplicationContext;
import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.nill.Null;
import com.zuoer.sofa.blog.base.rpc.RpcServiceComponent;
import com.zuoer.sofa.blog.base.runtime.BaseRuntime;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zuoer
 * @version $Id: RpcServiceComponentImpl.java, v 0.1 2019/12/11 18:06 zuoer Exp $$
 */
@Component
public class RpcServiceComponentImpl implements RpcServiceComponent, BaseLifeCycle {

    private ServerConfig serverConfig;

    private ServerConfig getServerConfig() {
        if (serverConfig != null) {
            return serverConfig;
        }
        synchronized (this) {
            if (serverConfig != null) {
                return serverConfig;
            }
            ServerConfig serverConfig = new ServerConfig()
                    .setProtocol("bolt") // 设置一个协议，默认bolt
                    .setPort(12200) // 设置一个端口，默认12200
                    .setDaemon(false); // 非守护线程
            this.serverConfig = serverConfig;
            return this.serverConfig;
        }
    }

    @Override
    public void starting(BaseRuntime runtime) throws Exception {
        //在启动中抓起spring中的rpc服务，然后进行发布
        Map<String, Object> servieBeanMap =BaseRuntime.getInstance().getContext().getBeansWithAnnotation(RestServer.class);

        for (Map.Entry<String, Object> entry : servieBeanMap.entrySet()) {
            RestServer restServerAnno = entry.getValue().getClass().getAnnotation(RestServer.class);
            Class<?> serviceInterface = restServerAnno.serviceInterface();
            if (serviceInterface == Null.class) {
                if (entry.getValue().getClass().getInterfaces().length > 0) {
                    if (entry.getValue().getClass().getInterfaces().length > 1) {
                        throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "当前类有多个接口，必须指定在WebServiceServer注解中指定serviceInterface");
                    }
                    serviceInterface = entry.getValue().getClass().getInterfaces()[0];
                } else {
                    serviceInterface = entry.getValue().getClass();
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            publicSerice(serviceInterface,objectMapper.convertValue(entry.getValue(), serviceInterface));

//            ProviderConfig providerConfig=getProvider(serviceInterface,objectMapper.convertValue(entry.getValue(), serviceInterface));
//
//            ProviderProxyInvoker providerProxyInvoker=new ProviderProxyInvoker(providerConfig);
//
//            BaseConfiguration.getInstance().getRestServer().registerProcessor(providerConfig,providerProxyInvoker);
//            BaseConfiguration.getInstance().getBoltServer().registerProcessor(providerConfig,providerProxyInvoker);
        }
//        BaseConfiguration.getInstance().getRestServer().start();
//        BaseConfiguration.getInstance().getBoltServer().start();

    }

    private <T> ProviderConfig<T> getProvider(Class<?> serviceClass,T bean) {

        ProviderConfig<T> providerConfig = new ProviderConfig<T>();
        providerConfig.setInterfaceId(serviceClass.getName()); // 指定接口
        providerConfig.setRef(bean);// 指定实现
        return providerConfig;
    }


    private <T> void publicSerice(Class<?> serviceClass,T bean) {

        ProviderConfig<T> providerConfig = new ProviderConfig<T>();
        providerConfig.setInterfaceId(serviceClass.getName()); // 指定接口
        providerConfig.setRef(bean);// 指定实现

        providerConfig.setServer(getServerConfig()); // 指定服务端
        providerConfig.export(); // 发布服务
    }


    public <T> void publicSerice(Class<T> serviceClass) {

        ProviderConfig<T> providerConfig = new ProviderConfig<T>();
        providerConfig.setInterfaceId(serviceClass.getName()); // 指定接口
        String implName = serviceClass.getSimpleName() + "Impl";


        //从springContent中获取对象
        ApplicationContext ac = BaseApplicationContext.getApplicationContext();

        providerConfig.setRef(ac.getBean(StringUtils.toFirstCharLowerCase(implName), serviceClass));// 指定实现

        providerConfig.setServer(getServerConfig()); // 指定服务端
        providerConfig.export(); // 发布服务
    }

    @Override
    public <T> T getService(Class<T> serviceClass, String providerAppName, String consumerAppName) {
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<T>();
        consumerConfig.setInterfaceId(serviceClass.getName());// 指定接口
        consumerConfig.setProtocol("bolt");// 指定协议
        consumerConfig.setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址

        return consumerConfig.refer();
    }
}
