
import org.junit.jupiter.api.*;
import step.project.booking.Booking;
import step.project.booking.bookingDAO.BookingController;
import step.project.booking.bookingDAO.BookingService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerTest {

    private BookingController bookingController;

    @BeforeAll
    public void initBookingController() {
        this.bookingController = new BookingController("testBookingService.spfb");
    }

    @Test
    @Order(1)
    public void createNewBookingTest() {
        int bookingSize = this.bookingController.getAllBooking().size();
        List<String> passengerSamplesX = new ArrayList<>();
        passengerSamplesX.add("T28347G5678");
        passengerSamplesX.add("П46456D8559");
        passengerSamplesX.add("R79153Й4677");
        List<Integer> flightsIDSamplesX = new ArrayList<>();
        flightsIDSamplesX.add(121);
        flightsIDSamplesX.add(444);
        flightsIDSamplesX.add(679);
        assertThat(this.bookingController.createNewBooking(flightsIDSamplesX,passengerSamplesX)).isInstanceOf(String.class);
        assertThat(this.bookingController.getAllBooking().size()).isEqualTo(bookingSize + 1);
    }

    @Test
    @Order(2)
    public void getBookingByIDTest() {
        List<String> passengerSamplesX = new ArrayList<>();
        passengerSamplesX.add("C56347T7723");
        passengerSamplesX.add("V44696B8678");
        passengerSamplesX.add("D32154F3122");
        List<Integer> flightsIDSamplesX = new ArrayList<>();
        flightsIDSamplesX.add(91);
        flightsIDSamplesX.add(555);
        flightsIDSamplesX.add(881);
        String sampleId = this.bookingController.createNewBooking(flightsIDSamplesX,passengerSamplesX);
        //Проверяем что возвращаемое значение нужного класса и не является Null
        assertThat(this.bookingController.getBookingByID(sampleId)).isInstanceOf(Booking.class).isNotNull();
        //Проверяем что возвращаемое значение идентично переданному
        assertThat(this.bookingController.getBookingByID(sampleId).getFlightIDList()).isEqualTo(flightsIDSamplesX);
        assertThat(this.bookingController.getBookingByID(sampleId).getPassengerIDList()).isEqualTo(passengerSamplesX);
    }

      @Test
    @Order(3)
    public void getAllBookingTest(){
        assertThat(this.bookingController.getAllBooking()).isInstanceOf(List.class);
        assertThat(this.bookingController.getAllBooking().size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    public void saveDataFileTest() {
        int bookingSize = this.bookingController.getAllBooking().size();
        this.bookingController.saveDataFile();
        List<Booking> temp = this.bookingController.getAllBooking();
        //Очищаем список
        this.bookingController = new BookingController("testBookingService.spfb");
        //Проверяем что размер загруженного списка идентичен размеру сохраненного
        assertThat(this.bookingController.getAllBooking().size()).isEqualTo(bookingSize);
        assertThat(this.bookingController.getAllBooking()).isEqualTo(temp);
        //Удаляем тестовую базу
        File bookingBase = new File("testBookingService.spfb");
        bookingBase.delete();
    }
}
