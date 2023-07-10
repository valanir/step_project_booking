package step.project.user.user.userDAO;

import step.project.user.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserService {

    public UserDAO userDAO;


    public UserService(String fileName) {
        this.userDAO = new CollectionUserDAO(fileName);
    }


    public boolean isRegisteredUser(String userFullName) {
        User user;
        if ((user = getUserByFullName(userFullName)) == null)
            return false;

        return user.isRegistered();
    }


    // Проверяем пароль
    public String checkUserPassword(String userFullName, String password) {
        User user = getUserByFullName(userFullName);
        if (user == null)
            return null;

        return user.checkPassword(password);
    }


    public Map<String, User> getAllUsers() {
        return this.userDAO.getAllUsers();
    }


    //Создает number случайных пользователей и добавляет их в текущий список
    public void createRandomUsers(int number) {
        for (int i = 0; i < number; i++) {
            this.userDAO.saveUser(userDAO.createRandomUser());
        }
    }

    public String getUserIDByFullName(String fullName) {
        User user = userDAO.getUserByID(fullName.toUpperCase());
        if (user == null)
            return null;

        return user.getId();
    }

    /**
     * Сохраняем, обновляем информацию о пользователе, проверяя перед эти ранее охраненный пароль, если он есть
     *
     * @param fullName
     * @param oldPassword
     * @param newPassword
     * @return userID
     */
    public String saveUser(String fullName, String oldPassword, String newPassword) {
        User user = userDAO.getUserByID(fullName);

        if (user == null) {
            user = new User(fullName);
            // Если не указаны пароли, сохраняем пользователя и возвращаем ID
            if (oldPassword == null && newPassword == null)
                return userDAO.saveUser(user);
        }

        if (oldPassword == null && newPassword == null)
            return user.getId();

        if (user.checkPassword(oldPassword) == null) {
            System.out.println("\t>>> Не верно указан старый пароль пользователя " + fullName
                    + "\n\t\t Действие отменено!");
            return null;
        }
        user.setPassword(newPassword);

        return userDAO.saveUser(user);
    }


    public User getUserByFullName(String fullName) {
        return this.userDAO.getUserByID(fullName);
    }


    public String getUserFullNameById(String id) {
        return this.userDAO.getUserByID(id).getFullName();
    }


    public void addBookingIDsToUsersByIDs(List<String> usersID, String bookingID) {
        Map<String, User> allUsers = getAllUsers();
        usersID.forEach(id -> allUsers.get(id).addBooking(bookingID));

        saveData();
    }


    // Удаляем ID бронирования из списка бронирований юзера
    public void deleteBookingIDsToUsersByIDs(List<String> usersID, String bookingID) {
        Map<String, User> allUsers = getAllUsers();
        usersID.forEach(id -> {
            User user = allUsers.get(id);
            user.deleteBookingID(bookingID);
            userDAO.saveUser(user);
        });
    }


    //Возвращает список номеров билетов пользователя и выводит в консоль если указан show = true
    public Set<String> getUserBookingIDs(String userID, boolean show) {
        User temp = this.userDAO.getUserByID(userID);
        if (show) {
            System.out.println("Користувач " + temp.getFullName() + "(id:" + temp.getId()
                    + "), є щасливим власником наступних " + temp.getBookingsSet().size() + " квиткiв:");
            temp.getBookingsSet().forEach(System.out::println);
        }

        return temp.getBookingsSet();
    }


    //Сохраняет текущий список в локальное хранилище
    public void saveData() {
        this.userDAO.saveData();
    }


    //Загружает список из локального хранилища
    public void loadData() {
        this.userDAO.loadDataFromLocalStorage();
    }

    // тех Вывод всех бронирований в консоль
    public void toStringInConsole() {
        getAllUsers().values().forEach(System.out::println);
    }

}
