package step.project;

import step.project.flight.Flight;
import step.project.flight.flightDAO.FlightController;
import step.project.flight.flightDAO.FlightGenerator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainTemp_FlightDAO {

    public static void main(String[] args) {

//        for (int i=0; i<256; i++)
//            System.out.println(i + " " + (char) i);

        FlightGenerator.generateFlights("flights.spfb");

        FlightController flightController = new FlightController("flights.spfb");

        List<Flight> allFlight = flightController.getAllFlights();

        allFlight.forEach(System.out::println);

        System.out.println(allFlight.size());

//        List<List<Flight>> flightListList = flightController.findFlights("Токiо", "Варшава", 3);
//
//        System.out.println(flightListList.size());
//
//        if (flightListList.size() > 0)
//            Collections.sort(flightListList, (lhs, rhs) -> lhs.size() - rhs.size());
//
//        for (List<Flight> lf : flightListList) {
//            System.out.println("\t>>> Пересадок: " + (lf.size() - 1));
//            lf.forEach(System.out::println);
//        }


    }
}
