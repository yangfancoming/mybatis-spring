
package org.mybatis.spring.submitted.autowire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class AutowireTest {

  @Test
  void shouldReturnMapper() {
    try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        "classpath:org/mybatis/spring/submitted/autowire/spring.xml")) {

      FooMapper fooMapper = (FooMapper) context.getBean("fooMapper");
      assertThat(fooMapper).isNotNull();
      fooMapper.executeFoo();

      BarMapper barMapper = (BarMapper) context.getBean("barMapper");
      assertThat(barMapper).isNotNull();
      barMapper.executeBar();
    }

  }
}
