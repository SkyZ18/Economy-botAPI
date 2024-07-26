package org.economy.api;

import org.economy.EconomyAPI;
import org.economy.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EconomyCashService {

    private static Connection connection = null;

    public EconomyCashService() {
        try {
            connection = DatabaseConnection.openConn(EconomyAPI.url, EconomyAPI.user, EconomyAPI.password);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public String createAccount(Long id) {
        try {
            String sqlCash = "INSERT INTO cash(id, user_id, balance) VALUES(?,?,0)";
            String sqlIdCash = "SELECT MAX(id) FROM cash";

            ResultSet cashId = connection.createStatement().executeQuery(sqlIdCash);
            cashId.next();

            PreparedStatement pstmt = connection.prepareStatement(sqlCash);
            pstmt.setLong(1, cashId.getLong(1) + 1);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();

            return "\nCreated cash-account";
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nCash-account already exists";
    }

    public String addMoneyToUser(Long id, Double amount) {
        try {
            String sql = "UPDATE cash SET balance=balance+? WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nAdded " + amount + "$ to User: ";
    }

    public String removeMoneyFromUser(Long id, Double amount) {
        try {
            String sql = "UPDATE cash SET balance=balance-? WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nRemoved " + amount + "$ from User: ";
    }

    public void removeCashAccount(Long id) {
        try {
            String sql = "DELETE FROM cash WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public ResultSet returnCashAccount(Long id) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM cash WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            res = pstmt.executeQuery();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public void printCash(ResultSet resultSet) {
        try {
            System.out.println("\n--------------CASH-------------");
            while (resultSet.next()) {
                System.out.println("CASH-" + resultSet.getLong(1) + ": "
                        + "\n user_id: " + resultSet.getLong(2)
                        + "\n balance: " + resultSet.getDouble(3) + "$"
                );
            }
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
