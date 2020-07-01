

package org.mybatis.spring.mapper;

import org.springframework.stereotype.Component;

// annotated interface for MapperScannerPostProcessor tests
// ensures annotated classes are loaded
@Component
public interface AnnotatedMapper {
  void method();
}
