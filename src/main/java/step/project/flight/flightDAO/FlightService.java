package step.project.flight.flightDAO;

import step.project.flight.Flight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FlightService {
    private static CollectionFlightDAO flightDAO;


    public FlightService(String fileName) {
        flightDAO = new CollectionFlightDAO(fileName);
    }


    // метод возвращает все рейсы
    public List<Flight> getAllFlights() {
        return flightDAO.getALLFlights();
    }

    // метод возвращает рейс согласно его id
    public Flight getFlightByID(int id) {
        return flightDAO.getFlightById(id);
    }

    // метод сохраняет List<Flight> flights в файл flight.dat
    public void saveFlightData() {
        flightDAO.saveFlightData();
    }

    // метод загружает в List<Flight> flights из файла flight.dat
    public void loadFlightData() {
        flightDAO.loadFlightData();
    }


    // ----------------------------------------------------------------------------------------------------------------


    public void addBookingIDsToFlightByIDs(List<Integer> flightIDList, String newBookingID) {
        // variant # 1
        // getAllFlights().stream().filter(f -> flightIDList.contains(f.getId())).forEach(f -> f.addBookingID(newBookingID));

        // variant # 2
        getAllFlights().forEach(f -> {
            if (flightIDList.contains(f.getId()))
                f.addBookingID(newBookingID);
        });

        saveFlightData();
    }


    public void deleteBookingIDsToFlightByIDs(List<Integer> flightIDList, String bookingID) {
        getAllFlights().forEach(f -> {
            while (f.getBookingList().contains(bookingID))
                f.removeBookingID(bookingID);
        });

        saveFlightData();
    }


    public List<Flight> getFlightNext24H(NameOfCity enumDepartureCity) {
        // Получаем список всех рейсов
        List<Flight> flightList = getAllFlights();

        // Выбираем рейсы с нужным городом вылета
        flightList = getFlightFrom(flightList, enumDepartureCity);

        // Выбираем рейсы с датой вылета до (текущее время + 24 часа)
        flightList = getFlightBefore(flightList, LocalDateTime.now().plusDays(1));

        // Выбираем рейсы с датой вылета после текущего времени (отбрасываем рейсы из прошлого)
        flightList = getFlightAfter(flightList, LocalDateTime.now());

        return flightList;

    }


    /**
     * Возвращает список из списков рейсов
     * Если без пересадки - подсписок содержит один рейс
     * Если с пересадкой подсписок содержит список рейсов
     *
     * @param enumDepartureCity город отправления
     * @param enumArrivalCity   город прибытия
     * @param numOfSeats        кол-во мест
     * @return список из списков рейсов для перелёта (список вариантов добраться
     */
    public List<List<Flight>> findFlights(NameOfCity enumDepartureCity, NameOfCity enumArrivalCity, int numOfSeats) {

        // Список со списками рейсов для перелёта
        List<List<Flight>> flightsListsList = new ArrayList<>();

        // Если не нашли запрашиваемые города в перечислениях, возвращаем null
        if (enumDepartureCity == null || enumArrivalCity == null)
            return null;

        // Получаем все рейсы, убираем рейсы c датой отправления раньше текущей, убираем рейсы с малым кол-вом мест
        List<Flight> allFlight = getFlightAfter(getAllFlights(), LocalDateTime.now())
                .stream()
                .filter(f -> f.getFreeSeats() >= numOfSeats)
                .toList();

        // Запускаем рекурсивную функцию поиска
        recursiveFlightSearch(new ArrayList<>(), allFlight, enumDepartureCity, enumArrivalCity, flightsListsList);

        // Возвращаем, предварительно убрав списки рейсов с общим временем пересадок больше ьaximumNumberOfHoursForTransfer
        return flightsListsList;

    }


    /**
     * рекурсивный посик рейсов
     *
     * @param tmpList           временный список рейсов для 1 перелёта
     * @param flightsToBust     список рейсов для поиска марщрута
     * @param departureCity     город вылета
     * @param targetArrivalCity город прибытия
     * @param flightListList    список из списков рейсов для перелёта (список вариантов добраться из пункта А в пункт Б
     */
    private void recursiveFlightSearch(List<Flight> tmpList, List<Flight> flightsToBust, NameOfCity departureCity, NameOfCity targetArrivalCity, List<List<Flight>> flightListList) {
        // Максимальное время для пересадки в часах
        int maximumNumberOfHoursForTransfer = 12;

        // Перебираем рейсы, выбрав с нужным городом отправления
        for (Flight nextFlight : getFlightFrom(flightsToBust, departureCity)) {

            // Список для передачи в рекурсию или сохранение
            List<Flight> nextFlightList = new ArrayList<>(tmpList);
            nextFlightList.add(nextFlight);

            // Считаем общее кол-во часов между рейсами
            int hoursCount = 0;
            if (nextFlightList.size() > 1)
                for (int i = 0; i < (nextFlightList.size() - 1); i++)
                    hoursCount += ChronoUnit.HOURS.between(nextFlightList.get(i).getTimeArrival(), nextFlightList.get(i + 1).getTimeDeparture());

            // Если время пересадок превышает максимальное - пропускаем связку рейсов
            if (hoursCount > maximumNumberOfHoursForTransfer)
                continue;

            // Если город последнего прибытия совпадает с желаемым - сохраняем связку рейсов
            if (nextFlight.getArrival().equals(targetArrivalCity)) {
                flightListList.add(nextFlightList);

            } else
                // Иначе передаем связку дальше в рекурсию
                recursiveFlightSearch(nextFlightList,
                        flightsToBust
                                .stream()
                                .filter(f -> f.getTimeDeparture().isAfter(nextFlight.getTimeArrival()))
                                .toList(),
                        nextFlight.getArrival(), targetArrivalCity, flightListList);
        }
    }


    /**
     * Принимает список рейсов и возвращает отфильтрованнвый по городу отправления
     */
    public List<Flight> getFlightFrom(List<Flight> flightList, NameOfCity enumDepartureCity) {
        return flightList.stream()
                .filter(f -> f.getDeparture().equals(enumDepartureCity))
                .toList();
    }


    /**
     * Принимает список рейсов и возвращает отфильтрованнвый - дата вылета позже указанной
     */
    public List<Flight> getFlightAfter(List<Flight> flightList, LocalDateTime localDateTime) {
        return flightList.stream()
                .filter(f -> f.getTimeDeparture().isAfter(localDateTime))
                .toList();
    }


    public List<Flight> getFlightBefore(List<Flight> flightList, LocalDateTime localDateTime) {
        return flightList.stream()
                .filter(f -> f.getTimeDeparture().isBefore(localDateTime))
                .toList();
    }


    public void flightToStringInConsole(){
        getAllFlights().forEach(System.out::println);
    }


}
