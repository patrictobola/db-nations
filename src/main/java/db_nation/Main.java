package db_nation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class Main {

	public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nation";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT " +
                    "countries.country_id AS id, " +
                    "countries.name AS name, " +
                    "regions.name AS region, " +
                    "continents.name AS continent " +
                    "FROM countries " +
                    "JOIN regions ON countries.region_id = regions.region_id " +
                    "JOIN continents ON regions.continent_id = continents.continent_id " +
                    "ORDER BY countries.name";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String region = resultSet.getString("region");
                    String continent = resultSet.getString("continent");

                    System.out.println("ID: " + id + ", Name: " + name + ", Region: " + region + ", Continent: " + continent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
