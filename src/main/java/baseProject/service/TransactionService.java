package baseProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baseProject.dao.UserDao;

/**
 * 
 * 事务控制采用动态代理实现，类内部如果非事务方法调用事务方法会导致事务失效 推荐每个事务操作直接调用事务方法
 */
@Service
@Transactional
// @Transactional(rollbackFor=Exception.class)这样注解表示抛出一般异常也进行回滚
public class TransactionService {
    @Autowired
    UserDao userDao;

    @Transactional
    public void deleteUserIfName(int id, String username) {
        userDao.deteleUserByUserName(username);
        if (id == 1) {
            throw new RuntimeException();
        }
    }

}
