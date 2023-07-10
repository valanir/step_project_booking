package step.project.user.user;

import step.project.user.Logger;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

    private final String ID;

    private String password;
    private final String fullName;
    private final Set<String> bookingsSet;


    /*Базовый конструктор, принимает строковую репрезентацию ФИО пользователя, заменяет 2 последовательных пробела на
    единичный. Удаляет пробелы в начале и конце строки.
     */
    public User(String fullName) {
        this(fullName, null);

    }

    public User(String fullName, String password) {
        while (fullName.contains("  ")) {
            fullName = fullName.replace("  ", " ");
        }
        this.fullName = fullName.trim();
        this.password = password;
        this.ID = this.fullName.toUpperCase();

        this.bookingsSet = new HashSet<>();
    }


    //Возвращает ID
    public String getId() {
        return this.ID;
    }


    //Проверяет пароль
    public String checkPassword(String password) {
        if (Objects.equals(this.password, password))
            return this.getId();
        return null;
    }


    // Проверяет зарегестрирован ли пользователь, нет пароля - не зарегистрированный
    public boolean isRegistered() {
        return password != null;
    }


    //Устанавливает новый пароль и возвращает объект класса User
    public User setPassword(String password) {
        this.password = password;
        return this;
    }


    //Возращает ФИО
    public String getFullName() {
        return this.fullName;
    }


    //Добавляет номер билета в список и возвращает объект класса User
    public User addBooking(String bookingID) {
        if (!this.bookingsSet.add(bookingID)) {
            Logger.error("Ticket №" + bookingID + " already in list");
            System.out.println("Квиток №" + bookingID + " вже у перелiку");
        }
        return this;
    }

    //Удаляет номер билета из списка и возращает true или false в зависимости от успешности
    public boolean deleteBookingID(String ticketID) {
        if (this.bookingsSet.contains(ticketID)) {
            this.bookingsSet.remove(ticketID);

            return true;
        } else {
            Logger.info("Ticket №" + ticketID + " is absent in list");
            System.out.println("Квиток №" + ticketID + " вiдсутнiй у перелiку");

            return false;
        }
    }


    //Возращает сет номеров билетов
    public Set<String> getBookingsSet() {
        if (this.bookingsSet.isEmpty()) {
            Logger.info("User " + this.fullName + " do not have any ticket!");
            // System.out.println("Користувач " + this.fullName + " не має жодного квитка!");
        }

        return this.bookingsSet;
    }

    //Возвращает презентацию полей объекта в виде строки
    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password=" + password +
                ", bookings=" + bookingsSet +
                '}';
    }

    //Сравнивает объекты класса User между собой (cет билетов исключен из сравнения, т.к. динамичен)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getFullName().toUpperCase(), user.getFullName().toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName().toUpperCase());
    }
}
