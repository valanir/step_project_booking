import org.junit.jupiter.api.*;
import step.project.flight.Flight;
import step.project.flight.flightDAO.NameOfCity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FlightTest {

    private Flight flight;

    @BeforeAll
    public void initFlight() {
        LocalDateTime timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(22, 45));
        LocalDateTime timeArrival = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(5, 30));
        this.flight = new Flight(NameOfCity.KYIV, NameOfCity.ATLANTA, timeDeparture, timeArrival, 100);
    }

    @Test
    @Order(1)
    public void getIdTest() {
        int id1 = this.flight.getId();
        //Проверяем что метод возвращает стартовое значение счетчика
        assertThat(id1).isEqualTo(0);
        LocalDateTime timeDeparture1 = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(3, 20));
        LocalDateTime timeArrival1 = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(6, 50));
        LocalDateTime timeDeparture2 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 44));
        LocalDateTime timeArrival2 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 28));
        Flight sample1 = new Flight(NameOfCity.ANTALYA, NameOfCity.BEIJING, timeDeparture1, timeArrival1, 300);
        Flight sample2 = new Flight(NameOfCity.BARCELONA, NameOfCity.CHERNIVCI, timeDeparture2, timeArrival2, 250);
        int id2 = sample1.getId();
        int id3 = sample2.getId();
        //Проверяем что метод возвращает корректную инкрементацию счетчика после создания дополнительных рейсов
        assertThat(id2).isEqualTo(1);
        assertThat(id3).isEqualTo(2);
    }

    @Test
    @Order(2)
    public void getDepartureTest() {
        //Проверяем что метод возвращает правильное значение, присвоенное при инициализации объекта
        assertThat(this.flight.getDeparture()).isInstanceOf(NameOfCity.class).isEqualTo(NameOfCity.KYIV);
    }

    @Test
    @Order(3)
    public void getArrivalTest() {
        //Проверяем что метод возвращает правильное значение, присвоенное при инициализации объекта
        assertThat(this.flight.getArrival()).isInstanceOf(NameOfCity.class).isEqualTo(NameOfCity.ATLANTA);
    }

    @Test
    @Order(4)
    public void getTimeDepartureTest() {
        LocalDateTime timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(22, 45));
        //Проверяем что метод возвращает правильное значение, присвоенное при инициализации объекта
        assertThat(this.flight.getTimeDeparture()).isInstanceOf(LocalDateTime.class).isEqualTo(timeDeparture);
    }

    @Test
    @Order(5)
    public void getTimeArrivalTest() {
        LocalDateTime timeArrival = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(5, 30));
        //Проверяем что метод возвращает правильное значение, присвоенное при инициализации объекта
        assertThat(this.flight.getTimeArrival()).isInstanceOf(LocalDateTime.class).isEqualTo(timeArrival);
    }

    @Test
    @Order(6)
    public void addBookingIDTest() {
        String booking1 = "№64596";
        int bookingCount = this.flight.getBookingList().size();
        assertThat(this.flight.addBookingID(booking1)).isTrue();
        //Проверяем что список бронирований увеличился
        assertThat(this.flight.getBookingList().size()).isEqualTo(bookingCount + 1);
        //Проверяем что список бронирований содержит добавленый номер брони
        assertThat(this.flight.getBookingList()).contains(booking1);
        //Заполняем рейс под завязку и проверяем, что метод возвращает false при попытке переполнения
        while (this.flight.getFreeSeats() > 0)
            this.flight.addBookingID(booking1);
        assertThat(this.flight.addBookingID(booking1)).isFalse();
    }

    @Test
    @Order(7)
    public void getBookingListTest() {
        //Проверяем что метод возвращает объект нужного класса
        assertThat(this.flight.getBookingList()).isInstanceOf(List.class);
        //Проверяем что переданные объекты содержатся в списке
        assertThat(this.flight.getBookingList().contains("№64596")).isTrue();
        //Проверяем что список заполнен максимальному кол-ву мест на рейсе (в предыдущем тесте)
        assertThat(this.flight.getBookingList().size()).isEqualTo(100);
    }

    @Test
    @Order(8)
    public void getFreeSeats() {
        this.flight.getBookingList().clear();
        int seats = this.flight.getFreeSeats();
        String booking1 = "№64596";
        this.flight.addBookingID(booking1);
        //Проверяем что счетчик свободных мест на рейсе уменьшился
        assertThat(this.flight.getFreeSeats()).isEqualTo(seats - 1);
        String booking2 = "№65756";
        this.flight.addBookingID(booking2);
        //Проверяем что счетчик свободных мест на рейсе уменьшился
        assertThat(this.flight.getFreeSeats()).isEqualTo(seats - 2);
    }

    @Test
    @Order(9)
    public void removeBookingID() {
        String booking1 = "№64596";
        String booking2 = "№65756";
        int seats = this.flight.getFreeSeats();
        assertThat(this.flight.removeBookingID(booking1)).isInstanceOf(Flight.class);
        //Проверяем что счетчик свободных мест на рейсе увеличился
        assertThat(this.flight.getFreeSeats()).isEqualTo(seats + 1);
        this.flight.removeBookingID(booking2);
        //Проверяем что счетчик свободных мест на рейсе увеличился
        assertThat(this.flight.getFreeSeats()).isEqualTo(seats + 2);
        //Проверяем что счетчик свободных мест на рейсе не изменился
        this.flight.removeBookingID(booking1);
        assertThat(this.flight.getFreeSeats()).isEqualTo(seats + 2);
    }
}
