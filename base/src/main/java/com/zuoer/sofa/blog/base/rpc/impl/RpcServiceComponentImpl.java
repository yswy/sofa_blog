package com.zuoer.sofa.blog.base.rpc.impl;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuoer.sofa.blog.base.BaseLifeCycle;
import com.zuoer.sofa.blog.base.annotation.RestServer;
import com.zuoer.sofa.blog.base.config.BaseConfiguration;
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

    @Override
    public void starting(BaseRuntime runtime) throws Exception {
        //在启动中抓起spring中的rpc服务，然后进行发布
        Map<String, Object> serviceBean =BaseRuntime.getInstance().getContext().getBeansWithAnnotation(RestServer.class);

        for (Map.Entry<String, Object> entry : serviceBean.entrySet()) {
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

        }

    }

    private <T> ProviderConfig<T> getProvider(Class<?> serviceClass,T bean) {

        ProviderConfig<T> providerConfig = new ProviderConfig<T>();
        providerConfig.setInterfaceId(serviceClass.getName()); // 指定接口
        providerConfig.setRef(bean);// 指定实现
        return providerConfig;
    }


    private <T> void publicSerice(Class<?> serviceClass,T bean) {

        BaseConfiguration baseConfiguration=BaseConfiguration.getInstance();

        ProviderConfig<T> providerConfig = new ProviderConfig<T>();
        providerConfig.setInterfaceId(serviceClass.getName()); // 指定接口
        providerConfig.setRef(bean);// 指定实现
        providerConfig.setRegistry(baseConfiguration.getRegistryConfig());//设置注册中心
        providerConfig.setServer(baseConfiguration.getServerConfig()); // 指定服务端
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
