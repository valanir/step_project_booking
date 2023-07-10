import org.junit.jupiter.api.*;
import step.project.user.user.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    private User user;

    @BeforeAll
    public void initUser() {
        //Инициализируем тестовый объект
        user = new User("Бонифаций   Поликарпов   ", "poliCarboNAT7894%");
    }

    @Test
    @Order(1)
    public void getIdTest() {
        //Проверяем, что поле id присвоилось при инициализации объекта
        assertThat(this.user.getId()).isNotEmpty();
    }

    @Test
    @Order(2)
    public void checkPasswordTest() {
        //Проверяем, что поле Password присвоилось корректно при инициализации и метод возвращает id пользователя
        assertThat(this.user.checkPassword("poliCarboNAT7894%")).isNotNull().isEqualTo(this.user.getId());
    }

    @Test
    @Order(3)
    public void setPasswordTest() {
        String sample = "SomePassword";
        this.user.setPassword(sample);
        //Проверяем что после присвоения пароля метод возвращает нужное значение
        assertThat(this.user.checkPassword("SomePassword")).isNotNull().isEqualTo(this.user.getId());
    }

    @Test
    @Order(4)
    public void isRegisteredTest() {
        //Проверяем что метод возвращает корректные логические значения
        assertThat(this.user.isRegistered()).isTrue();
        User sample = new User("Ибрагим Будулаевич");
        assertThat(sample.isRegistered()).isFalse();
    }

    @Test
    @Order(5)
    public void getFullNameTest() {
        //Проверяем, что полное имя заданное при инициализации объекта правильно лишилось пробелов
        assertThat(this.user.getFullName()).isEqualTo("Бонифаций Поликарпов");
    }

    @Test
    @Order(6)
    public void addBookingTest() {
        String booking1 = "ergcv-4565-dfg-&&";
        String booking2 = "gfhtg-7788-fgh-##";
        //Проверяем что возвращаемое значение класса User для Fluent паттерна
        assertThat(this.user.addBooking(booking1)).isInstanceOf(User.class);
        int setSize = this.user.getBookingsSet().size();
        //Проверяем что после работы метода сет содержит нужное значение
        assertThat(this.user.getBookingsSet().contains(booking1)).isTrue();
        this.user.addBooking(booking2);
        //Проверяем что кол-во элементов сета увеличилось
        assertThat(this.user.getBookingsSet().size()).isEqualTo(setSize + 1);
        this.user.addBooking(booking2);
        //Проверяем что кол-во элементов сета не увеличилось при добавлении дубля
        assertThat(this.user.getBookingsSet().size()).isEqualTo(setSize + 1);
    }

    @Test
    @Order(7)
    public void deleteBookingIDTest() {
        //Проверяем что метод возвращает верное логическое значение
        assertThat(this.user.deleteBookingID("ergcv-4565-dfg-&&")).isTrue();
        //Проверяем что метод возвращает верное логическое значение
        assertThat(this.user.deleteBookingID("ergcv-4565-dfg-&&")).isFalse();
        //Проверяем что после работы метода сет больше не содержит удаленного значения
        assertThat(this.user.getBookingsSet().contains("ergcv-4565-dfg-&&")).isFalse();
    }

    @Test
    @Order(8)
    public void getBookingsSetTest() {
        //Проверяем что возвращаемое значение класса Set
        assertThat(this.user.getBookingsSet()).isInstanceOf(Set.class);
        //Проверяем что возвращаемый сет не пустой, т.к. в предыдущем тесте удалили не все значения
        assertThat(this.user.getBookingsSet().isEmpty()).isFalse();
        this.user.deleteBookingID("gfhtg-7788-fgh-##");
        //Проверяем что поля опустело после удаления всех записей
        assertThat(this.user.getBookingsSet()).isEmpty();
    }

    @Test
    @Order(9)
    public void equalsTest() {
        //Проверяем рефлексивность
        assertThat(this.user.equals(this.user)).isTrue();
        User sample = new User("Амбидекстр Веникалошный");
        //Проверяем корректность сравнения с другим объектом
        assertThat(this.user.equals(sample)).isFalse();
        sample.setPassword("Биборан888");
        assertThat(this.user.equals(sample)).isFalse();

        //Проверка симметричности
        boolean compare = this.user.equals(sample);
        assertThat(sample.equals(this.user)).isEqualTo(compare);

        //Проверяем что сравние с null корректное
        assertThat(this.user.equals(null)).isFalse();

        //Проверяем транзитивность
        User sampleJunior = new User("Бонифаций   Поликарпов   ", "poliCarboNAT7894%");
        User sampleSenior = new User("Бонифаций   Поликарпов   ", "poliCarboNAT7894%");
        compare = this.user.equals(sampleJunior);
        boolean compare2 = sampleJunior.equals(sampleSenior);
        if (compare && compare2) {
            assertThat(this.user.equals(sampleSenior)).isTrue();
        }
    }

    @Test
    @Order(10)
    public void hashCodeTest() {
        //Проверяем повторяемость
        int hashCode1 = this.user.hashCode();
        int hashCode2 = this.user.hashCode();
        int hashCode3 = this.user.hashCode();
        assertThat(hashCode1 == hashCode2 && hashCode2 == hashCode3).isTrue();

        //Проверка отличия между хешами разных объектов;
        User sample = new User("Амбидекстр Веникалошный");
        assertThat(sample.hashCode()).isNotEqualTo(this.user.hashCode());

        //Проверяем хеш код идентичных объектов
        User sampleJunior = new User("Амбидекстр Веникалошный");
        hashCode1 = sample.hashCode();
        hashCode2 = sampleJunior.hashCode();
        assertThat(hashCode1 == hashCode2).isTrue();
    }
}
