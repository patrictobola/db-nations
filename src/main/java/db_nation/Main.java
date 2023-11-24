package db_nation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nation";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Cerca una parola: ");
            String searchString = scanner.nextLine();

            String query = "SELECT " +
                    "countries.country_id AS id, " +
                    "countries.name AS name, " +
                    "regions.name AS region, " +
                    "continents.name AS continent " +
                    "FROM countries " +
                    "JOIN regions ON countries.region_id = regions.region_id " +
                    "JOIN continents ON regions.continent_id = continents.continent_id " +
                    "WHERE countries.name LIKE ? " +
                    "ORDER BY countries.name";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + searchString + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String region = resultSet.getString("region");
                        String continent = resultSet.getString("continent");

                        System.out.println("ID: " + id + ", Name: " + name + ", Region: " + region + ", Continent: " + continent);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
