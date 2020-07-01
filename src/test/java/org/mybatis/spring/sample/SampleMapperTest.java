

package org.mybatis.spring.sample;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Example of MyBatis-Spring integration with a DAO created by MapperFactoryBean.
 */
@SpringJUnitConfig(locations = { "classpath:org/mybatis/spring/sample/config/applicationContext-mapper.xml" })
class SampleMapperTest extends AbstractSampleTest {
}
