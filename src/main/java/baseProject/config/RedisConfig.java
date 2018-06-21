package baseProject.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import baseProject.utils.LogUtil;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
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
        LogUtil.debug("init redis connection factory");
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

    /**
     * 
     */

    // @Bean
    // public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory
    // redisConnectionFactory) {
    // RedisTemplate<String, String> template = new RedisTemplate<>();
    // template.setConnectionFactory(redisConnectionFactory);
    // template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    // template.setKeySerializer(new StringRedisSerializer());
    // template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
    // template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    // return template;
    // }
}
