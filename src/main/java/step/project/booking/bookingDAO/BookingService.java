package step.project.booking.bookingDAO;

import step.project.booking.Booking;

import java.util.List;
import java.util.Objects;

public class BookingService {

    BookingDAO bookingDAO;

    public BookingService(String fileName) {
        bookingDAO = new CollectionBooking(fileName);
    }


    // Возвращаем список всех бронирований
    public List<Booking> getAllBooking() {
        return bookingDAO.getAllBooking();
    }


    // Создаёт новое бронирование и возвращает его
    public String createNewBooking(List<Integer> listIDFlight, List<String> listIDPassenger) {
        Booking newBooking = new Booking(listIDFlight, listIDPassenger);
        List<Booking> allBookingList = bookingDAO.getAllBooking();

        if (allBookingList.contains(newBooking))
            System.out.println("\n\t Fatal ERROR !!!!   Бронирование с таким ID уже существует !!!!\n");

        allBookingList.add(newBooking);
        saveDataFile();

        return newBooking.getID();
    }


    // Возвращаем бронирование по ID
    public Booking getBookingByID(String bookingID) {
        for (Booking b : bookingDAO.getAllBooking()) {
            if (Objects.equals(b.getID(), bookingID))
                return b;
        }
        return null;
    }


    // Удаление бронирования по ID
    public Booking deleteBookingByID(String bookingID) {
        Booking booking = getBookingByID(bookingID);
        getAllBooking().remove(booking);

        saveDataFile();

        return booking;
    }


    // Сохранение файла базы данных бронирований
    public boolean saveDataFile() {
        return bookingDAO.saveDataFile(getAllBooking());
    }


    // тех Вывод всех бронирований в консоль
    public void toStringInConsole(){
        getAllBooking().forEach(System.out::println);
    }
}
