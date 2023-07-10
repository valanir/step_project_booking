package step.project.user.user.userDAO;

import java.util.List;
import java.util.Set;

public class UserController {

    public UserService userService;


    public UserController(String fileName) {
        this.userService = new UserService(fileName);
    }


    // Проверяет зарегистрирован ли пользователь
    public boolean isRegisteredUser(String userFullName) {
        return userService.isRegisteredUser(userFullName);
    }


    // Проверяем пароль
    public String checkUserPassword(String userFullName, String password) {
        return userService.checkUserPassword(userFullName, password);
    }


//    //Возвращает коллекцию пользователей
//    public Map<String, User> getAllUsers() {
//        return this.userService.getAllUsers();
//    }


    public String getUserIDByFullName(String fullName) {
        return this.userService.getUserIDByFullName(fullName);
    }


    public String getUserFullNameById(String id) {
        return this.userService.getUserFullNameById(id);
    }


    //Создает number случайных пользователей и добавляет их в текущий список
    public void createRandomUsers(int number) {
        this.userService.createRandomUsers(number);
    }


    //Создает нового пользователя только с ФИО и добавляет в текущий список
    public String saveUser(String fullName, String oldPassword, String newPassword) {
        return this.userService.saveUser(fullName, oldPassword, newPassword);
    }

    // Добавляет id рейса списку пользователе по id
    public void addBookingIDsToUsersByIDs(List<String> usersID, String bookingID) {
        userService.addBookingIDsToUsersByIDs(usersID, bookingID);
    }

    public void deleteBookingIDsToUsersByIDs(List<String> usersID, String bookingID) {
        userService.deleteBookingIDsToUsersByIDs(usersID, bookingID);
    }


    //Возвращает список номеров билетов пользователя и выводит в консоль если указан output = true
    public Set<String> getUserBookingIDs(String fullName, boolean output) {
        return this.userService.getUserBookingIDs(fullName, output);
    }


    //Сохраняет текущий список в локальное хранилище
    public void saveData() {
        this.userService.saveData();
    }


    //Загружает список из локального хранилища
    public void loadData() {
        this.userService.loadData();
    }


    public void toStringInConsole() {
        userService.toStringInConsole();
    }
}

