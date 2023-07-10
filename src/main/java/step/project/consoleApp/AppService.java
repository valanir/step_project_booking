package step.project.consoleApp;

import de.vandermeer.asciitable.*;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import step.project.booking.Booking;
import step.project.booking.bookingDAO.BookingController;
import step.project.consoleApp.helpers.Helpers;
import step.project.flight.Flight;
import step.project.flight.flightDAO.FlightController;
import step.project.flight.flightDAO.NameOfCity;
import step.project.user.user.userDAO.UserController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AppService {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter FORMATTER_T = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");
    private final Scanner sc;
    private final ConsoleApp consoleApp;


    private final FlightController flightController = new FlightController("flights.spfb");
    private final UserController userController = new UserController("user.spfb");
    private final BookingController bookingController = new BookingController("booking.spfb");
    private final Helpers hl;


    // Авторизированный юзер
    private String currentUserID = null;


    /**
     * Конструктор
     */
    protected AppService(Scanner sc, ConsoleApp consoleApp) {
        this.sc = sc;
        this.consoleApp = consoleApp;
        this.hl = new Helpers(sc);
    }


    /**
     * Методы
     */
    // Авторизация и регистрация.
    public void signIn() throws Exception {
        String userFullName;
        String userPassword;

        userFullName = hl.inputString("Введiть  \"Призвiще iм'я\": ");
        if (userFullName == null) return;

        // Если пользователь НЕ зарегистрирован
        if (!userController.isRegisteredUser(userFullName)) {
            System.out.println("Користувач " + userFullName + " не зареєстррований. Для реєстрацiї встановiть пароль.");

            userPassword = hl.inputString("Придумайте собi пароль: ");
            if (userPassword == null) return;

            // Устанавливаем ID текущего пользователя
            currentUserID = userController.saveUser(userFullName, null, userPassword);

            // Если пользователь зарегистрирован
        } else
            while (true) {
                userPassword = hl.inputString("Введiть свiй пароль: ");
                if (userPassword == null) return;

                // Устанавливаем ID текущего пользователя
                currentUserID = userController.checkUserPassword(userFullName, userPassword);
                if (currentUserID == null)
                    System.out.println("\n\t>>> Пароль НЕ вiрний !");
                else
                    break;
            }


        // Запускаем меню личного кабинета
        consoleApp.cabinet_menu("Кабiнет " + userController.getUserFullNameById(currentUserID));

        // Сюда мы попадем, когда выйдем из меню личного кабинета - "забываем" авторизированного пользователя
        currentUserID = null;

    }


    // Показываем рейсы на ближайшие 24 часа
    public void showFlight24H() {
        // Выбираем город отправления из индексированного списка
        List<NameOfCity> numOfCityList = getCityEnumFromIndexList("Введiть номер мiста вiдправлення: ", 1);
        if (numOfCityList == null) return;
        NameOfCity enumDeparture = numOfCityList.get(0);

        List<Flight> next24HFlight = flightController.getFlightNext24H(enumDeparture).stream().limit(90).toList();

        System.out.println();

        if (next24HFlight.size() == 0)
            System.out.println("\t>>> З мiста " + enumDeparture.nameOfCity + " немає вильотiв найближчi 24 години ");
        else
            printFlightListInConsole(next24HFlight, "Рейси на найближчi 24 години",
                    "час формування: " + LocalDateTime.now().format(FORMATTER_T), null);
    }


    /**
     * Вывод в консоль списка рейсов в таблице
     *
     * @param flightList список рейсов
     * @param titleL     текст левая верхняя ячейка
     * @param titleR     текст правая верхняя ячеййка
     */
    public void printFlightListInConsole(List<Flight> flightList, String titleL, String titleR, String titleB) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");
        AsciiTable at = new AsciiTable();

        at.addRule();
        AT_Row rowTitle = at.addRow(null, null, titleL, null, null, titleR);
        rowTitle.getCells().get(2).getContext().setTextAlignment(TextAlignment.LEFT);
        rowTitle.getCells().get(5).getContext().setTextAlignment(TextAlignment.RIGHT);

        at.addRule();
        at.addRow("ID", null, "Вiдправлення", null, "Прибуття", (titleB == null ? "Мiсця" : ""))
                .setTextAlignment(TextAlignment.CENTER);

        for (Flight f : flightList) {
            at.addRule();
            AT_Row row = at.addRow(f.getId(), f.getDeparture().nameOfCity, f.getTimeDeparture().format(formatter),
                    f.getArrival().nameOfCity, f.getTimeArrival().format(formatter), (titleB == null ? f.getFreeSeats() : ""));
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
            row.getCells().get(5).getContext().setTextAlignment(TextAlignment.CENTER);
        }
        at.addRule();

        if (titleB != null) {
            at.addRow(null, null, null, null, null, titleB).setTextAlignment(TextAlignment.LEFT);
            at.addRule();
        }

        // Отступы содержимого от левого и правого края таблицы
        at.setPaddingLeftRight(1);

        // Устанавливаем ширину столбцов таблицы
        at.getRenderer().setCWC(new CWC_FixedWidth().add(6).add(20).add(20).add(20).add(20).add(7));

        System.out.println(at.render());
    }


    /**
     * Выбор города из пронумерованного номера
     *
     * @param requestText            текст строки запроса
     * @param numToInputOfNameOfCity кол-во enum городов, которое нужно получить
     * @return список enum городов
     */
    private List<NameOfCity> getCityEnumFromIndexList(String requestText, int numToInputOfNameOfCity) {
        // Получаем список всех городов
        List<String> listCityName = Arrays.stream(NameOfCity.values()).map(c -> c.nameOfCity).toList();

        // Выводим пронумерованный список доступных городов
        System.out.println("Перелiк мiст:\n");

        for (int i = 0; i < listCityName.size(); i++) {
            System.out.printf("%3s | %-20s", i + 1, listCityName.get(i));
            if ((i + 1) % 5 == 0) System.out.println();
        }
        System.out.println();

        // Цикл запроса номеров городов у пользователя
        while (true) {
            List<NameOfCity> arrNameOfCities = new ArrayList<>();

            String inputString = hl.inputString(requestText);
            if (inputString == null) return null;

            // Парсим введенную строку в массив строк
            String[] indOfCities = inputString.split(" ");

            // Передираем полученный массив введенных данных
            for (int i = 0; i < indOfCities.length; i++) {

                // Парсим в число, если элемент не число - присваиваем 0
                int tmp = indOfCities[i].matches("([1-9]+)|([1-9][0-9]+)") ? Integer.parseInt(indOfCities[i]) : 0;

                // Если введенное число НЕ в диапазоне - ругаемся
                if (tmp < 1 || tmp > listCityName.size())
                    System.out.println("\t>>> Параметр № " + (i + 1) + ". \"" + indOfCities[i] + "\" вне диапазона");
                    // Если введенное число в диапазоне - получаем enum города и сохраняем в список
                else
                    arrNameOfCities.add(NameOfCity.getEnumByValue(listCityName.get(tmp - 1)));
            }

            // Проверяем введенные города на совпадение
            if (arrNameOfCities.size() != new HashSet<NameOfCity>(arrNameOfCities).size()) {
                System.out.println(">>> Мiсто вiдправлення не може спiвпадати з мiстом прибуття");
                continue;
            }

            // проверяем кол-во полученных имен городов и возвращаем, если оно соответствует запрашиваемому
            if (arrNameOfCities.size() == numToInputOfNameOfCity)
                return arrNameOfCities;

            System.out.println("\t>>> Выбрано " + arrNameOfCities.size() + ", ожидается " + numToInputOfNameOfCity);
        }
    }


    // Поиск рейсов и бронирование
    public void searchAndBookingFlight() {
        if (currentUserID == null) {
            System.out.println("\n\t>>> Для бронювання Вам необхїдно авторизуватись");
            return;
        }

        List<List<Flight>> listOfFlightList;
        Integer numSeats = 0;

        // Ввод пунктов вылета и прибытия, кол-ва мест. Поиск всех возможных вариантов
        while (true) {
            // Выбираем город вылета и назначения
            List<NameOfCity> enumCityList = getCityEnumFromIndexList(
                    "Введiть через пробiл номери мiста вiдправлення та прибуття\n(приклад: 23 12) : ", 2);
            if (enumCityList == null) return;

            NameOfCity enumDepartureCity = enumCityList.get(0);
            NameOfCity enumArrivalCity = enumCityList.get(1);
            System.out.println("\nПерелiт " + enumDepartureCity.nameOfCity + " - " + enumArrivalCity.nameOfCity);

            // Запрос кол-ва мест
            do {
                numSeats = hl.inputInt("Введiть кiлькiсть мiсць: ");
                if (numSeats == null) return;
            } while (numSeats < 1);

            // Получаем варианты перелёта удовлетворяющие критериям
            listOfFlightList = flightController.findFlights(enumDepartureCity, enumArrivalCity, numSeats);
            // Если есть доступные варианты - выходим из цикла ввода
            if (listOfFlightList.size() > 0) break;

            System.out.println("\n\t>>> Нажаль за вказаними параметрами немаэ пропозицiй, сробуйте змiнити праметри запиту\n");
        }

        // Выбор даты рейсов
        LocalDate selectedLocalDate = choiceDateOfFlights(listOfFlightList);
        if (selectedLocalDate == null) return;

        // Фильтруем арианты с выбранной датой вылета
        listOfFlightList = listOfFlightList.stream()
                .filter(lf -> lf.get(0).getTimeDeparture().toLocalDate().equals(selectedLocalDate)).toList();

        // Выбираем вариант перельта
        List<Flight> selectedListOfFlights = choiceListFlight(listOfFlightList);
        if (selectedListOfFlights == null) return;

        // Выводи выбранный рейс
        System.out.println("\nОбраний перелiт:");
        printFlightListInConsole(selectedListOfFlights, "Напрямок: "
                        + selectedListOfFlights.get(0).getDeparture() + " - "
                        + selectedListOfFlights.get(selectedListOfFlights.size() - 1).getArrival(),
                "Загальний час перельоту, годин: " + ChronoUnit.HOURS.between(
                        selectedListOfFlights.get(0).getTimeDeparture(),
                        selectedListOfFlights.get(selectedListOfFlights.size() - 1).getTimeArrival()), null
        );

        // Подготавливаем список пассажиров, первым добавляем владельца
        List<String> passengerIDList = new ArrayList<>();
        passengerIDList.add(currentUserID);

        // Запрашиваем остальных пассажиров
        while (passengerIDList.size() < numSeats) {
            String fullNameOfNextPassenger = hl.inputString("Введiть \"Фамiлiю та iм'я\" пассажира "
                    + (passengerIDList.size() + 1) + " з " + numSeats + " : ");
            if (fullNameOfNextPassenger == null) return;

            String nextUserID = userController.getUserIDByFullName(fullNameOfNextPassenger);
            // Если пассажира нет -  сохраняем его и получаем его ID
            if (nextUserID == null)
                nextUserID = userController.saveUser(fullNameOfNextPassenger, null, null);

            passengerIDList.add(nextUserID);
        }

        // Получаем ID всех рейсов
        List<Integer> flightIDList = selectedListOfFlights.stream().map(f -> f.getId()).toList();

        // Создаём новое бронирование со списками пользователей и рейсов
        String newBookingID = bookingController.createNewBooking(flightIDList, passengerIDList);

        // Добавляем ID бронирования пользователям и рейсам, сохраняем
        flightController.addBookingIDsToFlightByIDs(flightIDList, newBookingID);
        userController.addBookingIDsToUsersByIDs(passengerIDList, newBookingID);

        printBookingInConsole(newBookingID, "Бронювання сформоване");
    }


    // Вывод в консоль всех бронирований текущего пользователя
    public void showAllBookingCurrentUser() {
        if (currentUserID == null)
            System.out.println("\n\t>>> Вы не авторизованы !");
        else
            showAllBookingOfUser(currentUserID);
    }


    // Вывод в консоль бронирований пользователя по  fullName
    public void showAllBookingOfUserByFullName() {
        while (true) {
            String fullName = hl.inputString("Введiть Фамiлiю iм'я для пошуку: ");
            if (fullName == null) return;

            String userID = userController.getUserIDByFullName(fullName);
            if (userID == null)
                System.out.println("\n\t>>> Не знайдено користувача " + fullName + "\n");
            else showAllBookingOfUser(userID);
        }
    }


    // Перегляд всiх своїх бронювань
    public void showAllBookingOfUser(String userID) {
        Set<String> bookingIDList = userController.getUserBookingIDs(userID, false);

        if (bookingIDList.size() == 0)
            System.out.println("\n\t>>> Нет бронирований для отображения\n");
        else bookingIDList.forEach(b ->
                printBookingInConsole(b, "Дата створеннябронювання:"));
    }


    // Удаление своего бронирования
    public void deletedBookingOfCurrentUser() {
        if (currentUserID == null) {
            System.out.println("\n\t>>> Для виконання цiєї дiї Вам необхiдно авторизуватись.");
        }

        // Плучаем список ID бронирования юзера где он владелец
        List<String> bookingIDListWhereBuyer = userController.getUserBookingIDs(currentUserID, false)
                .stream().filter(id -> bookingController.getBookingByID(id).isBuyer(currentUserID)).toList();

        if (bookingIDListWhereBuyer.size() == 0) {
            System.out.println("\n\t>>> не знайдено бронювань сформованих Вами. Видаляти можна тiлки тi, якi сформували саме Ви. ");
            return;
        }

        // Показываем бронирование
        bookingIDListWhereBuyer.forEach(id -> printBookingInConsole(id, "Дата створеннябронювання:"));

        // Запрашиваем именно ID бронирования для исключения случайного удаления
        String idOfBookingToDelete;
        while (true) {
            idOfBookingToDelete = hl.inputString("Введiть ID бронювання, яке треба видалити: ");
            if (idOfBookingToDelete == null) return;

            if (bookingIDListWhereBuyer.contains(idOfBookingToDelete.toUpperCase()))
                break;

            System.out.println("\t>>> iз зазначаним ID не знайдено бронювання сформованного Вами. Видаляти можна тiлки тi, якi сформували саме Ви. ");
        }

        System.out.println("\n\t>>> Бронювання видалене!\n ");

        Booking deletedBooking = bookingController.deleteBookingByID(idOfBookingToDelete.toUpperCase());

        userController.deleteBookingIDsToUsersByIDs(deletedBooking.getPassengerIDList(), idOfBookingToDelete.toUpperCase());
        flightController.deleteBookingIDsToFlightByIDs(deletedBooking.getFlightIDList(), idOfBookingToDelete.toUpperCase());
    }


    // Выводим бронирование в консоль
    private void printBookingInConsole(String bookingID, String titleL) {
        // Выводим бронирование в консоль
        Booking booking = bookingController.getBookingByID(bookingID);
        List<Flight> flightList = booking.getFlightIDList().stream().map(flightController::getFlightByID).toList();
        List<String> usersFullNameList = booking.getPassengerIDList().stream().map(userController::getUserFullNameById).toList();

        printFlightListInConsole(flightList, titleL + "<br>" + booking.getBuyDateInString(), "ID бронювання: " + bookingID
                        + "<br>" + "Перелiт: " + flightList.get(0).getDeparture() + " - " + flightList.get(flightList.size() - 1).getArrival(),
                "Власник: " + usersFullNameList.get(0) + "<br>"
                        + "Пасажири: " + usersFullNameList.stream().skip(1).toList());
    }

    // Выбор даты рейса для бронирования
    private LocalDate choiceDateOfFlights(List<List<Flight>> listOfFlightList) {
        // Класс минимальных значений из перелётов на дату
        class FData {
            public int minNumberTransfers;
            public int minFlightTime;

            FData(int mnt, int mft) {
                minNumberTransfers = mnt;
                minFlightTime = mft;
            }

            public FData upDate(int mnt, int mft) {
                minNumberTransfers = Math.min(minNumberTransfers, mnt);
                minFlightTime = Math.min(minFlightTime, mft);
                return this;
            }
        }

        TreeMap<LocalDate, FData> dateMap = new TreeMap<>();

        listOfFlightList.forEach(lf -> {
            LocalDateTime departDate = lf.get(0).getTimeDeparture();
            LocalDateTime arrivDate = lf.get(lf.size() - 1).getTimeArrival();

            int transferTime = (int) ChronoUnit.HOURS.between(departDate, arrivDate);

            dateMap.putIfAbsent(departDate.toLocalDate(), new FData(lf.size() - 1, transferTime));
            dateMap.put(departDate.toLocalDate(), dateMap.get(departDate.toLocalDate()).upDate(lf.size() - 1, transferTime));
        });

        LocalDate desiredDate;
        String inputStr;

        // Выводим список доступных дат
        System.out.println("\nОберiть дату зi списку\n"
                + "\n\t'mitT' - мiн кiлькiсть пересадок\n\t'minH' - мiн тривалiсть перельоту в годинах\n");

        int count = 0;
        for (LocalDate ld : dateMap.keySet()) {
            count++;
            FData fData = dateMap.get(ld);
            System.out.printf("%-10s | minT: %-2s minH: %-15s",
                    ld.format(FORMATTER), fData.minNumberTransfers, fData.minFlightTime);

            if ((count % 3) == 0 || count == dateMap.size())
                System.out.println();
        }

        System.out.println();

        // Запрашиваем ввод даты
        while (true) {
            inputStr = hl.inputString("Введите дату в формате \"dd/MM/yyyy\": ");
            if (inputStr == null) return null;

            try {
                desiredDate = LocalDate.parse(inputStr, FORMATTER);
                if (dateMap.containsKey(desiredDate))
                    return desiredDate;

            } catch (DateTimeParseException e) {
                System.out.printf("\t>>> Строка %s не є датой формата %s%n", inputStr, "dd/MM/yyyy\n");
                continue;
            }

            // Проверяем дату, не позже текщей
            if (desiredDate.isBefore(LocalDate.now())) {
                System.out.println("\t>>> Дата не может быть раньше текущей");
                continue;
            }

            System.out.println("\n>>> Нажаль на " + inputStr + " рейсiв не має.");
        }
    }

    private List<Flight> choiceListFlight(List<List<Flight>> listsOfFlightList) {
        // Если всего один вариант - возвращаем его
        if (listsOfFlightList.size() == 1)
            return listsOfFlightList.get(0);

        // Выводим варианты перелёта
        System.out.println("\nВарїанти перельоту '"
                + listsOfFlightList.get(0).get(0).getDeparture().nameOfCity + " - "
                + listsOfFlightList.get(0).get(listsOfFlightList.get(0).size() - 1).getArrival().nameOfCity + "'");

        for (int i = 0; i < listsOfFlightList.size(); i++) {
            printFlightListInConsole(listsOfFlightList.get(i), "ВАРiАНТ № " + (i + 1),
                    "Загальный час перельоту, годин: " + Duration.between(
                            listsOfFlightList.get(i).get(0).getTimeDeparture(),
                            listsOfFlightList.get(i).get(listsOfFlightList.get(i).size() - 1).getTimeArrival()).toHours(), null);
        }

        while (true) {
            Integer inputInt = hl.inputInt("Введiть номер варiанту: ");
            if (inputInt == null) return null;

            if (inputInt > 0 && inputInt <= listsOfFlightList.size())
                return listsOfFlightList.get(inputInt - 1);

            System.out.println("\t>>> Варiант № " + inputInt + " вiдсутнiй");
        }

    }

    // Сахраняем все базы данных
    public void saveAllDataInfile() {
        userController.saveData();
        flightController.saveFlightData();
        bookingController.saveDataFile();
    }

    // тех Вывод всех бронирований в консоль
    public void bookingToStringInConsole(){
        bookingController.toStringInConsole();
    }

    // тех Вывод всех бронирований в консоль
    public void userToStringInConsole(){
        userController.toStringInConsole();
    }

    // тех Вывод всех бронирований в консоль
    public void flightToStringInConsole(){
        flightController.flightToStringInConsole();
    }
}
