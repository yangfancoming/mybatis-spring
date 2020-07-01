

package org.mybatis.spring.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.mapper.ds1.Ds1Mapper;
import org.mybatis.spring.mapper.AnnotatedMapper;
import org.mybatis.spring.mapper.MapperInterface;
import org.mybatis.spring.mapper.MapperSubinterface;
import org.mybatis.spring.mapper.child.MapperChildInterface;
import org.mybatis.spring.type.DummyMapperFactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mockrunner.mock.jdbc.MockDataSource;

/**
 * Test for the MapperScannerRegistrar. This test works fine with Spring 3.1 and 3.2 but with 3.1 the registrar is
 * called twice.
 */
class MapperScanTest {
  private AnnotationConfigApplicationContext applicationContext;

  @BeforeEach
  void setupContext() {
    applicationContext = new AnnotationConfigApplicationContext();

    setupSqlSessionFactory();

    // assume support for autowiring fields is added by MapperScannerConfigurer
    // via
    // org.springframework.context.annotation.ClassPathBeanDefinitionScanner.includeAnnotationConfig
  }

  private void startContext() {
    applicationContext.refresh();
    applicationContext.start();

    // this will throw an exception if the beans cannot be found
    applicationContext.getBean("sqlSessionFactory");
  }

  @AfterEach
  void assertNoMapperClass() {
    try {
      // concrete classes should always be ignored by MapperScannerPostProcessor
      assertBeanNotLoaded("mapperClass");

      // no method interfaces should be ignored too
      assertBeanNotLoaded("package-info");
      // assertBeanNotLoaded("annotatedMapperZeroMethods"); // as of 1.1.0 mappers
      // with no methods are loaded
    } finally {
      applicationContext.close();
    }
  }

  @Test
  void testInterfaceScan() {
    applicationContext.register(AppConfigWithPackageScan.class);

    startContext();

    // all interfaces with methods should be loaded
    applicationContext.getBean("mapperInterface");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");
    applicationContext.getBean("annotatedMapper");
  }

  @Test
  void testInterfaceScanWithPackageClasses() {
    applicationContext.register(AppConfigWithPackageClasses.class);

    startContext();

    // all interfaces with methods should be loaded
    applicationContext.getBean("mapperInterface");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");
    applicationContext.getBean("annotatedMapper");
  }

  @Test
  void testNameGenerator() {
    applicationContext.register(AppConfigWithNameGenerator.class);

    startContext();

    // only child inferfaces should be loaded and named with its class name
    applicationContext.getBean(MapperInterface.class.getName());
    applicationContext.getBean(MapperSubinterface.class.getName());
    applicationContext.getBean(MapperChildInterface.class.getName());
    applicationContext.getBean(AnnotatedMapper.class.getName());
  }

  @Test
  void testMarkerInterfaceScan() {
    applicationContext.register(AppConfigWithMarkerInterface.class);

    startContext();

    // only child inferfaces should be loaded
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");

    assertBeanNotLoaded("mapperInterface");
    assertBeanNotLoaded("annotatedMapper");
  }

  @Test
  void testAnnotationScan() {
    applicationContext.register(AppConfigWithAnnotation.class);

    startContext();

    // only annotated mappers should be loaded
    applicationContext.getBean("annotatedMapper");
    applicationContext.getBean("mapperChildInterface");

    assertBeanNotLoaded("mapperInterface");
    assertBeanNotLoaded("mapperSubinterface");
  }

  @Test
  void testMarkerInterfaceAndAnnotationScan() {
    applicationContext.register(AppConfigWithMarkerInterfaceAndAnnotation.class);

    startContext();

    // everything should be loaded but the marker interface
    applicationContext.getBean("annotatedMapper");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");

    assertBeanNotLoaded("mapperInterface");
  }

  @Test
  void testCustomMapperFactoryBean() {
    DummyMapperFactoryBean.clear();
    applicationContext.register(AppConfigWithCustomMapperFactoryBean.class);

    startContext();

    // all interfaces with methods should be loaded
    applicationContext.getBean("mapperInterface");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");
    applicationContext.getBean("annotatedMapper");

    assertTrue(DummyMapperFactoryBean.getMapperCount() > 0);

  }

  @Test
  void testScanWithNameConflict() {
    GenericBeanDefinition definition = new GenericBeanDefinition();
    definition.setBeanClass(Object.class);
    applicationContext.registerBeanDefinition("mapperInterface", definition);

    applicationContext.register(AppConfigWithPackageScan.class);

    startContext();

    assertThat(applicationContext.getBean("mapperInterface").getClass())
        .as("scanner should not overwrite existing bean definition").isSameAs(Object.class);
  }

  private void setupSqlSessionFactory() {
    GenericBeanDefinition definition = new GenericBeanDefinition();
    definition.setBeanClass(SqlSessionFactoryBean.class);
    definition.getPropertyValues().add("dataSource", new MockDataSource());
    applicationContext.registerBeanDefinition("sqlSessionFactory", definition);
  }

  private void assertBeanNotLoaded(String name) {
    try {
      applicationContext.getBean(name);
      fail("Spring bean should not be defined for class " + name);
    } catch (NoSuchBeanDefinitionException nsbde) {
      // success
    }
  }

  @Test
  void testScanWithExplicitSqlSessionFactory() {
    applicationContext.register(AppConfigWithSqlSessionFactory.class);

    startContext();

    // all interfaces with methods should be loaded
    applicationContext.getBean("mapperInterface");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");
    applicationContext.getBean("annotatedMapper");
  }

  @Test
  void testScanWithExplicitSqlSessionTemplate() {
    GenericBeanDefinition definition = new GenericBeanDefinition();
    definition.setBeanClass(SqlSessionTemplate.class);
    ConstructorArgumentValues constructorArgs = new ConstructorArgumentValues();
    constructorArgs.addGenericArgumentValue(new RuntimeBeanReference("sqlSessionFactory"));
    definition.setConstructorArgumentValues(constructorArgs);
    applicationContext.registerBeanDefinition("sqlSessionTemplate", definition);

    applicationContext.register(AppConfigWithSqlSessionTemplate.class);

    startContext();

    // all interfaces with methods should be loaded
    applicationContext.getBean("mapperInterface");
    applicationContext.getBean("mapperSubinterface");
    applicationContext.getBean("mapperChildInterface");
    applicationContext.getBean("annotatedMapper");

  }

  @Test
  void testScanWithMapperScanIsRepeat() {
    applicationContext.register(AppConfigWithMapperScanIsRepeat.class);

    startContext();

    applicationContext.getBean("ds1Mapper");
    applicationContext.getBean("ds2Mapper");
  }

  @Test
  void testScanWithMapperScans() {
    applicationContext.register(AppConfigWithMapperScans.class);

    startContext();

    SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
    assertEquals(2, sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers().size());

    applicationContext.getBean("ds1Mapper");
    applicationContext.getBean("ds2Mapper");
  }

  @Test
  void testLazyScanWithPropertySourcesPlaceholderConfigurer() {
    applicationContext.register(LazyConfigWithPropertySourcesPlaceholderConfigurer.class);

    startContext();

    SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
    assertEquals(0, sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers().size());
    applicationContext.getBean(Ds1Mapper.class);
    assertEquals(1, sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers().size());

  }

  @Test
  void testLazyConfigWithPropertySource() {
    applicationContext.register(LazyConfigWithPropertySource.class);

    startContext();

    SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
    assertEquals(0, sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers().size());
    applicationContext.getBean(Ds1Mapper.class);
    assertEquals(1, sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers().size());

  }

  @Configuration
  @MapperScan("org.mybatis.spring.mapper")
  public static class AppConfigWithPackageScan {
  }

  @Configuration
  @MapperScan(basePackageClasses = MapperInterface.class)
  public static class AppConfigWithPackageClasses {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", markerInterface = MapperInterface.class)
  public static class AppConfigWithMarkerInterface {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", annotationClass = Component.class)
  public static class AppConfigWithAnnotation {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", annotationClass = Component.class, markerInterface = MapperInterface.class)
  public static class AppConfigWithMarkerInterfaceAndAnnotation {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
  public static class AppConfigWithSqlSessionTemplate {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
  public static class AppConfigWithSqlSessionFactory {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", nameGenerator = MapperScanTest.BeanNameGenerator.class)
  public static class AppConfigWithNameGenerator {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.mapper", factoryBean = DummyMapperFactoryBean.class)
  public static class AppConfigWithCustomMapperFactoryBean {
  }

  @Configuration
  @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds1")
  @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds2")
  public static class AppConfigWithMapperScanIsRepeat {
  }

  @Configuration
  @MapperScans({ @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds1"),
      @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds2") })
  public static class AppConfigWithMapperScans {
  }

  @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds1", lazyInitialization = "${mybatis.lazy-initialization:false}")
  public static class LazyConfigWithPropertySourcesPlaceholderConfigurer {
    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
      configurer.setLocation(new ClassPathResource("/org/mybatis/spring/annotation/scan.properties"));
      return configurer;
    }

  }

  @MapperScan(basePackages = "org.mybatis.spring.annotation.mapper.ds1", lazyInitialization = "${mybatis.lazy-initialization:false}")
  @PropertySource("classpath:/org/mybatis/spring/annotation/scan.properties")
  public static class LazyConfigWithPropertySource {

  }

  public static class BeanNameGenerator implements org.springframework.beans.factory.support.BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry definitionRegistry) {
      return beanDefinition.getBeanClassName();
    }

  }

}
