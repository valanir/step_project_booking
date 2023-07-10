package step.project.flight;

import step.project.flight.flightDAO.NameOfCity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight implements Serializable {
    static int staticId = 0;
    int id;   // id рейса
    NameOfCity departure; //пункт вылета
    NameOfCity arrival; //пункт назначения
    LocalDateTime timeDeparture; // дата и время вылета
    LocalDateTime timeArrival; // дата и время вылета
    List<String> bookingList = new ArrayList<>(); //список пассажиров
    int numOfSeats; //к-тво мест


    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", departure='" + departure.nameOfCity + '\'' +
                ", arrival='" + arrival.nameOfCity + '\'' +
                ", timeDeparture= " + timeDeparture.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm ")) +
                ", timeArrival= " + timeArrival.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                ", freeSeats= " + getFreeSeats() +
                '}';
    }

    public Flight(NameOfCity departure, NameOfCity arrival, LocalDateTime timeDeparture, LocalDateTime timeArrival, int numOfSeats) {
        this.departure = departure;
        this.arrival = arrival;
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
        this.numOfSeats = numOfSeats;
        this.id = staticId;
        staticId++;
    }

    public int getId() {
        return id;
    }

    // возвращает город с которого вылет
    public NameOfCity getDeparture() {
        return departure;
    }

    // возвращает город куда летит
    public NameOfCity getArrival() {
        return arrival;
    }

    // возвращает дату и время вылета
    public LocalDateTime getTimeDeparture() {
        return timeDeparture;
    }

    // возвращает дату и время приземление
    public LocalDateTime getTimeArrival() {
        return timeArrival;
    }

    // врозвращает список пассажиров на текущем рейсе
    public List<String> getBookingList() {
        return bookingList;
    }

    // добавляет пассажира на рейс
    public boolean addBookingID(String passenger) {
        if (getFreeSeats() > 0) {
            this.bookingList.add(passenger);
            return true;
        } else System.out.println("Не має вiльних мiсць");

        return false;
    }

    // возвращает к-тво свободных мест в рейсе
    public int getFreeSeats() {
        return numOfSeats - bookingList.size();
    }

    public Flight removeBookingID(String bookingID) {
        while (bookingList.contains(bookingID))
            bookingList.remove(bookingID);
        return this;
    }

}
