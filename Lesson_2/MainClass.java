package java_3.lesson_2;

import org.sqlite.JDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Created by Валентин Фалин on 27.01.2017.
 * Урок 2. Базы Данных.
 * Задание:
 * - Сформировать таблицу товаров (id, prodid, title, cost) запросом из Java
 *   приложения.
 *   id - порядковый номер записи, первичный ключ
 *   prodid - уникальный номер товара
 *   title - название товара
 *   cost - стоимость
 * - При запуске приложения очистить таблицу и заполнить 10.000 товаров вида:
 *   id_товара 1 товар1 10
 *   id_товара 2 товар2 20
 *   id_товара 3 товар3 30
 *   ...
 *   id_товара 10000 товар10000 100010
 *   т.е. просто тестовые данные
 * - Написать консольное приложение, которое позволяет узнать цену товара по
 *   его имени, либо если такого товара нет, то должно быть выведено сообщение
 *   "Такого товара нет". Пример консольной комманды для получения цены:
 *   "/цена товар545"
 * - В этом же приложении должна быть возможность изменения цены
 *   товара(указываем имя товара и новую цену). Пример: "/сменитьцену товар10
 *   10000"
 * - Вывести товары в заданном ценовом диапазоне. Консольная комманда:
 *   "/товарыпоцене 100 600"
 */
public class MainClass {
    private static String TABLE_NAME = "Products";
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static BufferedReader reader;

    public static void main(String[] args) {
        connect();
        try {
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (prodid, title, price) VALUES (?, ?, ?);");
            statement.execute("DELETE FROM " + TABLE_NAME);
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " prodid INTEGER UNIQUE NOT NULL, title TEXT NOT NULL, price INTEGER NOT NULL);");
            connection.setAutoCommit(false);
            for (int i = 1; i < 10000; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "товар" + i);
                preparedStatement.setInt(3, i * 10);
                preparedStatement.executeUpdate();
            }
            connection.commit();

            reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Запросы:\n/цена ТОВАР\n/сменитьцену ТОВАР НОВАЯ_ЦЕНА\n/товарыпоцене ЦЕНА_ОТ ЦЕНА_ДО\n/выход");
                String fullCommand = reader.readLine();
                if (fullCommand.equals("/выход")) {
                    disconnect();
                    System.exit(0);
                } else if (fullCommand.contains(" ") && fullCommand.length() != 1) {
                    int spase = fullCommand.indexOf(" ");
                    String command = fullCommand.substring(0, spase);
                    switch (command) {
                        case "/цена":
                            price(fullCommand);
                            break;
                        case "/сменитьцену":
                            changePrice(fullCommand);
                            break;
                        case "/товарыпоцене":
                            titlesInRange(fullCommand);
                            break;
                        default:
                            System.out.println("Неверный запрос.");
                            break;
                    }
                } else {
                    System.out.println("Неверный запрос.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    private static void titlesInRange(String fullCommand) {
        ResultSet resultSet;
        String[] rerultSetArr = fullCommand.split(" ");
        if (rerultSetArr.length > 2) {
            if (checkStringToInt(rerultSetArr[1]) && checkStringToInt(rerultSetArr[2])) {
                try {
                    preparedStatement = connection.prepareStatement("SELECT title FROM " + TABLE_NAME + " WHERE (price > ?) AND (price < ?);");
                    preparedStatement.setInt(1, Integer.parseInt(rerultSetArr[1]) - 1);
                    preparedStatement.setInt(2, Integer.parseInt(rerultSetArr[2]) + 1);
                    resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        System.out.println("Товаров по цене от " + rerultSetArr[1] + " до " + rerultSetArr[2] + " нет.");
                    } else {
                        System.out.println("Товары по цене от " + rerultSetArr[1] + " до " + rerultSetArr[2] + ":");
                        while (resultSet.next()) {
                            System.out.println(resultSet.getString(1));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Некоректно введен диапазон цен.");
            }
        } else {
            System.out.println("Некоректно введен диапазон цен.");
        }
    }

    //Метод для смены цены
    private static void changePrice(String fullCommand) {
        String[] rerultSetArr = fullCommand.split(" ");
        if (checkTitle(rerultSetArr[1])) {
            if (checkStringToInt(rerultSetArr[2])) {
                try {
                    statement.executeUpdate("UPDATE " + TABLE_NAME + " SET price = '" + rerultSetArr[2] + "' WHERE title = '" + rerultSetArr[1] + "';");
                    connection.commit();
                    System.out.println("Цена товара " + rerultSetArr[1] + " успешно изменена на " + rerultSetArr[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Неверно введена новая цена.");
            }
        } else {
            System.out.println("Неверное название товара, либо такого товара нет в базе.");
        }
    }

    public static boolean checkStringToInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Метод для поиска по цене
    private static void price(String fullCommand) {
        ResultSet resultSet;
        int spase = fullCommand.indexOf(" ");
        String title = fullCommand.substring(spase + 1);
        try {
            preparedStatement = connection.prepareStatement("SELECT price FROM " + TABLE_NAME + " WHERE title = ?;");
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Цена товара " + title + " - " + resultSet.getString(1));
            } else {
                System.out.println("Неверное название товара, либо такого товара нет в базе.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Метод для проверки, если такой товар в базе
    public static boolean checkTitle(String title) {
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("SELECT title FROM " + TABLE_NAME + " WHERE title = ?;");
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC.PREFIX + "MainDB.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
