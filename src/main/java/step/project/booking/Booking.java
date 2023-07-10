package step.project.booking;


import step.project.flight.flightDAO.NameOfCity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Booking implements Serializable {

    // Формат строкового отображения даты бронировани
    public final static DateTimeFormatter BUY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ZoneOffset даты бронирования
    public final static ZoneOffset BUY_DATE_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());

    // Уникальный ID бронирования
    private final String ID;

    // Дата и время бронирования в миллисекундах
    private final long buyDateEpochMilli;

    // Список ID пассажиров, первым идет тот, кто оформляет бронь.
    private final List<String> passengerIDList;

    /**
     * Список id рейсов.
     * Если пересадок нет - то в списке будет один рейс
     * Если есть пересадки - рейсы в списке расположены в порядке следования
     */
    private final List<Integer> flightIDList;



    /*
        Конструктор
     */


    public Booking(List<Integer> listIDFlight, List<String> listIDPassenger) {
        // Получаем текущую дату и время
        LocalDateTime nowLDT = LocalDateTime.now();
        buyDateEpochMilli = nowLDT.toInstant(BUY_DATE_OFFSET).toEpochMilli();

        // Генерируем уникальный ID бронирования
        ID = String.valueOf(Booking.getRndChar()) + Booking.getRndChar() + nowLDT.getSecond();

        passengerIDList = new ArrayList<>(listIDPassenger);

        flightIDList = new ArrayList<>(listIDFlight);
    }



    /*
        Методы
     */

    public String getID() {
        return ID;
    }

    // Возвращает дату бронирования в LocalDateTime
    public LocalDateTime getBuyDateInLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(buyDateEpochMilli), BUY_DATE_OFFSET);
    }

    // Возвращает дату бронирования в виде строки
    public String getBuyDateInString() {
        return getBuyDateInLocalDateTime().format(BUY_DATE_FORMATTER);
    }

    // Добавляет id рейса в список id рейсов
    public Booking addFlightID(Integer flightId) {
        flightIDList.add(flightId);
        return this;
    }

    // Добавляем id пассажира в список id пасажиров
    public Booking addPassengerID(String passengerID) {
        passengerIDList.add(passengerID);
        return this;
    }

    // Проверяет по ID есть ли человек в данном бронировании
    public boolean isPassenger(String passengerID) {
        return passengerIDList.contains(passengerID);
    }

    // Проверяет по ID является ли человек тем кто оформил бронь
    public boolean isBuyer(String passengerID) {
        return passengerIDList.get(0).equals(passengerID);
    }

    // Возвращает список пассажиров
    public List<String> getPassengerIDList() {
        return passengerIDList;
    }

    // Возвращает список рейсов
    public List<Integer> getFlightIDList() {
        return flightIDList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(getID(), booking.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    @Override
    public String toString(){
        return "Booking{ID=" + getID() + ", buyDate=" + getBuyDateInString() + ", passengerIDList=" + getPassengerIDList().toString() + ", flightIDList=" + getFlightIDList().toString();
    }

    /**
     * Вспомогательный метод.
     *
     * @return случайныя заглавная латинская буква
     */
    public static char getRndChar() {
        Random rnd = new Random();
        return (char) (rnd.nextInt(25) + 65);
    }
}

