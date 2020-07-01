

package org.mybatis.spring.sample.domain;

/**
 * A simple bean that holds User info.
 */
public class User {

  private String id;

  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(30);
    buf.append("{");
    buf.append(id);
    buf.append(", ");
    buf.append(name);
    buf.append("}");
    return buf.toString();
  }
}
