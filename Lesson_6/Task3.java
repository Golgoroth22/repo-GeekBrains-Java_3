import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Валентин Фалин on 26.02.2017.
 */
public class Task3 {
    private static Connection connection;
    private static PreparedStatement prepStmt;
    private static Statement stmt;
    private static String STATEMENT_FOR_ADD = "INSERT INTO Students (surname, grade) VALUES (?, ?);";
    private static String STATEMENT_FOR_UPDATE = "UPDATE Students SET grade = %d WHERE surname = '%s';";
    private static String STATEMENT_FOR_DELETE = "DELETE FROM Students WHERE surname = '%s';";
    private static String STATEMENT_FOR_INFO = "SELECT id, surname, grade FROM Students WHERE surname = ?;";

    public static void main(String[] args) {
        Task3 task3 = new Task3();
        task3.connect();

        ArrayList<String> arrayList = task3.studentInfo("Wayne");
        for (String s : arrayList) {
            System.out.println(s);
        }

        task3.disconnect();
    }

    public ArrayList<String> studentInfo(String surname) {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet resultSet;
        try {
            prepStmt = connection.prepareStatement(STATEMENT_FOR_INFO);
            prepStmt.setString(1, surname);
            resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getInt(1)+ " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteStudent(String surname) {
        try {
            stmt = connection.createStatement();
            String curStmt = String.format(STATEMENT_FOR_DELETE, surname);
            stmt.executeUpdate(curStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(String surname, int grade) {
        try {
            stmt = connection.createStatement();
            String curStmt = String.format(STATEMENT_FOR_UPDATE, grade, surname);
            stmt.executeUpdate(curStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStudent(String surname, int grade) {
        try {
            prepStmt = connection.prepareStatement(STATEMENT_FOR_ADD);
            prepStmt.setString(1, surname);
            prepStmt.setInt(2, grade);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC.PREFIX + "DBForTests.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
