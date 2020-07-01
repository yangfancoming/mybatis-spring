

package org.mybatis.spring.sample.service;

import org.mybatis.spring.sample.domain.User;
import org.mybatis.spring.sample.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * FooService simply receives a userId and uses a mapper to get a record from the database.
 */
@Transactional
public class FooService {

  private final UserMapper userMapper;

  public FooService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public User doSomeBusinessStuff(String userId) {
    return this.userMapper.getUser(userId);
  }

}
