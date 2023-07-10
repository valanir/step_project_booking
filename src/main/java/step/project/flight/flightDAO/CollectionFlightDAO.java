package step.project.flight.flightDAO;

import step.project.flight.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionFlightDAO {
    private final List<Flight> flightsList;
    private final File file;


    public CollectionFlightDAO(String fileName) {
        this.file = new File(fileName);
        this.flightsList = new ArrayList<>();
        loadFlightData();
    }


    // метод возвращает все рейсы
    public List<Flight> getALLFlights() {
        return flightsList;
    }

    // метож возвращает рейс по id
    public Flight getFlightById(int id) {
        for (Flight flight : getALLFlights())
            if (flight.getId() == id) return flight;
        return null;
    }

    // метод сохраняет List<Flight> flights в файл flight.dat
    public void saveFlightData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeInt(flightsList.size());
            for (Flight flight : flightsList) {
                oos.writeObject(flight);
            }
            oos.flush();
            //logger.info("File date was save successful");
            System.out.println("\t>>> Файл flights.dat збережено успiшно: " + flightsList.size());
        } catch (IOException e) {
            System.out.println("\n\t>>> Помилка збереження списку рейсiв у файл flights.dat");
        }
    }

    // метод загружает в List<Flight> flights из файла flight.dat
    public void loadFlightData() {

        // Очищаем список перед загрузкой из файла
        flightsList.clear();

        // Если базы полетов нет - генерим ее
        if (!file.exists())
            FlightGenerator.generateFlights(file.getName());

        // Пробуем загрузить
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            int count = ois.readInt();
            for (int i = 0; i < count; i++) {
                Flight flight = (Flight) ois.readObject();
                flightsList.add(flight);
            }
            System.out.println("\t>>> База даних рейсiв була успiшно завантажена: " + flightsList.size());

        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}

