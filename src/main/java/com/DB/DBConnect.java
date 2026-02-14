package com.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static Connection conn;

    public static Connection getConnection() {

        try {
            if (conn == null || conn.isClosed()) {

                Class.forName("oracle.jdbc.driver.OracleDriver");

                String url = "jdbc:oracle:thin:@//10.3.111.120:1521/ORCL";

                conn = DriverManager.getConnection(
                        url,
                        "PERSONNEL",
                        "PERSONNEL"
                );

                System.out.println("Oracle DB Connected Successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
