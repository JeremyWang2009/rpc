package com.zfzj.client.config;

import com.zfzj.client.proxy.ProxyFactory;
import com.zfzj.common.RpcInterface;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author jeremy.wang
 * @date 2019-11-12
 */
@Configuration
public class RpcConfig implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections("com.zfzj");
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Set<Class<?>> clzs = reflections.getTypesAnnotatedWith(RpcInterface.class);
        for (Class<?> clz : clzs) {
            beanFactory.registerSingleton(clz.getSimpleName(), ProxyFactory.create(clz));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
