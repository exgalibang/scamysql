package com.sca.controller.app.login;

import javax.annotation.PreDestroy;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * app用户缓存处理类
 */
@Component
public class AppMemory {
 
	 @Autowired
     private Cache ehcache;
 
     public void setValue(String key, String value) {
          ehcache.put(new Element(key, value));
      }
     /**
      * 获取缓存中的对象
      * @param key
      * @return
      */
     public Object getValue(String key) {
         Element element = ehcache.get(key);
         return element != null ? element.getValue() : null;
     }
     
     /**
      * 关闭缓存管理器
      */
     @PreDestroy
     protected void shutdown() {
         if (ehcache != null) {
             ehcache.getCacheManager().shutdown();
         }
     }
 
     /**
      * 保存当前登录用户信息
      * 
      * @param loginUser
      */
     public void saveLoginUser(String keySeed,AppUser loginUser) {
         // 清空之前的登录信息
         clearLoginInfoBySeed(keySeed);
         // 保存新的token和登录信息
         ehcache.put(new Element(keySeed, loginUser.getToken()));
         ehcache.put(new Element(loginUser.getToken(), loginUser));
     }
 
     /**
      * 根据token检查用户是否登录
      * 
      * @param token
      * @return
      */
     public boolean checkLoginInfo(String token) {
         Element element = ehcache.get(token);
         return element != null && (AppUser) element.getValue() != null;
     }
 
 
     /**
      * 根据seed清空登录信息
      * 
      * @param seed
      */
     public void clearLoginInfoBySeed(String seed) {
         // 根据seed找到对应的token
         Element element = ehcache.get(seed);
         if (element != null) {
             // 根据token清空之前的登录信息
             ehcache.remove(seed);
             ehcache.remove(element.getValue());
         }
     }
 }
