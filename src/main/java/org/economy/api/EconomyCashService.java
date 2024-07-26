package org.economy.api;

import org.checkerframework.checker.units.qual.C;
import org.economy.EconomyAPI;
import org.economy.config.Logger;
import org.economy.models.Cash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EconomyCashService {

    private static Connection connection = null;

    public void createAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sqlCash = "INSERT INTO cash(id, user_id, balance) VALUES(?,?,0)";
            String sqlIdCash = "SELECT MAX(id) FROM cash";

            ResultSet cashId = connection.createStatement().executeQuery(sqlIdCash);
            cashId.next();

            PreparedStatement pstmt = connection.prepareStatement(sqlCash);
            pstmt.setLong(1, cashId.getLong(1) + 1);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();

            Logger.log("Created cash-account", Logger.LogType.INFO);
        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);

        }
        Logger.log("Cash-account already exists", Logger.LogType.ERROR);
    }

    public void addMoneyToUser(Long id, Double amount) {
        try {
            connection = EconomyAPI.connection;
            String sql = "UPDATE cash SET balance=balance+? WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
            Logger.log("Added money to user", Logger.LogType.INFO);
        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }

    public void removeMoneyFromUser(Long id, Double amount) {
        try {
            connection = EconomyAPI.connection;
            String sql = "UPDATE cash SET balance=balance-? WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
            Logger.log("Removed money from user", Logger.LogType.INFO);
        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }

    public void removeCashAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sql = "DELETE FROM cash WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
            Logger.log("Deleted Cash-Account", Logger.LogType.INFO);
        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }

    public Cash returnCashAccount(Long id) {
        Cash cash = new Cash();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM cash WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            res.next();

            cash.setId(res.getLong(1));
            cash.setUser_id(res.getLong(2));
            cash.setBalance(res.getDouble(3));

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
        return cash;
    }
}
