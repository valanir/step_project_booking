package step.project.consoleApp.myConsoleMenu;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MenuItem {

    // Ссылка на общий Scanner, для всех экземпляров класса
    private static Scanner sc;

    // Описание пункта меню
    private String menuName;

    // Список подменю данного пункта меню
    private final List<MenuItem> subMenuItemsList = new ArrayList<>();

    // Ссылка на предыдущий пункт меню
    private MenuItem prevMenuItem;


    /**
     * Интерфейс, нужно определить метод action. Исполняется при запуске пункта меню.
     * Сразу, если нет списка подменю, или после выхода из цикла выбора (выхода из меню)
     * По умолчанию метод ничего не делает
     */
    private final ItemActionMethod itemAction;

    /**
     * Интерфейс, нужно определить метод preparation. Исполняется первым при запуске меню. Позволяет подготовить меню.
     * По умолчанию метод ничего не делает
     */
    private final ItemPreparationMethod itemPreparation;



    /*
        Конструкторы
     */

    public MenuItem(String menuName, ItemActionMethod itemAction, ItemPreparationMethod itemPreparation) throws Exception {
        if (MenuItem.sc == null) throw new Exception("\n\n ОШИБКА !!!! Не установлен сканер."
                + "\n\tСначала добавьте общий сканер ConsoleMenu.setScanner(Scanner)\n\n");

        this.setMenuName(menuName);
        this.itemAction = itemAction;
        this.itemPreparation = itemPreparation;
    }

    public MenuItem(String menuName, ItemActionMethod itemAction) throws Exception {
        // По умолчанию preparation ничего не делает ничего не делает
        this(menuName, itemAction, (menuItem) -> {
        });
    }

    public MenuItem(String menuName, ItemPreparationMethod itemPreparation) throws Exception {
        // По умолчанию action ничего не делает ничего не делает
        this(menuName, () -> {
        }, itemPreparation);
    }

    public MenuItem(String menuName) throws Exception {
        // По умолчанию preparation и action ничего не делает ничего не делает
        this(menuName, () -> {
        }, (menuItem) -> {
        });
    }



    /*
        Геттеры и Сеттеры
     */

    public static void setScanner(Scanner sc) {
        MenuItem.sc = sc;
    }

    public final MenuItem getPrevMenuItem() {
        return prevMenuItem;
    }

    private void setPrevMenuItem(MenuItem prevMenuItem) {
        this.prevMenuItem = prevMenuItem;
    }

    public final MenuItem setMenuName(String s) {
        this.menuName = s;

        return this;
    }

    public final String getMenuName() {
        return this.menuName;
    }



    /*
        Методы
     */

    public MenuItem clearSubMenuList() {
        this.subMenuItemsList.clear();
        return this;
    }


    // Возвращает положение указателя, на главном(первом) меню или нет
    private boolean isMainMenu() {
        return this.getPrevMenuItem() == null;
    }


    // Добавляем пункт меню
    public final MenuItem add(MenuItem m) {
        this.subMenuItemsList.add(m);

        return this;
    }


    // Возвращает кол-во пунктов подменю в subMenuItemsList
    public final int getNumSubMenu() {
        return this.subMenuItemsList.size();
    }


    // получаем строку - путь по дереву меню
    public final StringBuffer getMenuNestingPath() {

        // Если мы в главном меню
        if (this.isMainMenu())
            return new StringBuffer().append(" * ").append(getMenuName()).append(" | ");

        // Если НЕ в главном меню
        return this.getPrevMenuItem().getMenuNestingPath().insert(3, this.getMenuName() + "  <  ");
    }


    // Выбор пункта меню из subMenuItemsList. Если пунктов нет, запусается метод run() (его нужно переопределить при заполненнии меню)
    public final void run(MenuItem prevMenuItem) throws Exception {

        // Сохраняем ссылку на предыдущий пункт меню
        this.setPrevMenuItem(prevMenuItem);

        // Выводим путь дерева меню
        System.out.println("\n" + "─".repeat(130) + "\n" + this.getMenuNestingPath() + "\n" + "─".repeat(130));

        // Получаем кол-во пунктов вложенного меню
        int countMenuItems = this.getNumSubMenu();

        // Цыкл выбора из списка подменю subMenuItemsList, если он не пустой
        while (countMenuItems != 0) {

            // Запускаем метод подготовки меню
            itemPreparation.preparation(this);

            // Выводим список меню
            for (int i = 0; i < countMenuItems; i++) {

                System.out.printf("%4s  " + (subMenuItemsList.get(i).getNumSubMenu() != 0 ? '>' : '-') + "  %s\n",
                        (i + 1), this.subMenuItemsList.get(i).getMenuName());
            }

            // Выводим сервисные комманды. Если мы в главном меню - exit, если в подменю - 0
            if (!this.isMainMenu()) {
                System.out.printf("%4s  -  %s\n", "0", "Вiдмiна / Повернення в попереднє меню");
            } else
                System.out.printf("%4s  -  %s\n", "exit", "Завершення сеансу");

            System.out.print("\nВведiть номер дiї: ");

            // Если введено число
            if (sc.hasNextInt()) {
                int n = sc.nextInt();
                sc.nextLine();

                // Если 0 и мы НЕ в главном меню - выходим иц цикла выбора
                if (n == 0 && !this.isMainMenu())
                    break;

                // Проверяем введенное число, не выходит ли за кол-во пунктов меню
                if (0 < n && n < countMenuItems + 1) {
                    subMenuItemsList.get(n - 1).run(this);

                } else
                    System.out.println("\n\t>>> ОШИБКА! Вы ввели: '" + n + "'. Введите номер команды из списка выше ");

            } else {    // Если введено НЕ число
                String s = sc.nextLine();

                // Введён текст - проверяем на наличие комманды закрытия программы, если мы в главном(первом) меню
                if (s.equalsIgnoreCase("exit") && this.isMainMenu())
                    break;

                System.out.println("\n\t>>> ОШИБКА! Комманда '" + s + "' не распознана.");
            }

            // Выводим путь дерева меню
            System.out.println("\n" + "─".repeat(130) + "\n" + this.getMenuNestingPath() + "\n" + "─".repeat(130));
        }

        // Запускаем метод пункта меню, после выполнения выходим в предыдущее меню
        this.itemAction.action();

    }


}

