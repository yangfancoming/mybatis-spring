

package org.mybatis.spring.sample.service;

import org.mybatis.spring.sample.dao.UserDao;
import org.mybatis.spring.sample.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * BarService simply receives a userId and uses a dao to get a record from the database.
 */
@Transactional
public class BarService {

  private final UserDao userDao;

  public BarService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User doSomeBusinessStuff(String userId) {
    return this.userDao.getUser(userId);
  }

}
