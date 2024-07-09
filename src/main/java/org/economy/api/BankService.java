package org.economy.api;

public class BankService {

    public String depositMoneyToAccount(Long id, Double amount) {

        return "Deposited " + amount + "$";
    }

    public String withdrawMoneyFromAccount(Long id, Double amount) {

        return "Withdraw " + amount + "$";
    }

}
