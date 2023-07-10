package step.project.booking.bookingDAO;


import step.project.booking.Booking;

import java.util.List;


public interface BookingDAO {

    boolean loadDataFromFile();

    boolean saveDataFile(List<Booking> allBookingList);

    List<Booking> getAllBooking();
}
