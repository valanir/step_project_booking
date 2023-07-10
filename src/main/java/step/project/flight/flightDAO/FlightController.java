package step.project.flight.flightDAO;

import step.project.flight.Flight;

import java.time.LocalDateTime;
import java.util.List;

public class FlightController {
    private final FlightService flightService;

    public FlightController(String fileName) {
        this.flightService = new FlightService(fileName);
    }


    // метод возвращает все рейсы
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    // метод возвращает рейс согласно позиции в списке
    public Flight getFlightByID(int id) {
        return flightService.getFlightByID(id);
    }


    // метод сохраняет List<Flight> flights в файл flight.dat
    public void saveFlightData() {
        //log
        flightService.saveFlightData();
    }

    // метод загружает в List<Flight> flights из файла flight.dat
    public void loadFlightData() {
        //log
        flightService.loadFlightData();
    }


    //-------------------------------------------------------------------------------------------------


    // Добавляем ID бронирования в рейсы
    public void addBookingIDsToFlightByIDs(List<Integer> flightIDList, String newBookingID) {
        flightService.addBookingIDsToFlightByIDs(flightIDList, newBookingID);
    }


    public void deleteBookingIDsToFlightByIDs(List<Integer> flightIDList, String bookingID) {
        flightService.deleteBookingIDsToFlightByIDs(flightIDList, bookingID);
    }

    public List<Flight> getFlightNext24H(NameOfCity enumDepartureCity) {
        return flightService.getFlightNext24H(enumDepartureCity);
    }


    // Ищет рейсы по задданым критериям
    public List<List<Flight>> findFlights(NameOfCity departureCity, NameOfCity arrivalCity, int numOfSeats) {
        return flightService.findFlights(departureCity, arrivalCity, numOfSeats);
    }


    /**
     * Принимает список рейсов и возвращает отфильтрованнвый по городу отправления
     */
    public List<Flight> getFlightFrom(List<Flight> flightList, NameOfCity enumDepartureCity) {
        return flightService.getFlightFrom(flightList, enumDepartureCity);
    }


    /**
     * Принимает список рейсов и возвращает отфильтрованнвый - дата вылета позже указанной
     */
    public List<Flight> getFlightAfter(List<Flight> flightList, LocalDateTime localDateTime) {
        return flightService.getFlightAfter(flightList, localDateTime);
    }


    public List<Flight> getFlightBefore(List<Flight> flightList, LocalDateTime localDateTime) {
        return flightService.getFlightBefore(flightList, localDateTime);
    }


    public void flightToStringInConsole(){
        flightService.flightToStringInConsole();
    }

}
