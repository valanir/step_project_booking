import org.junit.jupiter.api.*;
import org.testng.annotations.Ignore;
import step.project.flight.Flight;
import step.project.flight.flightDAO.FlightController;
import step.project.flight.flightDAO.FlightService;
import step.project.flight.flightDAO.NameOfCity;
import step.project.flight.flightDAO.FlightController;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static step.project.flight.flightDAO.FlightGenerator.generateFlights;
import static step.project.flight.flightDAO.NameOfCity.KYIV;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FlightControllerTest {

    public FlightController flightController;

    @BeforeAll
    public void initFlightController() {
        this.flightController = new FlightController("testFlightService.spfb");
    }

    @Test
    @Order(1)
    public void loadFlightDataTest() {
        this.flightController.getAllFlights().clear();
        //Проверяем список до загрузки
        assertThat(this.flightController.getAllFlights()).isEmpty();
        this.flightController.loadFlightData();
        //Проверяем список после загрузки
        assertThat(this.flightController.getAllFlights()).isNotEmpty();
    }

    @Test
    @Order(2)
    public void getAllFlightsTest() {
        //Проверяем что возвращаемы результат нужного класса и не является пустым
        assertThat(this.flightController.getAllFlights()).isInstanceOf(List.class).isNotEmpty();
        //Выводим в консоль для дальнейших тестов
        this.flightController.getAllFlights().forEach(System.out::println);
    }

    @Test
    @Order(3)
    public void addBookingIDsToFlightByIDs() {
        this.flightController.getAllFlights().clear();
        LocalDateTime timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(22, 45));
        LocalDateTime timeArrival = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(5, 30));
        Flight sample1 = new Flight(NameOfCity.KYIV, NameOfCity.ATLANTA, timeDeparture, timeArrival, 100);
        timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 14));
        timeArrival = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(22, 50));
        Flight sample2 = new Flight(NameOfCity.KYIV, NameOfCity.TAIBEI, timeDeparture, timeArrival, 300);
        timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(23, 15));
        timeArrival = LocalDateTime.of(LocalDate.now().plusDays(4), LocalTime.of(4, 11));
        Flight sample3 = new Flight(NameOfCity.KYIV, NameOfCity.ATLANTA, timeDeparture, timeArrival, 150);
        this.flightController.getAllFlights().add(sample1);
        this.flightController.getAllFlights().add(sample2);
        this.flightController.getAllFlights().add(sample3);
        List<Integer> flightIDs = new ArrayList<>();
        for (Flight flight : this.flightController.getAllFlights())
            flightIDs.add(flight.getId());
        this.flightController.addBookingIDsToFlightByIDs(flightIDs, "T64596H2345438");
        //Проверяем что все текущие записи рейсов содержат переданный ID брони
        for (Flight flight : this.flightController.getAllFlights())
            assertThat(flight.getBookingList().contains("T64596H2345438")).isTrue();
    }

    @Test
    @Order(4)
    public void getFlightByIDTest() {
        this.flightController.getAllFlights().clear();
        LocalDateTime timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(22, 45));
        LocalDateTime timeArrival = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(5, 30));
        Flight sample1 = new Flight(NameOfCity.KYIV, NameOfCity.ATLANTA, timeDeparture, timeArrival, 100);
        this.flightController.getAllFlights().add(sample1);
        //Проверяем что метод возвращает объект нужного класса
        assertThat(this.flightController.getFlightByID(sample1.getId())).isInstanceOf(Flight.class);
        //Проверяем реквизиты рейса на соответствие
        assertThat(this.flightController.getFlightByID(sample1.getId()).getDeparture()).isEqualTo(NameOfCity.KYIV);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getArrival()).isEqualTo(NameOfCity.ATLANTA);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeDeparture().getYear()).isEqualTo(LocalDate.now().getYear());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeDeparture().getMonthValue()).isEqualTo(LocalDate.now().getMonthValue());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeDeparture().getDayOfMonth()).isEqualTo(LocalDate.now().plusDays(2).getDayOfMonth());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeDeparture().getHour()).isEqualTo(22);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeDeparture().getMinute()).isEqualTo(45);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeArrival().getYear()).isEqualTo(LocalDate.now().getYear());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeArrival().getMonthValue()).isEqualTo(LocalDate.now().getMonthValue());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeArrival().getDayOfMonth()).isEqualTo(LocalDate.now().plusDays(3).getDayOfMonth());
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeArrival().getHour()).isEqualTo(5);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getTimeArrival().getMinute()).isEqualTo(30);
        assertThat(this.flightController.getFlightByID(sample1.getId()).getFreeSeats()).isEqualTo(100);
    }

    @Test
    @Order(5)
    public void getFlightFromTest() {
        //Генерируем новую базу рейсов
        generateFlights("testFlightService.spfb");
        //Проверяем что метод возвращает объект нужного класса
        assertThat(this.flightController.getFlightFrom(this.flightController.getAllFlights(), KYIV)).isInstanceOf(List.class);
        List<Flight> searchResult = this.flightController.getFlightFrom(this.flightController.getAllFlights(), KYIV);
        //Проверяем что список содержит объекты с указанным местом вылета
        for (Flight flight : searchResult)
            assertThat(flight.getDeparture()).isEqualTo(KYIV);
        //Проверяем что список не содержит других рейсов
        for (Flight flight : searchResult)
            assertThat(flight.getDeparture()).isNotEqualTo(Arrays.stream(NameOfCity.values())
                    .filter(c -> !c.equals(KYIV))
                    .iterator()
                    .next());
    }

    @Test
    @Order(6)
    public void getFlightAfterTest() {
        //Проверяем что метод возвращает объект нужного класса
        LocalDateTime date = LocalDateTime.now();
        assertThat(this.flightController.getFlightAfter(this.flightController.getAllFlights(), date)).isInstanceOf(List.class);
        //Проверяем что метод возвращает список с объектами удовлетворяющими запрос
        List<Flight> searchResult = this.flightController.getFlightAfter(this.flightController.getAllFlights(), date);
        for (Flight flight : searchResult)
            assertThat(flight.getTimeDeparture().isAfter(date)).isTrue();
        //Проверяем что список не содержит объектов вне критерия
        for (Flight flight : searchResult)
            assertThat(flight.getTimeDeparture().isBefore(date)).isFalse();
    }

    @Test
    @Order(7)
    public void getFlightBeforeTest() {
        //Проверяем что метод возвращает объект нужного класса
        LocalDateTime date = LocalDateTime.now();
        assertThat(this.flightController.getFlightBefore(this.flightController.getAllFlights(), date)).isInstanceOf(List.class);
        //Проверяем что метод возвращает список с объектами удовлетворяющими запрос
        List<Flight> searchResult = this.flightController.getFlightBefore(this.flightController.getAllFlights(), date);
        for (Flight flight : searchResult)
            assertThat(flight.getTimeDeparture().isBefore(date)).isTrue();
        //Проверяем что список не содержит объектов вне критерия
        for (Flight flight : searchResult)
            assertThat(flight.getTimeDeparture().isAfter(date)).isFalse();
    }

    @Test
    @Order(8)
    public void findFlightsTest() {
        List<List<Flight>> searchResult = this.flightController.findFlights(NameOfCity.TOKYO, NameOfCity.CHICAGO, 2);
        searchResult.forEach(System.out::println);
        //Проверяем что в выдаче метода объекта соответствуют всем критериям
        for (List<Flight> listflight : searchResult) {
            assertThat(listflight.get(0).getDeparture()).isEqualTo(NameOfCity.TOKYO); //Первый рейс в списке с точкой отправления Токио
            assertThat(listflight.get(listflight.size() - 1).getArrival()).isEqualTo(NameOfCity.CHICAGO); //Последний рейс в списке с местом прибытия Чикаго
            assertThat(listflight.stream().filter(f -> f.getFreeSeats() < 2).collect(Collectors.toList())).isEmpty();//Рейсов с кол-вом свободных мест меньше заданного нет
        }
    }

    @Test
    @Order(9)
    public void getFlightNext24H() {
        List<Flight> h24list = this.flightController.getFlightNext24H(KYIV);
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        //Проверяем что результирующий список удовлетворяет заданным критериям
        for (Flight flight : h24list) {
            assertThat(flight.getDeparture()).isEqualTo(KYIV);
            assertThat(flight.getTimeDeparture()).isBefore(time);
            assertThat(flight.getTimeDeparture()).isAfter(LocalDateTime.now());
        }
    }

    @Test
    @Order(10)
    public void saveFlightDataTest() {
        int baseSize = this.flightController.getAllFlights().size();
        this.flightController.saveFlightData();
        //Обнуляем текущую базу рейсов, точнее переходим на новую
        this.flightController.getAllFlights().clear();
        //Проверяем что база пуста
        assertThat(this.flightController.getAllFlights()).isEmpty();
        //Загружаем базу из локального хранилища
        this.flightController.loadFlightData();
        //Проверяем что база не пуста
        assertThat(this.flightController.getAllFlights()).isNotEmpty();
        //Проверяем что база содержит идентичное кол-во пользователей как и до сохранения/очистки
        assertThat(this.flightController.getAllFlights().size()).isEqualTo(baseSize);
        //Удаляем искаженную тестами базу
        File flightSource = new File("testFlightService.spfb");
        flightSource.delete();
    }
}
