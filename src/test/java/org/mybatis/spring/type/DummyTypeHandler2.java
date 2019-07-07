
package org.mybatis.spring.type;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(BigDecimal.class)
public class DummyTypeHandler2 implements TypeHandler<Object> {

  @Override
  public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
  }

  @Override
  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    return null;
  }

  @Override
  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return null;
  }

  @Override
  public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
    return null;
  }

}
