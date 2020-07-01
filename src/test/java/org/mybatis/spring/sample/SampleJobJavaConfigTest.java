

package org.mybatis.spring.sample;

import org.mybatis.spring.sample.config.SampleJobConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({ SampleJobConfig.class, AbstractSampleJobTest.LocalContext.class })
class SampleJobJavaConfigTest extends AbstractSampleJobTest {
  @Override
  protected String getExpectedOperationBy() {
    return "batch_java_config_user";
  }
}
