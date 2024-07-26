package org.economy.api;

import org.economy.EconomyAPI;
import org.economy.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EconomyUserService {

    private static final EconomyBankService ECONOMY_BANK_SERVICE = new EconomyBankService();
    private static final EconomyCashService ECONOMY_CASH_SERVICE = new EconomyCashService();

    private static Connection connection;

    public String createUser(String dc_tag, String name) {
        try {
            connection = EconomyAPI.connection;
            String sql = "INSERT INTO users(id, dc_tag, name) VALUES (?,?,?)";
            String checkUserSql = "SELECT * FROM users WHERE dc_tag=?";
            String sqlId = "SELECT MAX(id) FROM users";

            PreparedStatement pstmt = connection.prepareStatement(checkUserSql);
            pstmt.setString(1, dc_tag);

            if(!pstmt.executeQuery().next()) {
                ResultSet userId = connection.createStatement().executeQuery(sqlId);
                userId.next();

                pstmt = connection.prepareStatement(sql);

                pstmt.setLong(1, userId.getLong(1) + 1);
                pstmt.setString(2, dc_tag);
                pstmt.setString(3, name);

                pstmt.executeUpdate();

                ECONOMY_CASH_SERVICE.createAccount(userId.getLong(1) + 1);
                ECONOMY_BANK_SERVICE.createBankAccount(userId.getLong(1) + 1);

                return "\nSuccessfully created user";
            } else {
                return "\nUser already exists";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return "\nFailed";
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users";

            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                User user = User.builder()
                        .id(res.getLong(1))
                        .dc_tag(res.getString(2))
                        .name(res.getString(3))
                        .build();
                userList.add(user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return userList;
    }

    public User getUserById(Long id) {
        User user = new User();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users WHERE id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            res.next();

            user.setId(res.getLong(1));
            user.setDc_tag(res.getString(2));
            user.setName(res.getString(3));

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return user;
    }

    public ResultSet getUserByName(String name) {
        ResultSet res = null;
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users WHERE name=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, name);

            res = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public ResultSet getUserByTag(String tag) {
        ResultSet res = null;
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users WHERE dc_tag=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, tag);

            res = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public String removeUserAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sql = "DELETE FROM users WHERE id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();

            return "\nSuccessfully removed user";
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return "\nFailed";
    }

    public void printAllUsers(ResultSet resultSet) {
        try {
            System.out.println("\n--------------USER-------------");
            while (resultSet.next()) {
                System.out.println("USER-" + resultSet.getLong(1) + ": "
                        + "\n dc-tag: " + resultSet.getString(2)
                        + "\n name: " + resultSet.getString(3)
                );
            }
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

}
