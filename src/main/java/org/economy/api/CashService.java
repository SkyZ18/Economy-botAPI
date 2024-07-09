package org.economy.api;

public class CashService {

    public String addMoneyToUser(Long id, Double amount) {

        return "Added " + amount + "$ to User: ";
    }

    public String removeMoneyFromUser(Long id, Double amount) {

        return "Removed " + amount + "$ from User: ";
    }
}
