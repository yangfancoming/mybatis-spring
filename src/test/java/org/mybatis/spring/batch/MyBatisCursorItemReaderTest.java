

package org.mybatis.spring.batch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link MyBatisCursorItemReader}.
 */
class MyBatisCursorItemReaderTest {

  @Mock
  private SqlSessionFactory sqlSessionFactory;

  @Mock
  private SqlSession sqlSession;

  @Mock
  private Cursor<Object> cursor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testCloseOnFailing() throws Exception {

    Mockito.when(this.sqlSessionFactory.openSession(ExecutorType.SIMPLE)).thenReturn(this.sqlSession);
    Mockito.when(this.cursor.iterator()).thenReturn(getFoos().iterator());
    Mockito.when(this.sqlSession.selectCursor("selectFoo", Collections.singletonMap("id", 1)))
        .thenThrow(new RuntimeException("error."));

    MyBatisCursorItemReader<Foo> itemReader = new MyBatisCursorItemReader<>();
    itemReader.setSqlSessionFactory(this.sqlSessionFactory);
    itemReader.setQueryId("selectFoo");
    itemReader.setParameterValues(Collections.singletonMap("id", 1));
    itemReader.afterPropertiesSet();

    ExecutionContext executionContext = new ExecutionContext();
    try {
      itemReader.open(executionContext);
      fail();
    } catch (ItemStreamException e) {
      Assertions.assertThat(e).hasMessage("Failed to initialize the reader").hasCause(new RuntimeException("error."));
    } finally {
      itemReader.close();
      Mockito.verify(this.sqlSession).close();
    }

  }

  @Test
  void testCloseBeforeOpen() {
    MyBatisCursorItemReader<Foo> itemReader = new MyBatisCursorItemReader<>();
    itemReader.close();
  }

  private List<Object> getFoos() {
    return Arrays.asList(new Foo("foo1"), new Foo("foo2"), new Foo("foo3"));
  }

  private static class Foo {
    private final String name;

    Foo(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

}
