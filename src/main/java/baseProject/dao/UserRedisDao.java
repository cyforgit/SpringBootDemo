package baseProject.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserRedisDao {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public String getValue(String key)
	{
		System.out.println(redisTemplate.opsForValue().get(key));
		return redisTemplate.opsForValue().get(key);
	}
}
