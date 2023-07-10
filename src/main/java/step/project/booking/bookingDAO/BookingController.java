package step.project.booking.bookingDAO;

import step.project.booking.Booking;

import java.util.List;

public class BookingController {

    BookingService bookingService;

    public BookingController(String fileName) {
        bookingService = new BookingService(fileName);
    }


    public List<Booking> getAllBooking() {
        return bookingService.getAllBooking();
    }


    public String createNewBooking(List<Integer> listIDFlight, List<String> listIDPassenger) {
        return bookingService.createNewBooking(listIDFlight, listIDPassenger);
    }

//    public List<Booking> getAllBookingOfUserID(String userID){
//        return bookingService.getAllBookingOfUserID(userID);
//    }

    // Воозвращаем бронирование по ID
    public Booking getBookingByID(String bookingID) {
        return bookingService.getBookingByID(bookingID);
    }


    public Booking deleteBookingByID(String bookingID) {
        return bookingService.deleteBookingByID(bookingID);
    }


    // Сохранение файла базы данных бронирований
    public boolean saveDataFile() {
        return bookingService.saveDataFile();
    }


    // тех Вывод всех бронирований в консоль
    public void toStringInConsole(){
        bookingService.toStringInConsole();
    }
}
