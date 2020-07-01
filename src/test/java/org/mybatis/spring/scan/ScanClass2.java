

package org.mybatis.spring.scan;

import java.util.function.Supplier;

import org.apache.ibatis.jdbc.SQL;

public class ScanClass2 {

  public static class StaticInnerClass {

  }

  public class InnerClass {

  }

  public enum InnerEnum {

  }

  public @interface InnerAnnotation {

  }

  public String createSqlUsingAnonymousClass() {
    return new SQL() {
      {
        SELECT("a");
        FROM("test2");
      }
    }.toString();
  }

  public Supplier<String> createSqlSupplier() {
    return () -> "SELECT a FROM test2";
  }

}
