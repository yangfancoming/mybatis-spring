

package org.mybatis.spring.mapper;

import org.springframework.stereotype.Component;

// annotated interface for MapperScannerPostProcessor tests
// ensures annotated class with no methods is not loaded
@Component
public interface AnnotatedMapperZeroMethods {
}
