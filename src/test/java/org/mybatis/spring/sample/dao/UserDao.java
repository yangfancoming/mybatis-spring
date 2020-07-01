

package org.mybatis.spring.sample.dao;

import org.mybatis.spring.sample.domain.User;

public interface UserDao {
  User getUser(String userId);
}
