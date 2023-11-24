package org.java;

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
            Scanner in = new Scanner(System.in);
            System.out.print("Cerca una parola: ");
            String searchString = in.nextLine();

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

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, "%" + searchString + "%");

                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String region = resultSet.getString("region");
                        String continent = resultSet.getString("continent");

                        System.out.println("ID: " + id + ", Name: " + name + ", Region: " + region + ", Continent: " + continent);
                    }
                }
            } 
            System.out.print("Inserisci l'ID di una country: ");
            int selectedCountryId = in.nextInt();

            String languageQuery = "SELECT languages.language " +
                    "FROM country_languages " +
                    "JOIN languages ON country_languages.language_id = languages.language_id " +
                    "WHERE country_languages.country_id = ?";

            try (PreparedStatement languagePs = connection.prepareStatement(languageQuery)) {
                languagePs.setInt(1, selectedCountryId);

                try (ResultSet languageResultSet = languagePs.executeQuery()) {
                    System.out.println("Lingue parlate in quella country:");
                    while (languageResultSet.next()) {
                        String languageName = languageResultSet.getString("language");
                        System.out.println(languageName);
                    }
                }
            }

            String statsQuery = "SELECT * " +
                    "FROM country_stats " +
                    "WHERE country_id = ? " +
                    "ORDER BY year DESC " +
                    "LIMIT 1";
            try (PreparedStatement countryPs = connection.prepareStatement(statsQuery)) {
                countryPs.setInt(1, selectedCountryId);

                try (ResultSet countryResultSet = countryPs.executeQuery()) {
                    System.out.println("\nStatistiche più recenti per quella country:");
                    while (countryResultSet.next()) {

                        int statsId = countryResultSet.getInt("country_id");
                        String statsYear = countryResultSet.getString("year");
                        int statsPopulation = countryResultSet.getInt("population");
                        String statsgdp = countryResultSet.getString("gdp");

                        System.out.println("Stats ID: " + statsId + ", Stats Date: " + statsYear + ", Country Population: " + statsPopulation + ", GDP :" + statsgdp);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
