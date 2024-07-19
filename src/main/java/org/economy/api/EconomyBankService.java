package org.economy.api;

import java.sql.*;

public class EconomyBankService {

    private static final EconomyCashService ECONOMY_CASH_SERVICE = new EconomyCashService();

    public String createBankAccount(Connection connection, Long id) {
        try {
            String sql = "INSERT INTO bank(id, user_id, balance, loan) VALUES(?,?,300,null)";
            String sqlId = "SELECT MAX(id) FROM bank";

            ResultSet res = connection.createStatement().executeQuery(sqlId);

            if(res.next()) {
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setLong(1, res.getLong(1) + 1);
                pstmt.setLong(2, id);

                pstmt.executeUpdate();

                return "\nSuccessfully created bank-account";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nBank-account already exists";
    }

    public ResultSet returnAccountOfUser(Connection connection, Long id) {
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM bank WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            res = pstmt.executeQuery();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return res;
    }

    public void removeBankAccount(Connection connection, Long id) {
        try {
            String sql = "DELETE FROM bank WHERE user_id=?";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public String depositMoneyToAccount(Connection connection, Long id, Double amount) {
        try {
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

                ECONOMY_CASH_SERVICE.removeMoneyFromUser(connection, id, amount);

                return "\nDeposited " + amount + "$";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nNot enough money";
    }

    public String withdrawMoneyFromAccount(Connection connection, Long id, Double amount) {
        try {
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

                ECONOMY_CASH_SERVICE.addMoneyToUser(connection, id, amount);

                return "\nWithdraw " + amount + "$";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "\nNot enough money on account";
    }

    public void printAccount(ResultSet resultSet) {
        try {
            resultSet.next();
            System.out.println("\n--------------BANK-------------");
            System.out.println("BANK-" + resultSet.getLong(1) + ": "
                    + "\n user_id: " + resultSet.getLong(2)
                    + "\n balance: " + resultSet.getDouble(3) + "$"
                    + "\n loan: " + resultSet.getDouble(4) + "$"
            );
            System.out.println("-------------------------------");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

}
