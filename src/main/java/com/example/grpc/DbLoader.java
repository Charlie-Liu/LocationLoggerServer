package com.example.grpc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

import com.example.grpc.LocationInfo;

public class DbLoader {

    static String url = "jdbc:mysql://localhost:3306/locationloggerdb?useSSL=false";
    static String username = "locationlogger";
    static String password = "password";

    Connection con;
    Statement stmt;

    public DbLoader() {

        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DbLoader.url, DbLoader.username, DbLoader.password);
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addLocation(String address, double latitude, double longitude, String strTime) {
        String locationAddSql = "INSERT INTO location (address, latitude, longitude, time) " +
                                String.format("VALUE ('%s', '%f', '%f', '%s');",
                                    address,
                                    latitude,
                                    longitude,
                                    strTime);
        try {
            stmt.executeUpdate(locationAddSql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<LocationInfo> listLocation(String strTime, int num) {
        String locationListSql = "SELECT address, latitude, longitude, time FROM location " +
                                 "WHERE time < " +
                                 String.format("TIMESTAMP(\"%s\") ", strTime) +
                                 "ORDER BY time DESC " +
                                 "LIMIT " + String.valueOf(num);
        ArrayList<LocationInfo> locations = new ArrayList<>();
        try {
            ResultSet resultSet = stmt.executeQuery(locationListSql);
            while (resultSet.next()) {
                LocationInfo location = LocationInfo.newBuilder()
                    .setAddress(resultSet.getString(1))
                    .setLatitude(resultSet.getDouble(2))
                    .setLongitude(resultSet.getDouble(3))
                    .setTime(resultSet.getString(4))
                    .build();
                locations.add(location);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return locations;
    }

    public void endConnetion() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
