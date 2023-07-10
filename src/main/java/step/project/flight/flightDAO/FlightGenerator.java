package step.project.flight.flightDAO;

import step.project.flight.Flight;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;

public class FlightGenerator {
    public static void main(String[] args) {
        generateFlights("flights.spfb");
        //readBase();
    }

    public static void generateFlights(String fileName) {
        File file = new File(fileName);
        final int countOfCity = NameOfCity.values().length;
        final int countOfDay = 30; //на сколько дней вперед делать рейсы
        final int countOfFlightPerDay = 10; // количество рейсов за один день с одного города

        List<Flight> flights = new ArrayList<>();
        NameOfCity[] cities = NameOfCity.values();

        for (int i = 0; i < countOfDay; i++) {
            for (int j = 0; j < countOfCity; j++) {
                NameOfCity nameDeparture = cities[j];
                NameOfCity nameArrival;

                int freeSeats = 1;

                int countOfFlights = (int) (Math.random() * countOfFlightPerDay) + 1;

                for (int k = 0; k < countOfFlights; k++) {
                    do {
                        nameArrival = cities[(int) (Math.random() * countOfCity)];
                    } while (nameDeparture == nameArrival);

                    LocalDateTime timeDeparture = LocalDateTime.now()
                            .plusMinutes((int) ((Math.random() * 12)) * 5)
                            .plusHours((int) (Math.random() * 24))
                            .plusDays(i);
                    LocalDateTime timeArrival = timeDeparture.plusMinutes((int) ((Math.random() * (270)) + 30));

                    freeSeats = (int) ((Math.random() * 5) + 1);
                    Flight flight = new Flight(nameDeparture, nameArrival, timeDeparture, timeArrival, freeSeats);
                    flights.add(flight);
                }
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeInt(flights.size());
            for (Flight f : flights) {
                oos.writeObject(f);
            }
        } catch (IOException e) {
            System.out.println("Помилка створення нової бази");
        }
    }

    public static void readBase() {
        List<Flight> tmp = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("flights.dat"))) {
            int count = ois.readInt();
            for (int i = 0; i < count; i++) {
                tmp.add((Flight) ois.readObject());
            }
            for (Flight f : tmp) {
                System.out.println(f);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error");
        }
    }
}
