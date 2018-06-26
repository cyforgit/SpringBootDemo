package baseProject.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig
public class UserRedisCacheDao {
    
    
    @Cacheable()
    public void getCacheUserInRedis() {

    }

}
