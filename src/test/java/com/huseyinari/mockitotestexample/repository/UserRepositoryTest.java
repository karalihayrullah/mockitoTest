package com.huseyinari.mockitotestexample.repository;

import com.huseyinari.mockitotestexample.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {
  @Autowired
  private UserRepository repository;

  @Test
  public void whenGetFirstUser_thenSuccessfully() {
    Optional<User> o = repository.getFirstUser();
    User result = o.get();

    Assertions.assertTrue(o.isPresent());
    Assertions.assertEquals(1L, result.getId());
  }
  @Test
  public void whenSaveValidUser_thenSuccessfully() {
    User user = new User(null, "Test", "Test", "test@gmail.com", 50);
    User result = repository.save(user);

    Assertions.assertNotEquals(null, result);
    Assertions.assertEquals(6, result.getId());
  }
  @Test
  public void whenSaveUserWithoutNameAndSurname_thenThrowException() {
    Assertions.assertThrows(ConstraintViolationException.class, () -> {
      User user = new User(6l, null, null, "test@gmail.com", 20);
      repository.save(user);
    });
  }
  @Test
  public void whenCallFindAll_thenGetAllUsers() {
    List<User> userList = repository.findAll();

    Assertions.assertEquals(5, userList.size());
    Assertions.assertEquals(1, userList.get(0).getId());
    Assertions.assertEquals("Hüseyin", userList.get(0).getName());
    Assertions.assertEquals(5, userList.get(userList.size() - 1).getId());
  }
  @Test
  public void whenCallFindById_thenGetUser() {
    User user = repository.findById(1l).get();

    Assertions.assertNotEquals(null, user);
    Assertions.assertEquals("Hüseyin", user.getName());
    Assertions.assertEquals("Arı", user.getSurname());
  }
  @Test
  public void whenSaveExistUser_thenUpdateRecord() {
    User user = repository.findById(1l).get();

    user.setName("Updated Name");
    user.setSurname("Updated Surname");
    repository.save(user);

    Assertions.assertEquals(1l, user.getId());
    Assertions.assertEquals("Updated Name", user.getName());
    Assertions.assertEquals("Updated Surname", user.getSurname());
  }
  @Test
  public void whenCallDelete_thenDeleteRecord() {
    User user = repository.findById(1l).get();
    repository.delete(user);

    Optional<User> result = repository.findById(1l);

    Assertions.assertEquals(false, result.isPresent());
  }
}
