package service;

import model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import properties.AccountProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountService {

    private int idCounterAccount = 0;
    private final Map<Integer, Account> accountStorage = new HashMap<>();
    private final AccountProperties accountProperties;


    @Autowired
    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        accountStorage.forEach((key, value) -> {
            accounts.add(value);
        });
        return accounts;
    }

    public Account createAccount(int userId) {
        idCounterAccount++;
        Account account;
        if (accountStorage.containsKey(userId)) {
            account = new Account(idCounterAccount, userId, 0);
        } else {
            account = new Account(idCounterAccount, userId, accountProperties.getDefaultAmount());
        }
        accountStorage.put(userId, account);

        return account;
    }


}