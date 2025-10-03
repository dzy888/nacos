package com.alibaba.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

public class NacosDemo {
    public static void main(String[] args) throws NacosException {
        String serverAddr = "localhost:8848";
        NamingService namingService = NacosFactory.createNamingService(serverAddr);

        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);

        namingService.registerInstance("user", instance);
    }
}
