

package org.mybatis.spring.sample;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Example of MyBatis-Spring batch integration usage.
 */
@SpringJUnitConfig(locations = { "classpath:org/mybatis/spring/sample/config/applicationContext-batch.xml" })
class SampleBatchTest extends AbstractSampleTest {
  // Note this does not actually test batch functionality since FooService
  // only calls one DAO method. This class and associated Spring context
  // simply show that no implementation changes are needed to enable
  // different MyBatis configurations.
}
