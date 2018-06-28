package baseProject.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baseProject.dao.UserDao;
import baseProject.dao.UserRedisCacheDao;
import baseProject.dao.UserRedisDao;
import baseProject.pojo.response.BaseResponse;
import baseProject.utils.LogUtil;

@RestController
public class MvcController {

    @Value("${kkkkk}")
    public String key;

    @Autowired
    UserRedisCacheDao userRedisDao;

    @RequestMapping("/redisCacheTest")
    public BaseResponse getBaseResponse(HttpServletRequest request, HttpServletResponse response) {
       return userRedisDao.getBaseResponse("myCacheTest1");
    }
    
    @RequestMapping("/redisCacheTest2")
    public BaseResponse getBaseResponse2(HttpServletRequest request, HttpServletResponse response) {
       return userRedisDao.getBaseResponse2("myCacheTest2");
    }
    
    @RequestMapping("/redisCacheTest3")
    public String getBaseResponse3(HttpServletRequest request, HttpServletResponse response) {
       return userRedisDao.delete("myCacheTest1");
    }
    
    @RequestMapping("/redisCacheTest4")
    public BaseResponse getUpdatedResponse(HttpServletRequest request, HttpServletResponse response) {
       return userRedisDao.getUpdatedResponse("myCacheTest1");
    }
    // @Autowired
    // UserDao userDao;
    //
    // @Autowired
    // UserCacheDao userCacheDao;
    //
    // @Autowired
    // UserRedisDao userRedisDao;

    // @RequestMapping("/index")
    // public BaseResponse getBaseResponse(HttpServletRequest request,
    // HttpServletResponse response) {
    // userDao.getUsers().forEach((obj) -> {
    // System.out.println(obj.toString());
    // });
    // return new BaseResponse(0, key);
    // }
    //
    // @RequestMapping("/getUser")
    // public BaseResponse getUserById(HttpServletRequest request,
    // HttpServletResponse response) {
    // int id = Integer.parseInt(request.getParameter("id"));
    // userCacheDao.getUserById(id).forEach((obj) -> {
    // LogUtil.debug(obj.toString());
    // });
    // ;
    // return new BaseResponse();
    // }
    //
    // @RequestMapping("/deletecache")
    // public BaseResponse deleteUserCache(HttpServletRequest request,
    // HttpServletResponse response) {
    // int id = Integer.parseInt(request.getParameter("id"));
    // userCacheDao.deleteCache(id);
    // return new BaseResponse();
    // }
    //
    // @RequestMapping("/getredisvalue")
    // public BaseResponse getValueFromRedis(HttpServletRequest request,
    // HttpServletResponse response) {
    // String value = userRedisDao.getValue("keyaaa");
    // return new BaseResponse(0, value);
    // }
}
