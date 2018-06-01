package baseProject.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class CacheConfig {
	
	@Bean  
	  public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){  
	    System.out.println("CacheConfiguration.ehCacheManagerFactoryBean()");  
	    EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();  
	    cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
	    cacheManagerFactoryBean.setShared(true);  
	    return cacheManagerFactoryBean;  
	  }

	@Bean
	public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean factoryBean) {
		System.out.println("init cache manager");
		return new EhCacheCacheManager(factoryBean.getObject());
	}
	


}
