package step.project.booking.bookingDAO;


import step.project.booking.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionBooking implements BookingDAO {

    // База даннных бронирований
    private List<Booking> bookingData = new ArrayList<>();

    private final File file;


    /**
     * Конструктор
     */
    public CollectionBooking(String fileName) {
        this.file = new File(fileName);
        loadDataFromFile();
    }


    /**
     * Методы
     */
    // Загрузка базы бронирований
    @Override
    public boolean loadDataFromFile() {

        if (file.exists()) {
            // Пробуем загрузить данные из базы
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                // Получаем кол-во сохранённых ранее бронирований
                int count = ois.readInt();

                for (int i = 0; i < count; i++)
                    bookingData.add((Booking) ois.readObject());

                System.out.println("\t>>> Завантажено бронювань: " + bookingData.size());

                return true;

            } catch (IOException | ClassNotFoundException e) {
                System.out.print("\t>>> Помилка завантаження бази бронювань\n\t");
                System.out.println(e);
            }

        } else
            System.out.println("\t>>> Файл бази бронювань не знайдено. Перелiк бронювань порожнiй");

        return false;
    }


    // Сохранение файла базы данных бронирований
    @Override
    public boolean saveDataFile(List<Booking> allBookingList) {

        bookingData = allBookingList;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            // первым записываем в файл кол-во броней
            oos.writeInt(allBookingList.size());

            for (Booking booking : allBookingList)
                oos.writeObject(booking);

            oos.flush();

        } catch (IOException e) {
            System.out.print("\t>>> Помилка запису бази бронювань\n\t");
            System.out.println(e);

            return false;
        }

        System.out.println("\t>>> Базу бронювань збережно: " + allBookingList.size());

        return true;
    }


    // Возвращаем список бронирований
    @Override
    public List<Booking> getAllBooking() {
        return bookingData;
    }


}

