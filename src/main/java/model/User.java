package model;

import java.util.List;

public class User {
    private final int id;
    private final String login;
    private final List<Account> accountList;

    public User(int counterUserId, String login, List<Account> accounts) {
        this.id = counterUserId;
        this.login = login;
        this.accountList = accounts;

    }

    public void addAccount(Account account) {
        this.accountList.add(account);
    }

    public int getId() {
        return id;
    }

    public List<Account> getAccounts() {
        return accountList;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}