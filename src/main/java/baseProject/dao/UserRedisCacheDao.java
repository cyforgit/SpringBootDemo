package baseProject.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import baseProject.pojo.response.BaseResponse;

@Service
@CacheConfig
public class UserRedisCacheDao {

    @Cacheable(cacheManager = "expireOneHour", value = "onehour", key = "'_onehour_'+#key", sync = true)
    public BaseResponse getBaseResponse2(String key) {
        System.out.println("cache expire");
        return new BaseResponse(0, key);
    }

    // 重要 unless ="#result=null"
    // 如果结果为空，不缓存，以免sync为false时，多线程进方法，热点缓存失效也不会被保存，后续请求会触发刷新缓存动作
    // 但是不能解决未加锁状态下，多线程同时进入，正确数据写入后，错误线程重新覆盖问题。
    @Cacheable(cacheManager = "expireOneDay", value = "oneday", key = "'_oneday_'+#key", sync = true, unless = "#result==null")
    public BaseResponse getBaseResponse(String key) {
        System.out.println("cache expire");
        return new BaseResponse(0, key);
    }

    // @CacheEvict
    // public void

}
