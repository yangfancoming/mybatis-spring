

/**
 * MyBatis @Configuration style sample 
 */
package org.mybatis.spring.sample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.sample.config.SampleConfig;
import org.mybatis.spring.sample.domain.User;
import org.mybatis.spring.sample.service.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = SampleConfig.class)
class SampleJavaConfigTest {

  @Autowired
  private FooService fooService;

  @Autowired
  private FooService fooServiceWithMapperFactoryBean;

  @Test
  void test() {
    User user = fooService.doSomeBusinessStuff("u1");
    assertThat(user.getName()).isEqualTo("Pocoyo");
  }

  @Test
  void testWithMapperFactoryBean() {
    User user = fooServiceWithMapperFactoryBean.doSomeBusinessStuff("u1");
    assertThat(user.getName()).isEqualTo("Pocoyo");
  }

}
