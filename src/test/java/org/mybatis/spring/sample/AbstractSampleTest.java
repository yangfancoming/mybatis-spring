

package org.mybatis.spring.sample;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.sample.domain.User;
import org.mybatis.spring.sample.service.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
abstract class AbstractSampleTest {

  @Autowired
  protected FooService fooService;

  @Test
  final void testFooService() {
    User user = this.fooService.doSomeBusinessStuff("u1");
    assertThat(user).isNotNull();
    assertThat(user.getName()).isEqualTo("Pocoyo");
  }

}
