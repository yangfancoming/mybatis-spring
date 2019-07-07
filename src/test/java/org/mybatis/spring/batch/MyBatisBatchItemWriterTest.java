
package org.mybatis.spring.batch;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.domain.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

/**
 * @author Putthiphong Boonphong
 */
class MyBatisBatchItemWriterTest {

  @Mock
  private SqlSessionTemplate mockSqlSessionTemplate;

  @InjectMocks
  private MyBatisBatchItemWriter<Employee> writer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testZeroBatchResultShouldThrowException() {
    List<Employee> employees = Arrays.asList(new Employee(), new Employee());
    List<BatchResult> batchResults = Lists.emptyList();

    given(mockSqlSessionTemplate.flushStatements()).willReturn(batchResults);

    assertThrows(InvalidDataAccessResourceUsageException.class, () -> writer.write(employees));
  }

  @Test
  void testZeroUpdateCountShouldThrowException() {
    List<Employee> employees = Arrays.asList(new Employee(), new Employee());

    BatchResult batchResult = new BatchResult(null, null);
    batchResult.setUpdateCounts(new int[] { 1, 0 });
    List<BatchResult> batchResults = Collections.singletonList(batchResult);

    given(mockSqlSessionTemplate.flushStatements()).willReturn(batchResults);

    assertThrows(EmptyResultDataAccessException.class, () -> writer.write(employees));
  }

  @Test
  void testItemToParameterConverterIsDefault() {
    this.writer.setAssertUpdates(false);
    this.writer.setStatementId("updateEmployee");

    Employee employee = new Employee();
    List<Employee> employees = Collections.singletonList(employee);
    writer.write(employees);

    Mockito.verify(this.mockSqlSessionTemplate).update("updateEmployee", employee);
  }

  @Test
  void testSetItemToParameterConverter() {
    this.writer.setAssertUpdates(false);
    this.writer.setStatementId("updateEmployee");
    this.writer.setItemToParameterConverter(item -> {
      Map<String, Object> parameter = new HashMap<>();
      parameter.put("item", item);
      parameter.put("now", LocalDateTime.now(Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())));
      return parameter;
    });

    Employee employee = new Employee();
    List<Employee> employees = Collections.singletonList(employee);
    writer.write(employees);

    Map<String, Object> parameter = new HashMap<>();
    parameter.put("item", employee);
    parameter.put("now", LocalDateTime.now(Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())));
    Mockito.verify(this.mockSqlSessionTemplate).update("updateEmployee", parameter);
  }

  @Test
  void testItemToParameterConverterIsNull() {
    given(mockSqlSessionTemplate.getExecutorType()).willReturn(ExecutorType.BATCH);
    this.writer.setStatementId("updateEmployee");
    writer.setItemToParameterConverter(null);

    assertThrows(IllegalArgumentException.class, () -> writer.afterPropertiesSet(),
        "A itemToParameterConverter is required.");

  }

}
