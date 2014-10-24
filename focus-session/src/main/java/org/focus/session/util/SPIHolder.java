package org.focus.session.util;

import java.util.ServiceLoader;

public class SPIHolder {
	
	public static <T> T getService(Class<T> clazz) {
		  ServiceLoader<T> s = ServiceLoader.load(clazz);  
		  if(ObjectUtils.notNull(s)){
			  return s.iterator().next();
		  }
		  throw new RuntimeException(new StringBuilder("请检查classpath下META-INF/services/").append(clazz.getName()).append("中是否配置").toString());
	}
	
}
