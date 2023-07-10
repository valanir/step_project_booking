import org.junit.jupiter.api.*;
import step.project.user.user.User;
import step.project.user.user.userDAO.CollectionUserDAO;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollectionUserDAOTest {

    private CollectionUserDAO collectionUserDAO;

    @BeforeAll
    public void initUserDAO() {
        this.collectionUserDAO = new CollectionUserDAO("UserTest.spfb");
    }

    @Test
    @Order(1)
    public void saveUserTest() {
        User sample = new User("Семпловиний Униковский");
        this.collectionUserDAO.getAllUsers().clear();
        //Проверяем что список пользователей пуст
        assertThat(this.collectionUserDAO.getAllUsers()).isEmpty();
        this.collectionUserDAO.saveUser(sample);
        //Проверяем что список не пуст после работы метода и содержит переданное значение
        assertThat(this.collectionUserDAO.getAllUsers()).isNotEmpty().containsValue(sample);
        sample.setPassword("GigaКонь");
        this.collectionUserDAO.saveUser(sample);
        //Проверяем что информация в базе обновляется при повторном добавлении существующего объекта
        assertThat(this.collectionUserDAO.getAllUsers().get(sample.getFullName().toUpperCase()).checkPassword("GigaКонь"))
                .isEqualTo(sample.getFullName().toUpperCase());
    }

    @Test
    @Order(2)
    public void getAllUserTest() {
        User sampleJunior = new User("Аникей Кейбордович");
        User sampleSenior = new User("Мейнфрейм Байтович");
        this.collectionUserDAO.saveUser(sampleJunior);
        this.collectionUserDAO.saveUser(sampleSenior);
        assertThat(this.collectionUserDAO.getAllUsers())
                .isInstanceOf(Map.class)      //Метод возвращает нужный класс
                .isNotEmpty()                 //Возвращаемый объект не пуст
                .containsValue(sampleJunior)  //Содержит ранее переданный объект1
                .containsValue(sampleSenior)  //Содержит ранее переданный объект1
                .size().isEqualTo(3); //Имеет ожидаемый размер
    }

    @Test
    @Order(4)
    public void getUserByIdTest() {
        User sampleJunior = new User("Аникей Кейбордович");
        String id = sampleJunior.getId();
        assertThat(this.collectionUserDAO.getUserByID(id)).isInstanceOf(User.class).isEqualTo(sampleJunior);
    }

    @Test
    @Order(5)
    public void deleteUserByFullName() {
        assertThat(this.collectionUserDAO.deleteUserByFullName("Аникей Кейбордович")).isTrue();
        assertThat(this.collectionUserDAO.deleteUserByFullName("Аникей Кейбордович")).isFalse();
        assertThat(this.collectionUserDAO.getUserByID(("Аникей Кейбордович").toUpperCase())).isNull();
    }

    @Test
    @Order(6)
    public void deleteUserByID() {
        User sampleJunior = new User("Ибрагим Полуэктович");
        String id = sampleJunior.getId();
        this.collectionUserDAO.saveUser(sampleJunior);
        //Проверяем что метод возвращает корректные значения
        assertThat(this.collectionUserDAO.deleteUserByID(id)).isTrue();
        assertThat(this.collectionUserDAO.deleteUserByID(id)).isFalse();
        //Проверяем что удаленный объект отсутствует в коллекции
        assertThat(this.collectionUserDAO.getAllUsers().values().contains(sampleJunior)).isFalse();
    }

    @Test
    @Order(7)
    public void createRandomUserTest(){
        int usersCount = this.collectionUserDAO.getAllUsers().size();
        this.collectionUserDAO.saveUser(this.collectionUserDAO.createRandomUser());
        //Проверяем что размер коллекции увеличился на 1
        assertThat(this.collectionUserDAO.getAllUsers().size()).isEqualTo(usersCount + 1);
    }

    @Test
    @Order(8)
    public void saveLoadDataTest(){
        this.collectionUserDAO.saveUser(this.collectionUserDAO.createRandomUser());
        this.collectionUserDAO.saveUser(this.collectionUserDAO.createRandomUser());
        this.collectionUserDAO.saveUser(this.collectionUserDAO.createRandomUser());
        this.collectionUserDAO.saveUser(this.collectionUserDAO.createRandomUser());
        User sample = new User("Апроксиматор Синергичный");
        this.collectionUserDAO.saveUser(sample);
        int userCount = this.collectionUserDAO.getAllUsers().size();
        Path userSourcePath = Paths.get("UserTest.spfb");
        this.collectionUserDAO.saveData();
        //Проверяем что файл создан
        assertThat(userSourcePath.toFile().exists()).isTrue();
        //Очищаем текущий список
        this.collectionUserDAO.getAllUsers().clear();
        //Проверяем что он пуст
        assertThat(this.collectionUserDAO.getAllUsers().isEmpty()).isTrue();
        this.collectionUserDAO.loadDataFromLocalStorage();
        //Проверяем что после работы метода список не пустой и кол-во записей равно кол-ов сохраненных ранее записей
        assertThat(this.collectionUserDAO.getAllUsers().isEmpty()).isFalse();
        assertThat(this.collectionUserDAO.getAllUsers().size()).isEqualTo(userCount);
        //Проверяем что один из ранее сохраненных пользователей загружен в список
        assertThat(this.collectionUserDAO.getAllUsers().containsValue(sample)).isTrue();
        //Удаляем тестовую базу
        File userSource = userSourcePath.toFile();
        userSource.delete();
    }
}
