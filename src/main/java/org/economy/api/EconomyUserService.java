package org.economy.api;

import org.economy.EconomyAPI;
import org.economy.config.Logger;
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

    public void createUser(Long id, String dc_tag, String name) {
        try {
            connection = EconomyAPI.connection;
            String sql = "INSERT INTO users(id, dc_tag, name) VALUES (?,?,?)";
            String checkUserSql = "SELECT * FROM users WHERE id=?";

            PreparedStatement pstmt = connection.prepareStatement(checkUserSql);
            pstmt.setLong(1, id);

            if(!pstmt.executeQuery().next()) {
                pstmt = connection.prepareStatement(sql);

                pstmt.setLong(1, id);
                pstmt.setString(2, dc_tag);
                pstmt.setString(3, name);

                pstmt.executeUpdate();

                ECONOMY_CASH_SERVICE.createAccount(id);
                ECONOMY_BANK_SERVICE.createBankAccount(id);

                Logger.log("Successfully created user", Logger.LogType.INFO);
            } else {
                Logger.log("User already exists", Logger.LogType.ERROR);
            }

        } catch (Exception e) {
            Logger.log("Error in creating user", Logger.LogType.ERROR);
        }
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
            Logger.log("Error: " + e, Logger.LogType.ERROR);
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
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
        return user;
    }

    public User getUserByName(String name) {
        User user = new User();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users WHERE name=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, name);

            ResultSet res = pstmt.executeQuery();
            res.next();

            user.setId(res.getLong(1));
            user.setDc_tag(res.getString(2));
            user.setName(res.getString(3));

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
        return user;
    }

    public User getUserByTag(String tag) {
        User user = new User();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM users WHERE dc_tag=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, tag);

            ResultSet res = pstmt.executeQuery();
            res.next();

            user.setId(res.getLong(1));
            user.setDc_tag(res.getString(2));
            user.setName(res.getString(3));

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
        return user;
    }

    public void removeUserAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sql = "DELETE FROM users WHERE id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            ECONOMY_BANK_SERVICE.removeBankAccount(id);
            ECONOMY_CASH_SERVICE.removeCashAccount(id);

            pstmt.executeUpdate();

            Logger.log("Successfully removed user", Logger.LogType.INFO);
        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }
}
