package step.project.flight.flightDAO;

public enum NameOfCity {
    // список городов доступных для рейсов
    // при добавлении нового, города генератор базы автоматически подтянет их
    KYIV("Київ", "Європа"),
    VINNITSIA("Вiнниця", "Європа"),
    MYKOLAIV("Миколаїв", "Європа"),
    LVIV("Львiв", "Європа"),
    IVANOFRANK("iвано-Франкiвськ", "Європа"),
    UZHOROD("Ужгород", "Європа"),
    ODESA("Одеса", "Європа"),
    KRIVIYRIG("Кривий Рiг", "Європа"),
    ATLANTA("Атланта", "Пiвнiчна Америка"),
    BEIJING("Пекiн", "Азiя"),
    CHERNIVCI("Чернiвцi", "Європа"),
    WARSAWA("Варшава", "Європа"),
    KATOWICE("Катовiце", "Європа"),
    PARIS("Париж", "Європа"),
    LONDON("Лондон", "Європа"),
    CHICAGO("Чикаго", "Пiвнiчна Америка"),
    DUBAI("Дубаї", "Азiя"),
    NEWYORK("Нью-Йорк", "Пiвнiчна Америка"),
    ISTANBUL("Стамбул", "Європа"),
    SEOUL("Сеул", "Європа"),
    MADRID("Мадрид", "Європа"),
    LASVEGAS("Лас-Вегас", "North America"),
    MIAMI("Маямi", "North America"),
    BARCELONA("Барселона", "Європа"),
    TOKYO("Токiо", "Азiя"),
    HONGONG("Гонконг", "Азiя"),
    SHRILANKA("Шрi-Ланка", "Азiя"),
    TAIBEI("Тайбей", "Азiя"),
    PHENYAY("Пхенянь", "Азiя"),
    ANTALYA("Анталiя", "Європа");

    public final String nameOfCity;
    public final String continent;

    NameOfCity(String nameOfCity, String continent) {
        this.nameOfCity = nameOfCity;
        this.continent = continent;
    }

    public static NameOfCity getEnumByValue(String s) {
        // Убираем пробелы снаружи и меняем внутри повторяющиеся на одинарные
        s = s.trim();

        // Меняем внутри повторяющиеся пробелы на одинарные
        while (s.contains("  "))
            s = s.replace("  ", " ");

        // Находим с нужным названием и возвращаем
        for (NameOfCity nameOfCity : NameOfCity.values())
            if (nameOfCity.nameOfCity.equalsIgnoreCase(s))
                return nameOfCity;

        // Возвращаем null если не нашли
        return null;
    }
}
