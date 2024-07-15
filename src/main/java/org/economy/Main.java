package org.economy;

import com.github.lalyos.jfiglet.FigletFont;
import org.economy.api.BankService;
import org.economy.api.CashService;
import org.economy.api.UserService;
import org.economy.config.JSONParserService;
import org.economy.config.PasswordDecoder;
import org.economy.config.SQLFileReader;
import org.economy.connection.DatabaseConnection;
import org.economy.models.JSON.JSONData;
import org.economy.models.Metadata;
import org.economy.models.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class Main {

    private static final JSONParserService parser = new JSONParserService();
    private static final SQLFileReader sqlReader = new SQLFileReader();
    private static final PasswordDecoder decoder = new PasswordDecoder();
    private static final JSONData obj = parser.readJSON();

    private static final UserService userService = new UserService();
    private static final CashService cashService = new CashService();
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {
        System.out.println(FigletFont.convertOneLine("ECONOMY-API"));

        String url =
                "jdbc:mariadb://"
                        + obj.getEnv().getDb().getMariaDbHost()
                        + obj.getEnv().getDb().getMariaDbPort()
                        + obj.getEnv().getDb().getMariaDbDatabase();

        DatabaseConnection dbConn = new DatabaseConnection(
                url,
                obj.getEnv().getDb().getMariaDbUser(),
                decoder.decodePassword(obj.getEnv().getDb().getMariaDbPassword())
        );

        try (Connection connection = dbConn.openConn()) {
            DatabaseMetaData data = connection.getMetaData();

            Metadata metadata = new Metadata(
                    connection.getCatalog(),
                    obj.getName(),
                    obj.getDescription(),
                    data.getDriverName(),
                    data.getDriverVersion(),
                    data.getURL(),
                    data.getDatabaseMajorVersion(),
                    data.getUserName(),
                    data.getConnection().isClosed()
            );

            System.out.println(metadata.getMetadata());
            sqlReader.runScript(connection, obj.getEnv().getPathToSql());

            User user = User.builder()
                    .dc_tag("#123123")
                    .name("testUser")
                    .build();

            System.out.println(userService.createUser(connection, user));

            userService.printAllUsers(userService.getAllUsers(connection));
            cashService.printCash(cashService.returnCashAccount(connection, 1L));
            bankService.printAccount(bankService.returnAccountOfUser(connection, 1L));

            System.out.println(bankService.withdrawMoneyFromAccount(connection, 1L, 300D));

            cashService.printCash(cashService.returnCashAccount(connection, 1L));
            bankService.printAccount(bankService.returnAccountOfUser(connection, 1L));

            System.out.println(bankService.depositMoneyToAccount(connection, 1L, 300D));

            cashService.printCash(cashService.returnCashAccount(connection, 1L));
            bankService.printAccount(bankService.returnAccountOfUser(connection, 1L));

            cashService.removeCashAccount(connection, 1L);
            bankService.removeBankAccount(connection, 1L);
            userService.removeUserAccount(connection, 1L);

            while (true) {
                if (dbConn.healthCheck(connection)) {
                    try {
                        System.out.println("Reconnect to DB");
                        dbConn.openConn();
                    } catch (SQLException e) {
                        throw new SQLTimeoutException(e);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}