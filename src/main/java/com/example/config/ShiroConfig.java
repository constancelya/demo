package com.example.config;


import com.example.shiro.AccountRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.net.httpserver.AuthFilter;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public SecurityManager securityManager(AccountRealm accountRealm){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountRealm);
        log.info("-------------->securityManager注入成功");

        return securityManager;

    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");


        Map<String, String> hashMap = new LinkedHashMap<>();

        hashMap.put("/user/home", "authc");
        hashMap.put("/user/set", "authc");
        hashMap.put("/user/upload", "authc");
        hashMap.put("/login", "anon");
        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;
    }


}
