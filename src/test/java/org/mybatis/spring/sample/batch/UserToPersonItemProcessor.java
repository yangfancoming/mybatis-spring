

package org.mybatis.spring.sample.batch;

import org.mybatis.spring.sample.domain.Person;

import org.mybatis.spring.sample.domain.User;
import org.springframework.batch.item.ItemProcessor;

public class UserToPersonItemProcessor implements ItemProcessor<User, Person> {

  @Override
  public Person process(final User user) throws Exception {
    final String[] names = user.getName().split(" ");
    if (names.length == 1) {
      return new Person(names[0], null);
    } else {
      return new Person(names[0], names[1]);
    }
  }

}
