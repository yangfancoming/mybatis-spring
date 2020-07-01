

package org.mybatis.spring.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

public interface TypeHandlerFactory {

  static TypeHandler<String> handler1() {
    return new TypeHandler<String>() {
      @Override
      public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {

      }

      @Override
      public String getResult(ResultSet rs, String columnName) {
        return null;
      }

      @Override
      public String getResult(ResultSet rs, int columnIndex) {
        return null;
      }

      @Override
      public String getResult(CallableStatement cs, int columnIndex) {
        return null;
      }
    };
  }

  static TypeHandler<UUID> handler2() {
    return new InnerTypeHandler();
  }

  @MappedTypes({ UUID.class })
  class InnerTypeHandler implements TypeHandler<UUID> {

    @Override
    public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) {
    }

    @Override
    public UUID getResult(ResultSet rs, String columnName) {
      return null;
    }

    @Override
    public UUID getResult(ResultSet rs, int columnIndex) {
      return null;
    }

    @Override
    public UUID getResult(CallableStatement cs, int columnIndex) {
      return null;
    }

  }

}
