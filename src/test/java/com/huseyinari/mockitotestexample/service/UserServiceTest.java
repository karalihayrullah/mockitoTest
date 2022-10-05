package com.huseyinari.mockitotestexample.service;

import com.huseyinari.mockitotestexample.dto.UserDTO;
import com.huseyinari.mockitotestexample.entity.User;
import com.huseyinari.mockitotestexample.exception.EntityNotFoundException;
import com.huseyinari.mockitotestexample.mapper.UserMapper;
import com.huseyinari.mockitotestexample.mapper.UserMapperImpl;
import com.huseyinari.mockitotestexample.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@ExtendWith(MockitoExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
  private static Validator validator;
  private List<UserDTO> userDTOList = new ArrayList<>();
  private List<User> users = new ArrayList<>();

  @InjectMocks
  private UserService service;
  @Mock
  private UserRepository repository;
  @Mock
  private MailService mailService;
  @Spy
  private UserMapperImpl mapper;

  @BeforeAll
  public static void beforeAll() {
    System.out.println("---------------------- before all -----------------");
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }
  @BeforeEach
  public void beforeEach() {
    System.out.println("---------------------- before each -----------------");

    UserDTO userDTO = new UserDTO(1L, "Hüseyin", "Arı", "hsynari1060@gmail.com", 23);
    UserDTO userDTO1 = new UserDTO(2L, "Mustafa", "Arı", "mustafa@gmail.com", 18);
    UserDTO userDTO2 = new UserDTO(3L, "Mehmet Akif", "Pişkiner", "mehmetakif@gmail.com", 30);
    userDTOList = Arrays.asList(userDTO, userDTO1, userDTO2);

    users = Arrays.asList(mapper.toEntity(userDTO), mapper.toEntity(userDTO1), mapper.toEntity(userDTO2));
  }
  @AfterEach
  public void afterEach() {
    System.out.println("---------------------- after each -----------------");
  }
  @AfterAll
  public static void afterAll() {
    System.out.println("------------------------------ after all -----------------------------");
  }

  @RepeatedTest(5)
  @DisplayName("Tekrar edilecek test")
  public void repeatedTest() {
    Assertions.assertEquals(1, 1);
    Assertions.assertNotEquals(1, 2);
    Assertions.assertTrue(true);
    Assertions.assertFalse(false);
    Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, new int[]{1, 2, 3, 4, 5});
    // Assertions.assertSame();
    // Assertions.assertThrows()
  }
  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5, 7})
  void isOdd_TrueForOddNumbers(int number) {
    Assertions.assertTrue(number % 2 == 1);
  }
  @Test
  public void whenGetAll_thenGetAllUsers() {
    Mockito.when(repository.findAll()).thenReturn(users);

    List<UserDTO> result = service.getAll();

    Assertions.assertEquals(users.size(), result.size());
    Assertions.assertEquals(users.get(0).getId(), result.get(0).getId());
  }
  @Test
  public void whenGetOneWithoutExistUser_thenThrowEntityNotFoundException() {
    Mockito.when(repository.findById(6L)).thenReturn(Optional.empty());

    EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.getOne(6L));
    Assertions.assertEquals("6 id'li kayıt bulunamadı !", exception.getMessage());
  }
  @Test
  public void whenGetOneWithExistUserId_thenReturnUser() {
    User user = new User(1L, "Hüseyin", "Arı", "hsynari1060@gmail.com", 23);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(user));

    UserDTO result = service.getOne(1L);

    Assertions.assertAll(
            () -> Assertions.assertEquals(user.getId(), result.getId()),
            () -> Assertions.assertEquals(user.getName(), result.getName()),
            () -> Assertions.assertEquals(user.getSurname(), result.getSurname()),
            () -> Assertions.assertEquals(user.getEmail(), result.getEmail()),
            () -> Assertions.assertEquals(user.getAge(), result.getAge())
    );
  }
  @Test
  public void whenCreateWithId_thenThrowException() {
    UserDTO userDTO = new UserDTO(1L, "Test", "Test", "test@gmail.com", 20);

    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> service.create(userDTO));
    Assertions.assertEquals("Yeni kaydedilecek kullanıcı id içeremez.", exception.getMessage());
  }
  @Test
  public void whenCreateValidUser_thenSuccessfully() {
    UserDTO userDTO = new UserDTO(null, "Test", "Test", "test@gmail.com", 20);
    User user = mapper.toEntity(userDTO);
    user.setId(5L);

    Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
    Mockito.doNothing().when(mailService).sendEMail(Mockito.anyString(), Mockito.anyString());

    Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
    UserDTO result = null;
    if (violations.isEmpty())
      result = service.create(userDTO);

    Assertions.assertEquals(user.getId(), result.getId());
    Assertions.assertEquals(user.getName(), result.getName());
    Assertions.assertEquals(user.getSurname(), result.getSurname());
    Assertions.assertEquals(user.getAge(), result.getAge());
    Assertions.assertEquals(user.getEmail(), result.getEmail());
    Mockito.verify(mailService, Mockito.times(1)).sendEMail(Mockito.anyString(), Mockito.anyString());
  }
  @Test
  @Disabled
  public void whenUpdateWithoutId_thenThrowException() {
    UserDTO userDTO = new UserDTO(null, "Test", "Test", "test@gmail.com", 20);

    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> service.update(userDTO));
    Assertions.assertEquals("Güncellenecek kullanıcının id'si bulunmalıdır.", exception.getMessage());
  }
  @Test
  @Disabled
  public void whenUpdateUserWithAllFields_thenSuccessfully() {
    User user = new User(1L, "Hüseyin", "Arı", "hsynari1060@gmail.com", 23);
    UserDTO userDTO = new UserDTO(1L, "Test", "Test", "test@gmail.com", 20);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(user));

    UserDTO result = service.update(userDTO);

    Assertions.assertAll(
            () -> Assertions.assertEquals(userDTO.getName(), result.getName()),
            () -> Assertions.assertEquals(userDTO.getSurname(), result.getSurname()),
            () -> Assertions.assertEquals(userDTO.getEmail(), result.getEmail()),
            () -> Assertions.assertEquals(userDTO.getAge(), result.getAge())
    );
  }
  @Test
  @Disabled
  public void whenUpdateUserWithoutNameAndSurname_thenSuccessfully() {
    User user = new User(1L, "Hüseyin", "Arı", "hsynari1060@gmail.com", 23);
    UserDTO userDTO = new UserDTO(1L, null, null, "test@gmail.com", 20);

    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(user));

    UserDTO result = service.update(userDTO);

    Assertions.assertAll(
            () -> Assertions.assertEquals(user.getName(), result.getName()),
            () -> Assertions.assertEquals(user.getSurname(), result.getSurname()),
            () -> Assertions.assertEquals(userDTO.getEmail(), result.getEmail()),
            () -> Assertions.assertEquals(userDTO.getAge(), result.getAge())
    );
  }
  @Test
  public void whenDeleteUser_thenSuccessfully() {
    User user = new User(1L, "Hüseyin", "Arı", "hsynari1060@gmail.com", 23);
    Mockito.when(repository.findById(1L)).thenReturn(Optional.of(user));

    service.delete(user.getId());

    Mockito.verify(repository, Mockito.times(1)).deleteById(user.getId());
  }

}
