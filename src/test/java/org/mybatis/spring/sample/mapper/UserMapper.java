

package org.mybatis.spring.sample.mapper;

import org.mybatis.spring.sample.domain.User;

/**
 * A org.mybatis.spring sample mapper. This interface will be used by MapperFactoryBean to create a proxy implementation
 * at Spring application startup.
 */
public interface UserMapper {

  User getUser(String userId);

}
