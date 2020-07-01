

package org.mybatis.spring.sample;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({ AbstractSampleJobTest.LocalContext.class, SampleJobXmlConfigTest.LocalContext.class })
class SampleJobXmlConfigTest extends AbstractSampleJobTest {

  @Override
  protected String getExpectedOperationBy() {
    return "batch_xml_config_user";
  }

  @ImportResource("classpath:org/mybatis/spring/sample/config/applicationContext-job.xml")
  @Configuration
  static class LocalContext {
  }

}
