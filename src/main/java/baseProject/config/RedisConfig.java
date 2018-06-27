package baseProject.config;

import java.nio.charset.Charset;
import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import baseProject.utils.LogUtil;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    int port;
    @Value("${spring.redis.pool.max-idle}")
    int maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    int minIdle;
    @Value("${spring.redis.pool.max-active}")
    int maxActive;
    @Value("${spring.redis.pool.max-wait}")
    int maxWait;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LogUtil.debug("init redis connection factory:" + host + ":" + port);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdle);
        JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(poolConfig).build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                clientConfiguration);
        return jedisConnectionFactory;
    }
    // 设置序列化方式
    // 缓存自动前缀
    // 设置缓存自动过期时间,30秒
    @Primary
    @Bean(name="expireOneDay")
    public RedisCacheManager redisCacheManagerExpireOneDay(RedisConnectionFactory connectionFactory) {
       
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer());
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.serializeValuesWith(valueSerializationPair);
        cacheConfiguration = cacheConfiguration.prefixKeysWith("myPrefix");
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofDays(1));

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
        return redisCacheManager;
    }
    
    @Bean(name="expireOneHour")
    public RedisCacheManager redisCacheManagerExpireOneHour(RedisConnectionFactory connectionFactory) {
       
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer());
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.serializeValuesWith(valueSerializationPair);
        cacheConfiguration = cacheConfiguration.prefixKeysWith("myPrefix");
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofHours(1));

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
        return redisCacheManager;
    }

    /**
     * 
     */
    /**
     * 自定义serializer
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
