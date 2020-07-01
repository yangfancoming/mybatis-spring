

package org.mybatis.spring.submitted.xa;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.UserTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(locations = "classpath:org/mybatis/spring/submitted/xa/applicationContext.xml")
class UserServiceTest {

  @Autowired
  UserTransaction userTransaction;

  @Autowired
  private UserService userService;

  @Test
  void testCommit() {
    User user = new User(1, "Pocoyo");
    userService.saveWithNoFailure(user);
    assertThat(userService.checkUserExists(user.getId())).isTrue();
  }

  @Test
  void testRollback() {
    User user = new User(2, "Pocoyo");
    try {
      userService.saveWithFailure(user);
    } catch (RuntimeException ignore) {
      // ignored
    }
    assertThat(userService.checkUserExists(user.getId())).isFalse();
  }

  @Test
  void testCommitWithExistingTx() throws Exception {
    userTransaction.begin();
    User user = new User(3, "Pocoyo");
    userService.saveWithNoFailure(user);
    userTransaction.commit();
    assertThat(userService.checkUserExists(user.getId())).isTrue();
  }

  // TODO when the outer JTA tx is rolledback,
  // SqlSession should be rolledback but it is committed
  // because Spring calls beforeCommmit from its TX interceptor
  // then, the JTA TX may be rolledback.
  @Test
  void testRollbackWithExistingTx() throws Exception {
    userTransaction.begin();
    User user = new User(5, "Pocoyo");
    userService.saveWithNoFailure(user);
    userTransaction.rollback();
    assertThat(userService.checkUserExists(user.getId())).isFalse();
  }

}
