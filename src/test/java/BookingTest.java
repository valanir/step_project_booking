import org.junit.jupiter.api.*;
import step.project.booking.Booking;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingTest {

    private Booking booking;

    @BeforeAll
    public void initBooking() {
        List<String> passengerSamples = new ArrayList<>();
        passengerSamples.add("Ф21347Д5658");
        passengerSamples.add("П46456Р4459");
        passengerSamples.add("Х79483Й4567");
        List<Integer> flightsIDSamples = new ArrayList<>();
        flightsIDSamples.add(101);
        flightsIDSamples.add(404);
        flightsIDSamples.add(570);
        this.booking = new Booking(flightsIDSamples, passengerSamples);
    }

    @Test
    @Order(1)
    public void getIDTest() {
        //Проверяем что метод возвращает объект нужного класса и он не пуст, т.е. при инициализации объекта поле заполнилось
        assertThat(this.booking.getID()).isInstanceOf(String.class).isNotEmpty();
    }

    @Test
    @Order(2)
    public void getBuyDateInLocalDateTimeTest() {
        //Проверяем что метод возвращает объект нужного класса и он не пуст, т.е. при инициализации объекта поле заполнилось
        assertThat(this.booking.getBuyDateInLocalDateTime()).isInstanceOf(LocalDateTime.class).isBefore(LocalDateTime.now());
    }

    @Test
    @Order(3)
    public void getBuyDateInStringTest() {
        //Проверяем что метод возвращает не пустую строку нужного формата
        assertThat(this.booking.getBuyDateInString()).isInstanceOf(String.class).isNotEmpty()
                .matches(Pattern.compile("(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4}) ([01][0-9]|2[0-4]):([0-5][0-9])"));
    }

    @Test
    @Order(4)
    public void addFlightIDTest() {
        this.booking.getFlightIDList().clear();
        //Проверяем что список рейсов пуст
        assertThat(this.booking.getFlightIDList()).isEmpty();
        //Проверяем что метод возвращает объект класса Booking
        assertThat(this.booking.addFlightID(120)).isInstanceOf(Booking.class);
        //Проверяем что список рейсов уже не пуст
        assertThat(this.booking.getFlightIDList()).isNotEmpty();
        //Проверяем что список рейсов содержит добавленный ID
        assertThat(this.booking.getFlightIDList().contains(120)).isTrue();
    }

    @Test
    @Order(5)
    public  void addPassengerIDTest() {
        this.booking.getPassengerIDList().clear();
        //Проверяем что список пассажиров пуст
        assertThat(this.booking.getPassengerIDList()).isEmpty();
        //Проверяем что метод возвращает объект класса Booking
        assertThat(this.booking.addPassengerID("Ф21347Д5658")).isInstanceOf(Booking.class);
        //Проверяем что список пассажиров уже не пуст
        assertThat(this.booking.getPassengerIDList()).isNotEmpty();
        //Проверяем что список рейсов содержит добавленный ID
        assertThat(this.booking.getPassengerIDList().contains("Ф21347Д5658")).isTrue();
    }

    @Test
    @Order(6)
    public void isPassengerTest() {
        //Проверяем что метод возвращает корректный логический тип
        assertThat(this.booking.isPassenger("Ф21347Д5658")).isTrue();
        assertThat(this.booking.isPassenger("Клавесин")).isFalse();
    }

    @Test
    @Order(7)
    public void isBuyerTest() {
        this.booking.addPassengerID("Апроксиматор56190");
        //Проверяем что метод возвращает корректный логический тип
        assertThat(this.booking.isBuyer("Ф21347Д5658")).isTrue();
        assertThat(this.booking.isBuyer("Апроксиматор56190")).isFalse();
    }

    @Test
    @Order(8)
    public void getPassengerListTest(){
        List<String> passengerSamples = new ArrayList<>();
        passengerSamples.add("Ф21347Д5658");
        passengerSamples.add("Апроксиматор56190");
        passengerSamples.add("П46456Р4459");
        passengerSamples.add("Х79483Й4567");
        this.booking.addPassengerID("П46456Р4459");
        this.booking.addPassengerID("Х79483Й4567");
        assertThat(this.booking.getPassengerIDList()).isInstanceOf(List.class).isEqualTo(passengerSamples);
    }

    @Test
    @Order(9)
    public void getFlightListTest() {
        List<Integer> flightsIDSamples = new ArrayList<>();
        flightsIDSamples.add(120);
        flightsIDSamples.add(404);
        flightsIDSamples.add(570);
        this.booking.addFlightID(404);
        this.booking.addFlightID(570);
        assertThat(this.booking.getFlightIDList()).isInstanceOf(List.class).isEqualTo(flightsIDSamples);
    }

    @Test
    @Order(10)
    public void equalsTest() {
        //Проверяем рефлексивность
        assertThat(this.booking.equals(this.booking)).isTrue();

        List<String> passengerSamplesX = new ArrayList<>();
        passengerSamplesX.add("T28347G5678");
        passengerSamplesX.add("П46456D8559");
        passengerSamplesX.add("R79153Й4677");
        List<Integer> flightsIDSamplesX = new ArrayList<>();
        flightsIDSamplesX.add(121);
        flightsIDSamplesX.add(444);
        flightsIDSamplesX.add(679);
        Booking sample = new Booking(flightsIDSamplesX, passengerSamplesX);
        //Проверяем корректность сравнения с другим объектом
        assertThat(this.booking.equals(sample)).isFalse();

        //Проверка симметричности
        boolean compare = this.booking.equals(sample);
        assertThat(sample.equals(this.booking)).isEqualTo(compare);

        //Проверяем что сравние с null корректное
        assertThat(this.booking.equals(null)).isFalse();

        //Проверяем транзитивность
/*        Booking sample1 = new Booking(this.booking.getFlightIDList(),this.booking.getPassengerIDList());
        Booking sample2 = new Booking(this.booking.getFlightIDList(),this.booking.getPassengerIDList());
        compare = this.booking.equals(sample1);
        boolean compare1 = sample1.equals(sample2);
        if (compare == compare1)
            assertThat(this.booking.equals(sample2)).isTrue();*/
        //Проверить транзитивность невозможно, т.к. сравнение про
    }

    @Test
    @Order(11)
    public  void hashCodeTest() {
        //Проверяем повторяемость
        int hashCode1 = this.booking.hashCode();
        int hashCode2 = this.booking.hashCode();
        int hashCode3 = this.booking.hashCode();
        assertThat(hashCode1 == hashCode2 && hashCode2 == hashCode3).isTrue();

        //Проверка отличия между хешами разных объектов;
        List<String> passengerSamplesX = new ArrayList<>();
        passengerSamplesX.add("T28347G5678");
        passengerSamplesX.add("П46456D8559");
        passengerSamplesX.add("R79153Й4677");
        List<Integer> flightsIDSamplesX = new ArrayList<>();
        flightsIDSamplesX.add(121);
        flightsIDSamplesX.add(444);
        flightsIDSamplesX.add(679);
        Booking sample = new Booking(flightsIDSamplesX, passengerSamplesX);
        assertThat(sample.hashCode()).isNotEqualTo(this.booking.hashCode());

        //Хешкод идентичных объектов
/*        List<String> passengerSamples = new ArrayList<>();
        passengerSamples.add("Ф21347Д5658");
        passengerSamples.add("Апроксиматор56190");
        passengerSamples.add("П46456Р4459");
        passengerSamples.add("Х79483Й4567");
        List<Integer> flightsIDSamples = new ArrayList<>();
        flightsIDSamples.add(120);
        flightsIDSamples.add(404);
        flightsIDSamples.add(570);
        Booking sample1 = new Booking(flightsIDSamples,passengerSamples);
        Booking sample2 = new Booking(flightsIDSamples,passengerSamples);
        hashCode2 = sample1.hashCode();
        hashCode3 = sample2.hashCode();
        assertThat(hashCode1 == hashCode2 && hashCode2 == hashCode3).isTrue();*/

        //Проверить невозможно из-за хеширования по случайногенерируемому полю
    }
}
