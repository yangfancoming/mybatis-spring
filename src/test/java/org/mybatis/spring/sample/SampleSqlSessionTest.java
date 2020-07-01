

package org.mybatis.spring.sample;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.sample.domain.User;
import org.mybatis.spring.sample.service.BarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example of basic MyBatis-Spring integration usage with a manual DAO implementation that subclasses
 * SqlSessionDaoSupport.
 */
@DirtiesContext
@SpringJUnitConfig(locations = { "classpath:org/mybatis/spring/sample/config/applicationContext-sqlsession.xml" })
class SampleSqlSessionTest {

  @Autowired
  private BarService barService;

  @Test
  final void testFooService() {
    User user = this.barService.doSomeBusinessStuff("u1");
    assertThat(user).isNotNull();
    assertThat(user.getName()).isEqualTo("Pocoyo");
  }

}
