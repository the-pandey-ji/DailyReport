package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.DB.DBConnect;
import com.entity.User;

public class UserDAO {

    public User login(String username, String password) {

        User user = null;

        try {
            Connection con = DBConnect.getConnection();
            String sql =
              "SELECT * FROM GH_USER WHERE USERNAME=? AND PASSWORD=? AND ACTIVE='Y'";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setUsername(rs.getString("USERNAME"));
                user.setFullName(rs.getString("FULL_NAME"));
                user.setActive(rs.getString("ACTIVE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
