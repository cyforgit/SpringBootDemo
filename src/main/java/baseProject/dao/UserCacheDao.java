//package baseProject.dao;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@Cacheable
//public class UserCacheDao {
//	
//	
//	@Autowired
//	UserDao userDao;
//	private static final String CACHE_NAME="userCache";
//	
//	@Cacheable(value=CACHE_NAME,key="#id",sync=true)
//	public List<?> getUserById(int id) {
//		System.out.println("not cache when get user by id");
//			return userDao.getUserById(id);
//	}
//	/**
//	 * Whether the eviction should occur before the method is invoked.
//	 * @param id
//	 */
//	@CacheEvict(value=CACHE_NAME,key="#id",beforeInvocation=true)
//	public void deleteCache(int id) {
//		System.out.println("delete cache of id:"+id);
//	}
//	
//}
