package service;

import model.Account;
import model.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<Integer, User> userStorage = new HashMap<>();
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final AccountService accountService;

    private int counterUserId = 0;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean getUserId(int id) {
        if (userStorage.containsKey(id)) {
            return true;
        } else {
            System.out.println("User not found");
        }
        return false;
    }

    public int getIdCounter() {
        return counterUserId;
    }

    public List<Account> getAccountList() {
        return accountService.getAccounts();
    }

    public User createUser(String login) {
        counterUserId++;
        User user = new User(counterUserId, login, getAccountList());
        userStorage.put(counterUserId, user);

        return user;
    }


}