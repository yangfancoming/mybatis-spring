

package org.mybatis.spring.type;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyMapperFactoryBean<T> extends MapperFactoryBean<T> {

  public DummyMapperFactoryBean() {
    super();
  }

  public DummyMapperFactoryBean(Class<T> mapperInterface) {
    super(mapperInterface);
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(DummyMapperFactoryBean.class);

  private static final AtomicInteger mapperInstanceCount = new AtomicInteger(0);

  @Override
  protected void checkDaoConfig() {
    super.checkDaoConfig();
    // make something more
    if (isAddToConfig()) {
      LOGGER.debug(() -> "register mapper for interface : " + getMapperInterface());
    }
  }

  @Override
  public T getObject() throws Exception {
    MapperFactoryBean<T> mapperFactoryBean = new MapperFactoryBean<>();
    mapperFactoryBean.setMapperInterface(getMapperInterface());
    mapperFactoryBean.setAddToConfig(isAddToConfig());
    mapperFactoryBean.setSqlSessionFactory(getCustomSessionFactoryForClass());
    T object = mapperFactoryBean.getObject();
    mapperInstanceCount.incrementAndGet();
    return object;
  }

  private SqlSessionFactory getCustomSessionFactoryForClass() {
    // can for example read a custom annotation to set a custom sqlSessionFactory

    // just a dummy implementation example
    return (SqlSessionFactory) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),
        new Class[] { SqlSessionFactory.class }, (proxy, method, args) -> {
          if ("getConfiguration".equals(method.getName())) {
            return getSqlSession().getConfiguration();
          }
          // dummy
          return null;
        });
  }

  public static int getMapperCount() {
    return mapperInstanceCount.get();
  }

  public static void clear() {
    mapperInstanceCount.set(0);
  }

}
