

package org.mybatis.spring.sample;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Example of MyBatis-Spring integration with a DAO configured via MapperScannerConfigurer.
 */
@SpringJUnitConfig(locations = { "classpath:org/mybatis/spring/sample/config/applicationContext-namespace.xml" })
class SampleNamespaceTest extends AbstractSampleTest {
}
