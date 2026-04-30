package service;

import model.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private int idCounterAccount = 0;
    private final Map<Integer, Account> accountStorage = new HashMap<>();

    public Account createAccount(int userId, int moneyAmount) {
        idCounterAccount++;
        Account account = new Account(idCounterAccount, userId, moneyAmount);
        accountStorage.put(idCounterAccount, account);
        return account;
    }

    public boolean isAccountExist(int userId) {
        return accountStorage.containsKey(userId);
    }

    public int AccountDeposit(int idAccount, int money) {
        Account account = accountStorage.get(idAccount);
        if (money > 0 && account != null) {
            int currentBalance = account.getMoneyAmount();
            int deposit = money + currentBalance;
            account.setMoneyAmount(deposit);
            return deposit;
        }
        return 0;
    }

    public int getIdAccount() {
        return idCounterAccount;
    }

    public Map<Integer, Account> getAccountStorage() {
        return accountStorage;
    }

    public int AccountWithdraw(int idAccount, int money) throws Exception {
        Account account = accountStorage.get(idAccount);
        if (account == null) {
            throw new IllegalArgumentException("Account with id=" + idAccount + " not found");
        }
        if (account.getMoneyAmount() >= money) {
            int currentBalance = account.getMoneyAmount();
            int withdraw = currentBalance - money;
            account.setMoneyAmount(withdraw);
            return account.getMoneyAmount();

        } else {
            System.out.printf("Error: Insufficient funds on account id=%d, balance=%d, attempted withdraw=%d%n",
                    idAccount, account.getMoneyAmount(), money);
        }
        return account.getMoneyAmount();
    }
}