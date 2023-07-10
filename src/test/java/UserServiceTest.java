import org.junit.jupiter.api.*;
import step.project.user.user.User;
import step.project.user.user.userDAO.UserService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    private UserService userService;

    @BeforeAll
    public void initUserService() {
        this.userService = new UserService("testUserService.spfb");
        this.userService.createRandomUsers(1);
    }

    @Test
    @Order(1)
    public void getAllUsersTest() {
        //Проверяем, возвращаемый объект нужного класса и не пустой
        assertThat(this.userService.getAllUsers()).isInstanceOf(Map.class).isNotEmpty();
    }

    @Test
    @Order(2)
    public void createRandomUsers() {
        int userCount = this.userService.getAllUsers().values().size();
        this.userService.createRandomUsers(50);
        //Проверяем что в список добавлено 50 объектов
        assertThat(this.userService.getAllUsers().size()).isEqualTo(userCount + 50);
    }

    @Test
    @Order(3)
    public void saveUserTest() {
        this.userService.userDAO.getAllUsers().clear();
        this.userService.getAllUsers().put("Феодосий Апоплексиев".toUpperCase(), new User("Феодосий Апоплексиев", "534534TR"));
        String id = this.userService.saveUser("Феодосий Апоплексиев", "534534TR", "3243724GG");
        //Проверяем текущий пароль пользователя
        assertThat(this.userService.getAllUsers().get("Феодосий Апоплексиев".toUpperCase()).checkPassword("3243724GG")).isEqualTo(id);
    }

    @Test
    @Order(4)
    public void getUserByFullNameTest() {
        String id = this.userService.getAllUsers().get("Феодосий Апоплексиев".toUpperCase()).getId();
        assertThat(this.userService.getUserByFullName("Феодосий Апоплексиев")).isInstanceOf(User.class);
        assertThat(this.userService.getUserByFullName("Феодосий Апоплексиев").getId()).isEqualTo(id);
    }

    @Test
    @Order(5)
    public void getUserIDByFullNameTest() {
        User sampleJunior = new User("Аникей Кейбордович");
        String userId = sampleJunior.getId();
        this.userService.getAllUsers().put(sampleJunior.getFullName().toUpperCase(), sampleJunior);
        assertThat(this.userService.getUserIDByFullName(sampleJunior.getFullName())).isEqualTo(userId);
    }

    @Test
    @Order(6)
    public void getUserFullNameByIDTest() {
        String id = this.userService.getUserIDByFullName("Аникей Кейбордович");
        assertThat(this.userService.getUserFullNameById(id)).isEqualTo("Аникей Кейбордович");
    }

    @Test
    @Order(7)
    public void addBookingIDsToUsersByIDs() {
        List<String> userIds = new ArrayList<>();
        userIds.add(this.userService.getUserIDByFullName("Аникей Кейбордович"));
        userIds.add(this.userService.getUserIDByFullName("Феодосий Апоплексиев"));
        User sample = new User("Апроксиматор Синергичный");
        this.userService.getAllUsers().put(sample.getFullName().toUpperCase(), sample);
        this.userService.addBookingIDsToUsersByIDs(userIds, "Мегабронь");
        //Проверяем что все пользователи с соответствующим ID имеют соответсвующую запись о броне
        for (String id : userIds) {
            assertThat(this.userService.getUserBookingIDs(id, true).contains("Мегабронь")).isTrue();
        }
        //Проверяем что у других пользователей такой записи нет
        for (User user : this.userService.getAllUsers().values().stream().filter(u -> !userIds.contains(u.getId())).toList())
            assertThat(user.getBookingsSet().contains("Мегабронь")).isFalse();
    }

    @Test
    @Order(8)
    public void getUserBookingTest() {
        Set<String> tempSet = new HashSet<>();
        tempSet.add("PARIS-MARS#56574");
        tempSet.add("ALFACENTAURI-EYEOFTERROR#4534");
        tempSet.add("KYIV-WARSAW#456549");
        this.userService.getAllUsers().get("Аникей Кейбордович".toUpperCase()).getBookingsSet().clear();
        tempSet.forEach(t -> this.userService.getAllUsers().get("Аникей Кейбордович".toUpperCase()).addBooking(t));
        //Проверяем что метод возвращает прогнозируемый сет значений
        assertThat(this.userService.getUserBookingIDs(this.userService.getUserIDByFullName("Аникей Кейбордович"), true)).isEqualTo(tempSet);
    }

    @Test
    @Order(9)
    public void isRegisteredUser() {
        //Проверяем что метод возвращает корректные логические значения
        assertThat(this.userService.isRegisteredUser("Феодосий Апоплексиев")).isTrue();
        assertThat(this.userService.isRegisteredUser("Аникей Кейбордович")).isFalse();
    }

    @Test
    @Order(10)
    public void checkUserPassword() {
        String userId1 = this.userService.getAllUsers().get("Феодосий Апоплексиев".toUpperCase()).getId();
        //Проверяем что метод возвращает правильное значение
        assertThat(this.userService.checkUserPassword("Феодосий Апоплексиев","3243724GG")).isEqualTo(userId1);
    }

    @Test
    @Order(11)
    public void saveLoadTest() {
        this.userService.createRandomUsers(20);
        int userCount = this.userService.getAllUsers().size();
        Path userSource = Paths.get("testUserService.spfb");
        this.userService.saveData();
        //Проверяем что файл создан
        assertThat(userSource.toFile().exists()).isTrue();
        //Очищаем текущий список
        this.userService.getAllUsers().clear();
        //Проверяем что он пуст
        assertThat(this.userService.getAllUsers().isEmpty()).isTrue();
        this.userService.loadData();
        //Проверяем что после работы метода список не пустой и кол-во записей равно кол-ов сохраненных ранее записей
        assertThat(this.userService.getAllUsers().isEmpty()).isFalse();
        assertThat(this.userService.getAllUsers().size()).isEqualTo(userCount);
        //Проверяем что один из ранее сохраненных пользователей загружен в список
        assertThat(this.userService.getAllUsers().containsValue(this.userService.getAllUsers().get("Апроксиматор Синергичный".toUpperCase()))).isTrue();
        //Удаляем тестовую базу
        File userBase = userSource.toFile();
        userBase.delete();
    }
}

