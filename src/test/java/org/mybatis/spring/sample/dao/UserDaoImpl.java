

package org.mybatis.spring.sample.dao;

import org.mybatis.spring.sample.domain.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * This DAO extends SqlSessionDaoSupport and uses a Spring managed SqlSession instead of the MyBatis one. SqlSessions
 * are handled by Spring so you don't need to open/close/commit/rollback. MyBatis exceptions are translated to Spring
 * Data Exceptions.
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

  @Override
  public User getUser(String userId) {
    return getSqlSession().selectOne("org.mybatis.spring.sample.mapper.UserMapper.getUser", userId);
  }

}
