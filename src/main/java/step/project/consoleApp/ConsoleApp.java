package step.project.consoleApp;


import step.project.consoleApp.myConsoleMenu.MenuItem;

import java.util.Scanner;


public class ConsoleApp {
    AppService appService;

    // Авторизировавшийся пользователь
    // private User user = null;


    public void start() throws Exception {
        Scanner sc = new Scanner(System.in);

        // Подключаем сканнер
        appService = new AppService(sc, this);
        MenuItem.setScanner(sc);

        // Запускаем главное меню
        start_menu_authorization();

        // Сохраняем все базы данных
        appService.saveAllDataInfile();

        // Закрываем Сканнер
        sc.close();
    }


    /**
     * Меню Авторизации, главное меню
     */
    protected void start_menu_authorization() throws Exception {
        new MenuItem("Головне меню")
                .add(new MenuItem("Авторизацiя",
                        appService::signIn))
                .add(new MenuItem("Показати рейси на ближайшi 24 години",
                        appService::showFlight24H))
                .add(new MenuItem("Пошук бронювань за ' Призвище iм'я '",
                        appService::showAllBookingOfUserByFullName))
                .add(new MenuItem("Сервис - вывод внутренней, тестовой информации")
                        .add(new MenuItem("база всех рейсов",
                                appService::flightToStringInConsole))
                        .add(new MenuItem("база всех юзеров",
                                appService::userToStringInConsole))
                        .add(new MenuItem("база всех бронирований",
                                appService::bookingToStringInConsole))
                ).run(null);
    }


    protected void cabinet_menu(String fullUserName) throws Exception {
        new MenuItem(fullUserName)
                .add(new MenuItem("Мої бронювання")
                        .add(new MenuItem("Створити бронювання",
                                appService::searchAndBookingFlight))
                        .add(new MenuItem("Переглянути бронювання",
                                appService::showAllBookingCurrentUser))
                        .add(new MenuItem("Пошук бронювань за ' Призвище iм'я '",
                                appService::showAllBookingOfUserByFullName))
                        .add(new MenuItem("Скасувати бронювання",
                                appService::deletedBookingOfCurrentUser)))
                .add(new MenuItem("Показати рейси на ближайшi 24 години",
                        appService::showFlight24H))
                .add(new MenuItem("Сервис - вывод внутренней, тестовой информации")
                        .add(new MenuItem("база всех рейсов",
                                appService::flightToStringInConsole))
                        .add(new MenuItem("база всех юзеров",
                                appService::userToStringInConsole))
                        .add(new MenuItem("база всех бронирований",
                                appService::bookingToStringInConsole))
                ).run(null);
    }
}
