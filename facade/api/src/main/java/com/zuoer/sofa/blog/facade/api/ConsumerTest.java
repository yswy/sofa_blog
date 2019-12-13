package com.zuoer.sofa.blog.facade.api;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.zuoer.sofa.blog.facade.api.request.HelloRequest;

/**
 * @author zuoer
 * @version $Id: ConsumerTest.java, v 0.1 2019/12/11 17:54 zuoer Exp $$
 */
public class ConsumerTest {

    public static void main(String[] args) {

        ConsumerConfig<HelloFacade> consumerConfig = new ConsumerConfig<HelloFacade>()
                .setInterfaceId(HelloFacade.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址
        // 生成代理类
        HelloFacade helloService = consumerConfig.refer();
        System.out.println(helloService.helloStr("ceshi "));

        HelloRequest request = new HelloRequest();
        request.setName("测试");
        System.out.println(helloService.helloObject(request).getResultStr());


        ConsumerConfig<ByeFacade> consumerConfig2 = new ConsumerConfig<ByeFacade>()
                .setInterfaceId(ByeFacade.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址
        // 生成代理类
        ByeFacade byeFacade = consumerConfig2.refer();
        System.out.println(byeFacade.byeStr("ceshi "));

        System.out.println(byeFacade.byeObject(request).getResultStr());

    }
}
