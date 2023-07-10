import org.junit.jupiter.api.*;
import step.project.user.user.User;
import step.project.user.user.userDAO.UserController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private UserController userController;

   @BeforeAll
    public void initUserController() {
        this.userController = new UserController("testUserController.spfb");
        this.userController.createRandomUsers(1);
    }

    @Test
    @Order(1)
    public void getAllUsersTest() {
        //Проверяем, возвращаемый объект нужного класса и не пустой
        assertThat(this.userController.userService.getAllUsers()).isInstanceOf(Map.class).isNotEmpty();
    }

    @Test
    @Order(2)
    public void createRandomUsers() {
        int userCount = this.userController.userService.getAllUsers().values().size();
        this.userController.createRandomUsers(50);
        //Проверяем что в список добавлено 50 объектов
        assertThat(this.userController.userService.getAllUsers().size()).isEqualTo(userCount + 50);
    }

    @Test
    @Order(3)
    public void saveUserTest() {
        this.userController.userService.getAllUsers().clear();
        this.userController.userService.getAllUsers().put("Феодосий Апоплексиев".toUpperCase(), new User("Феодосий Апоплексиев", "534534TR"));
        String id = this.userController.saveUser("Феодосий Апоплексиев", "534534TR", "3243724GG");
        //Проверяем текущий пароль пользователя
        assertThat(this.userController.userService.getAllUsers().get("Феодосий Апоплексиев".toUpperCase()).checkPassword("3243724GG")).isEqualTo(id);
    }

    @Test
    @Order(5)
    public void getUserIDByFullNameTest() {
        User sampleJunior = new User("Аникей Кейбордович");
        String userId = sampleJunior.getId();
        this.userController.userService.getAllUsers().put(sampleJunior.getFullName().toUpperCase(), sampleJunior);
        assertThat(this.userController.getUserIDByFullName(sampleJunior.getFullName())).isEqualTo(userId);
    }

    @Test
    @Order(6)
    public void getUserFullNameByIDTest() {
        String id = this.userController.getUserIDByFullName("Аникей Кейбордович");
        assertThat(this.userController.getUserFullNameById(id)).isEqualTo("Аникей Кейбордович");
    }

    @Test
    @Order(7)
    public void addBookingIDsToUsersByIDs() {
        List<String> userIds = new ArrayList<>();
        userIds.add(this.userController.getUserIDByFullName("Аникей Кейбордович"));
        userIds.add(this.userController.getUserIDByFullName("Феодосий Апоплексиев"));
        User sample = new User("Апроксиматор Синергичный");
        this.userController.userService.getAllUsers().put(sample.getFullName().toUpperCase(), sample);
        this.userController.addBookingIDsToUsersByIDs(userIds, "Мегабронь");
        //Проверяем что все пользователи с соответствующим ID имеют соответсвующую запись о броне
        for (String id : userIds) {
            assertThat(this.userController.getUserBookingIDs(id, true).contains("Мегабронь")).isTrue();
        }
        //Проверяем что у других пользователей такой записи нет
        for (User user : this.userController.userService.getAllUsers().values().stream().filter(u -> !userIds.contains(u.getId())).toList())
            assertThat(user.getBookingsSet().contains("Мегабронь")).isFalse();
    }

    @Test
    @Order(8)
    public void getUserBookingTest() {
        Set<String> tempSet = new HashSet<>();
        tempSet.add("PARIS-MARS#56574");
        tempSet.add("ALFACENTAURI-EYEOFTERROR#4534");
        tempSet.add("KYIV-WARSAW#456549");
        this.userController.userService.getAllUsers().get("Аникей Кейбордович".toUpperCase()).getBookingsSet().clear();
        tempSet.forEach(t -> this.userController.userService.getAllUsers().get("Аникей Кейбордович".toUpperCase()).addBooking(t));
        //Проверяем что метод возвращает прогнозируемый сет значений
        assertThat(this.userController.getUserBookingIDs(this.userController.getUserIDByFullName("Аникей Кейбордович"), true)).isEqualTo(tempSet);
    }

    @Test
    @Order(9)
    public void isRegisteredUser() {
        //Проверяем что метод возвращает корректные логические значения
        assertThat(this.userController.isRegisteredUser("Феодосий Апоплексиев")).isTrue();
        assertThat(this.userController.isRegisteredUser("Аникей Кейбордович")).isFalse();
    }

    @Test
    @Order(10)
    public void checkUserPassword() {
        String userId1 = this.userController.userService.getAllUsers().get("Феодосий Апоплексиев".toUpperCase()).getId();
        //Проверяем что метод возвращает правильное значение
        assertThat(this.userController.checkUserPassword("Феодосий Апоплексиев","3243724GG")).isEqualTo(userId1);
    }

    @Test
    @Order(11)
    public void saveLoadTest() {
        this.userController.createRandomUsers(20);
        int userCount = this.userController.userService.getAllUsers().size();
        Path userSource = Paths.get("testUserController.spfb");
        this.userController.saveData();
        //Проверяем что файл создан
        assertThat(userSource.toFile().exists()).isTrue();
        //Очищаем текущий список
        this.userController.userService.getAllUsers().clear();
        //Проверяем что он пуст
        assertThat(this.userController.userService.getAllUsers().isEmpty()).isTrue();
        this.userController.loadData();
        //Проверяем что после работы метода список не пустой и кол-во записей равно кол-ов сохраненных ранее записей
        assertThat(this.userController.userService.getAllUsers().isEmpty()).isFalse();
        assertThat(this.userController.userService.getAllUsers().size()).isEqualTo(userCount);
        //Проверяем что один из ранее сохраненных пользователей загружен в список
        assertThat(this.userController.userService.getAllUsers().containsValue(this.userController.userService.getAllUsers().get("Апроксиматор Синергичный".toUpperCase()))).isTrue();
        //Удаляем тестовую базу
        File userBase = userSource.toFile();
        userBase.delete();
    }
}
