

package org.mybatis.spring.mapper.child;

import org.mybatis.spring.mapper.MapperInterface;
import org.springframework.stereotype.Component;

// interface for MapperScannerPostProcessor tests
// tests subpackage search
@Component
public interface MapperChildInterface extends MapperInterface {
  void childMethod();
}
