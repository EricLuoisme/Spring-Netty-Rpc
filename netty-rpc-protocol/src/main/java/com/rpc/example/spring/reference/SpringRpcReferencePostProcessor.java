package com.rpc.example.spring.reference;

import com.rpc.example.annotation.RpcRemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContextAware: 获取ApplicationContext上下文信息
 * BeanClassLoaderAware: 获取Bean的类装载器
 * BeanFactoryPostProcessor: Bean实例化前执行 (才能替换为代理)
 */
@Slf4j
public class SpringRpcReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext context;
    private ClassLoader classLoader;
    private RpcClientProperties clientProperties;
    // 保存发布的引用Bean的信息
    private final Map<String, BeanDefinition> rpcRefBeanDefinition = new ConcurrentHashMap<>();


    public SpringRpcReferencePostProcessor(RpcClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Bean实例化前, 对所有BeanDefinition进行遍历
        for (String bdName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(bdName);
            String className = beanDefinition.getBeanClassName();
            if (null != className) {
                Class<?> aClass = ClassUtils.resolveClassName(className, this.classLoader);
                // 对Bean的Field进行遍历, 找出被注解的, 将其放入Map
                ReflectionUtils.doWithFields(aClass, this::parseRpcReference);
            }
        }
        // 对Map找到的Field进行遍历, 将所有已经被Proxy的BeanDefinition的都register到registry上
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinition.forEach((name, definition) -> {
            if (context.containsBean(name)) {
                log.warn("Already register Bean with name: " + name);
            }
            registry.registerBeanDefinition(name, definition);
            log.info("Register Bean with name: " + name + " successfully");
        });
    }

    /**
     * 主要是针对Controller中的Field, 找到被注解的, 将其init设置为Proxy的, 再将其BeanDefinition存入Map中
     */
    private void parseRpcReference(Field field) {
        RpcRemoteReference annotation = AnnotationUtils.getAnnotation(field, RpcRemoteReference.class);
        if (null != annotation) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringRpcReferenceBean.class);
            // 这里调用init方法是重点, 因为就会调用反射一个Proxy, 后续Bean被调用的时候, 也就是被调用了这个Proxy
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceAddress", clientProperties.getServiceAddress());
            builder.addPropertyValue("servicePort", clientProperties.getServicePort());
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinition.put(field.getName(), beanDefinition);
        }
    }
}
