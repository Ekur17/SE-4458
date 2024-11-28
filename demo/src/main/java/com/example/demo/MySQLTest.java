package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/airbnb_project";
        String username = "airbnb_user";
        String password = "password123";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}