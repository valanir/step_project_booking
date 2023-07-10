import org.junit.jupiter.api.*;
import step.project.booking.Booking;
import step.project.booking.bookingDAO.BookingService;
import step.project.booking.bookingDAO.CollectionBooking;

import java.awt.print.Book;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollectionBookingTest {

    private CollectionBooking collectionBooking = new CollectionBooking("testCollectionBooking.spfb");

    @BeforeAll
    public void initCollectionBooking() {
        this.collectionBooking.getAllBooking().clear();
        List<String> passengerSamplesX = new ArrayList<>();
        passengerSamplesX.add("T28347G5678");
        passengerSamplesX.add("П46456D8559");
        passengerSamplesX.add("R79153Й4677");
        List<Integer> flightsIDSamplesX = new ArrayList<>();
        flightsIDSamplesX.add(121);
        flightsIDSamplesX.add(444);
        flightsIDSamplesX.add(679);
        Booking sample = new Booking(flightsIDSamplesX,passengerSamplesX);
        this.collectionBooking.getAllBooking().add(sample);
        this.collectionBooking.saveDataFile(this.collectionBooking.getAllBooking());
    }

    @Test
    @Order(1)
    public void loadSaveTest() {
        assertThat(this.collectionBooking.getAllBooking()).isNotEmpty();
        List<String> passengerSamples = new ArrayList<>();
        passengerSamples.add("Ф21347Д5658");
        passengerSamples.add("П46456Р4459");
        passengerSamples.add("Х79483Й4567");
        List<Integer> flightsIDSamples = new ArrayList<>();
        flightsIDSamples.add(120);
        flightsIDSamples.add(404);
        flightsIDSamples.add(570);
        Booking sample = new Booking(flightsIDSamples, passengerSamples);
        this.collectionBooking.getAllBooking().add(sample);
        int bookingCollectionSize = this.collectionBooking.getAllBooking().size();
        assertThat(this.collectionBooking.saveDataFile(this.collectionBooking.getAllBooking())).isTrue();
        //Очищаем список
        this.collectionBooking.getAllBooking().clear();
        //Загружаем из локального хранилища
        assertThat(this.collectionBooking.loadDataFromFile()).isTrue();
        //Проверяем что кол-во записей идентичному тому, что было до очистки списка
        assertThat(this.collectionBooking.getAllBooking().size()).isEqualTo(bookingCollectionSize);
    }

    @Test
    @Order(2)
    public void getAllBookingTest() {
        assertThat(this.collectionBooking.getAllBooking()).isNotEmpty().isInstanceOf(List.class).size().isEqualTo(2);
        //Удаляем тестовую базу
        File bookingBase = new File("testCollectionBooking.spfb");
        bookingBase.delete();
    }
}
