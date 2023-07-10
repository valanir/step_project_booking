package step.project.user.user.userDAO;

import step.project.user.Logger;
import step.project.user.user.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CollectionUserDAO implements UserDAO {

    //Коллекция пользователей, ключом на текущем этапе является поле fullName пользователя
    protected Map<String, User> users;

    private final File file;


    //Конструктор класса с аргументом true загрузит список пользователей из локального хранилища, при false создаст пустой
    public CollectionUserDAO(String fileName) {
        this.file = new File(fileName);
        loadDataFromLocalStorage();
    }


    //Возращает коллекцию пользователей
    @Override
    public Map<String, User> getAllUsers() {
        Logger.info("List with " + this.users.values().size() + " users was received");
        return this.users;
    }

    @Override
    //Возвращает пользователя по его ID
    public User getUserByID(String ID) {
        ID = ID.toUpperCase().trim();

        while (ID.contains("  "))
            ID = ID.replace("  ", " ");

        if (this.users.containsKey(ID)) {
            Logger.info("User class object:" + this.users.get(ID).getFullName() + " was received");
            return this.users.get(ID);
        } else {
            Logger.info("User class object:" + ID + " was failed to receive due its absence in list");
            return null;//Нужно ловить NullPointer при использовании метода снаружи
        }
    }


    //Сохраняет нового или обновляет существующего пользователя в списке
    @Override
    public String saveUser(User user) {
        if (!users.containsKey(user.getId())) {
            Logger.info("New User " + user.getFullName() + " (id: " + user.getId() + ") was added to user list");
            System.out.println("\n\t>>> Користувача " + user.getFullName() + " (id: " + user.getId() + ") вдало додано до перелiку");
        } else {
            Logger.info("User info " + user.getFullName() + " (id: " + user.getId() + ") was updated");
            System.out.println("\n\t>>> iнформацiя щодо користувача " + user.getFullName() + " (id: " + user.getId() + ") оновлена");
        }

        this.users.put(user.getFullName().toUpperCase(), user);

        // Сохраняем изменения на диск
        saveData();

        return user.getId();
    }


    //Удаляет из списка пользователя по полному имени
    @Override
    public boolean deleteUserByFullName(String fullName) {
        User user = getUserByID(fullName);

        if (user != null) {
            Logger.info("User " + fullName + " was deleted");
            System.out.println("Користувача " + fullName + " видалено");
            this.users.remove(user.getId());
            return true;
        } else {
            Logger.info("Пользователь " + fullName + " в списке отсутствует");
            System.out.println("Користувач " + fullName + " вiдсутнiй у перелiку");
            return false;
        }
    }


    //Удаляет пользователя по ID - пока избыточен, но на всякий случай
    @Override
    public boolean deleteUserByID(String id) {
        return deleteUserByFullName(id);
    }


    //Загружает коллекцию из локального хранилища
    @Override
    public void loadDataFromLocalStorage() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = new HashMap<>((Map<String, User>) ois.readObject());
            Logger.info("List with " + users.values().size() + " records loaded to application database");
            System.out.println("\t>>> Завантажено користувачiв: " + users.size());

        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Some error was appear in user list loading from local storage process:" + e);
            System.out.println(e);
            users = new HashMap<>();
            Logger.info("New empty user database created");
        }
    }

    //Сохраняет текущий список пользователей в локальное хранилище
    @Override
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this.users);
            Logger.info("List with " + users.values().size() + " user records was saved to local storage");
            System.out.println("\n\t>>> Перелiк iз " + users.values().size() + " користувачiв було збережено на локальному накопичувачi");
        } catch (IOException e) {
            Logger.error("Something went wrong during saving " + users.values().size() + " user records to local storage");
        }
    }

    //Создает пользователя c относительно случайными полями
    @Override
    public User createRandomUser() {
        Random rnd = new Random();
        User sample = new User("SAMPLE" + rnd.nextInt(1));
        Logger.info("Random user with id: " + sample.getId() + " created");

        return sample;
    }
}
