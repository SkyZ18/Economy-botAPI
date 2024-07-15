package org.economy.api;

import org.economy.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserService {

    private static final BankService bankService = new BankService();
    private static final CashService cashService = new CashService();

    public String createUser(Connection connection, User user) {
        try {
            String sql = "INSERT INTO users(id, dc_tag, name) VALUES (?,?,?)";
            String checkUserSql = "SELECT * FROM users WHERE dc_tag=?";
            String sqlId = "SELECT MAX(id) FROM users";

            PreparedStatement pstmt = connection.prepareStatement(checkUserSql);
            pstmt.setString(1, user.getDc_tag());

            if(!pstmt.executeQuery().next()) {
                ResultSet userId = connection.createStatement().executeQuery(sqlId);
                userId.next();

                pstmt = connection.prepareStatement(sql);

                pstmt.setLong(1, userId.getLong(1) + 1);
                pstmt.setString(2, user.getDc_tag());
                pstmt.setString(3, user.getName());

                pstmt.executeUpdate();

                cashService.createAccount(connection, userId.getLong(1) + 1);
                bankService.createBankAccount(connection, userId.getLong(1) + 1);

                return "\nSuccessfully created user";
            } else {
                return "\nUser already exists";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return "\nFailed";
    }

    public ResultSet getAllUsers(Connection connection) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM users";

            Statement stmt = connection.createStatement();

            res = stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public ResultSet getUserById(Connection connection, Long id) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM users WHERE id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            res = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public ResultSet getUserByName(Connection connection, String name) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM users WHERE name=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, name);

            res = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public ResultSet getUserByTag(Connection connection, String tag) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM users WHERE dc_tag=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, tag);

            res = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public String removeUserAccount(Connection connection, Long id) {
        try {
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
            System.out.println("\n--------------USERS------------");
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
