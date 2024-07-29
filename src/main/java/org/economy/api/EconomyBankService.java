package org.economy.api;

import org.economy.EconomyAPI;
import org.economy.config.Logger;
import org.economy.models.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EconomyBankService {

    private static final EconomyCashService ECONOMY_CASH_SERVICE = new EconomyCashService();
    private static Connection connection = null;

    public void createBankAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sql = "INSERT INTO bank(id, user_id, balance, loan) VALUES(?,?,300,null)";
            String sqlId = "SELECT MAX(id) FROM bank";

            ResultSet res = connection.createStatement().executeQuery(sqlId);

            if(res.next()) {
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setLong(1, res.getLong(1) + 1);
                pstmt.setLong(2, id);

                pstmt.executeUpdate();

                Logger.log("Created bank-account", Logger.LogType.INFO);
            }

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
        Logger.log("Bank-account already exists", Logger.LogType.ERROR);
    }

    public Bank returnAccountOfUser(Long id) {
        Bank bank = new Bank();
        try {
            connection = EconomyAPI.connection;
            String sql = "SELECT * FROM bank WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            res.next();

            bank.setId(res.getLong(1));
            bank.setUser_id(res.getLong(2));
            bank.setBalance(res.getDouble(3));
            bank.setLoan(res.getDouble(4));

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return bank;
    }

    public void removeBankAccount(Long id) {
        try {
            connection = EconomyAPI.connection;
            String sql = "DELETE FROM bank WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
            Logger.log("Deleted bank-account", Logger.LogType.INFO);

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }

    public void depositMoneyToAccount(Long id, Double amount) {
        try {
            connection = EconomyAPI.connection;
            String sql = "UPDATE bank SET balance=balance+? WHERE user_id=?";
            String sqlBalance = "SELECT balance FROM cash WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sqlBalance);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            res.next();

            if (res.getDouble(1) >= amount) {
                pstmt = connection.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setLong(2, id);

                pstmt.executeUpdate();

                ECONOMY_CASH_SERVICE.removeMoneyFromUser(id, amount);
            }

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }

    public void withdrawMoneyFromAccount(Long id, Double amount) {
        try {
            connection = EconomyAPI.connection;
            String sql = "UPDATE bank SET balance=balance-? WHERE user_id=?";
            String sqlBalance = "SELECT balance FROM bank WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sqlBalance);
            pstmt.setDouble(1, id);

            ResultSet res = pstmt.executeQuery();
            res.next();

            if (res.getLong(1) >= amount) {
                pstmt = connection.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setLong(2, id);

                pstmt.executeUpdate();

                ECONOMY_CASH_SERVICE.addMoneyToUser(id, amount);
            }

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }
}
