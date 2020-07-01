

package org.mybatis.spring.asyncsynchronization;

import org.jboss.byteman.contrib.bmunit.BMRule;
import org.junit.jupiter.api.Disabled;
import org.mybatis.spring.SqlSessionTemplateTest;

/**
 * 
 * The same test as original but afterCompletion is being called on a separate thread
 * 
 * @author Alex Rykov
 *
 */
@Disabled // FIXME: Enable after migrate BMUnitRunner to BMUnitExtension
// @ExtendWith(BMUnitRunner.class)
@BMRule(name = "proxy synchronizations", targetClass = "TransactionSynchronizationManager", targetMethod = "registerSynchronization(TransactionSynchronization)", helper = "org.mybatis.spring.asyncsynchronization.AsyncAfterCompletionHelper", action = "$1=createSynchronizationWithAsyncAfterComplete($1)")
class SqlSessionTemplateAsyncAfterCompletionTest extends SqlSessionTemplateTest {
}
