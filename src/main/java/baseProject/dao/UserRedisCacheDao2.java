package baseProject.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import baseProject.pojo.response.BaseResponse;

@Service
@CacheConfig
public class UserRedisCacheDao2 {
    @Cacheable(cacheManager = "expireOneHour", value = "onehour", key = "'_onehour_'+#key", sync = true)
    public BaseResponse getBaseResponse2(String key) {
        System.out.println("cache expire");
        return new BaseResponse(0, key);
    }

}
