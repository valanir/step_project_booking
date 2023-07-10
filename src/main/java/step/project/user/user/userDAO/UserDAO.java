package step.project.user.user.userDAO;

import step.project.user.user.User;

import java.util.Map;


public interface UserDAO {

    Map<String, User> getAllUsers();

    User getUserByID(String fullName);

    String saveUser(User user);

    boolean deleteUserByFullName(String fullName);

    boolean deleteUserByID(String id);

    void loadDataFromLocalStorage();

    void saveData();

    User createRandomUser();
}
