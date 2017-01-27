package com.java_3.lesson_2;

import org.sqlite.JDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Created by Валентин Фалин on 27.01.2017.
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
            for (int i = 1; i < 10; i++) {
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
                            //команда
                            System.out.println(2);
                            break;
                        case "/товарыпоцене":
                            //команда
                            System.out.println(3);
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

    private static void price(String fullCommand) {
        ResultSet resultSet;
        int spase = fullCommand.indexOf(" ");
        String title = fullCommand.substring(spase + 1);
        try {
            preparedStatement = connection.prepareStatement("SELECT price FROM " + TABLE_NAME + " WHERE title = ?");
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Цена товара " + title + " - " + resultSet.getString(1));
            } else {
                System.out.println("Неверное название товара, либо такого товара не существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
