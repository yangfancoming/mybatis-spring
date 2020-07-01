

package org.mybatis.spring.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Test to ensure that the {@link MapperScan} annotation works as expected.
 *
 * @since 1.2.0
 */
@SpringJUnitConfig
class SampleEnableTest extends AbstractSampleTest {

  @Configuration
  @ImportResource("classpath:org/mybatis/spring/sample/config/applicationContext-infrastructure.xml")
  @MapperScan("org.mybatis.spring.sample.mapper")
  static class AppConfig {
  }
}
