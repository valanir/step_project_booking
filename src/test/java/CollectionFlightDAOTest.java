import org.junit.jupiter.api.*;
import step.project.flight.flightDAO.CollectionFlightDAO;
import step.project.flight.Flight;
import step.project.flight.flightDAO.FlightGenerator;
import step.project.flight.flightDAO.NameOfCity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static step.project.flight.flightDAO.FlightGenerator.generateFlights;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollectionFlightDAOTest {

    CollectionFlightDAO collectionFlightDAO;

    @BeforeAll
    public void initCollectionFlightDAO() {
        this.collectionFlightDAO = new CollectionFlightDAO("testFlightService.spfb");
    }

    @Test
    @Order(1)
    public void loadFlightDataTest() {
        this.collectionFlightDAO.getALLFlights().clear();
        //Проверяем список до загрузки
        assertThat(this.collectionFlightDAO.getALLFlights()).isEmpty();
        this.collectionFlightDAO.loadFlightData();
        //Проверяем список после загрузки
        assertThat(this.collectionFlightDAO.getALLFlights()).isNotEmpty();
    }

    @Test
    @Order(2)
    public void getALLFlightsTest() {
        //Проверяем что возвращаемы результат нужного класса и не является пустым
        assertThat(this.collectionFlightDAO.getALLFlights()).isInstanceOf(List.class).isNotEmpty();
        //Выводим в консоль для дальнейших тестов
        this.collectionFlightDAO.getALLFlights().forEach(System.out::println);
    }

    @Test
    @Order(3)
    public void getFlightByIdTest() {
        //Проверяем что метод возвращает объект нужного класса
        assertThat(this.collectionFlightDAO.getFlightById(10)).isInstanceOf(Flight.class);
        this.collectionFlightDAO.getALLFlights().clear();
        LocalDateTime timeDeparture = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(22, 45));
        LocalDateTime timeArrival = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(5, 30));
        Flight sample = new Flight(NameOfCity.KYIV, NameOfCity.ATLANTA, timeDeparture, timeArrival, 100);
        this.collectionFlightDAO.getALLFlights().add(sample);
        int id = sample.getId();
        //Проверяем реквизиты рейса на соответствие
        assertThat(this.collectionFlightDAO.getFlightById(id).getDeparture()).isEqualTo(NameOfCity.KYIV);
        assertThat(this.collectionFlightDAO.getFlightById(id).getArrival()).isEqualTo(NameOfCity.ATLANTA);
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeDeparture().getYear()).isEqualTo(LocalDate.now().getYear());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeDeparture().getMonthValue()).isEqualTo(LocalDate.now().getMonthValue());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeDeparture().getDayOfMonth()).isEqualTo(LocalDate.now().plusDays(2).getDayOfMonth());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeDeparture().getHour()).isEqualTo(22);
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeDeparture().getMinute()).isEqualTo(45);
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeArrival().getYear()).isEqualTo(LocalDate.now().getYear());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeArrival().getMonthValue()).isEqualTo(LocalDate.now().getMonthValue());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeArrival().getDayOfMonth()).isEqualTo(LocalDate.now().plusDays(3).getDayOfMonth());
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeArrival().getHour()).isEqualTo(5);
        assertThat(this.collectionFlightDAO.getFlightById(id).getTimeArrival().getMinute()).isEqualTo(30);
        assertThat(this.collectionFlightDAO.getFlightById(id).getFreeSeats()).isEqualTo(100);
    }

    @Test
    @Order(4)
    public void saveFlightDataTest() {
        int baseSize = this.collectionFlightDAO.getALLFlights().size();
        this.collectionFlightDAO.saveFlightData();
        //Обнуляем текущую базу рейсов, точнее переходим на новую
        this.collectionFlightDAO.getALLFlights().clear();
        //Проверяем что база пуста
        assertThat(this.collectionFlightDAO.getALLFlights()).isEmpty();
        //Загружаем базу из локального хранилища
        this.collectionFlightDAO.loadFlightData();
        //Проверяем что база не пуста
        assertThat(this.collectionFlightDAO.getALLFlights()).isNotEmpty();
        //Проверяем что база содержит аналогичное кол-во записей что и до очистки
        assertThat(this.collectionFlightDAO.getALLFlights().size()).isEqualTo(baseSize);
        //Удаляем искаженную тестами базу
        File flightSource = new File("testFlightService.spfb");
        flightSource.delete();
    }
}
