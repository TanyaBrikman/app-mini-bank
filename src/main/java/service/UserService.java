package service;

import model.Account;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import properties.AccountProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<Integer, User> userStorage = new HashMap<>();
    private final AccountService accountService;
    private final AccountProperties accountProperties;
    private int counterUserId = 0;

    @Autowired
    public UserService(AccountService accountService, AccountProperties accountProperties) {
        this.accountService = accountService;
        this.accountProperties = accountProperties;
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.values());
    }

    public boolean isLoginUserExist(String login) {
        for (User user : userStorage.values()) {
            if (login.equals(user.getLogin())) {
                return true;
            }
        }
        return false;
    }

    public User findByAccountId(int accountId) {
        return userStorage.values().stream()
                .filter(user -> user.getAccounts().stream().anyMatch(acc -> acc.getId() == accountId))
                .findFirst()
                .orElse(null);
    }

    public User createUser(String login) {
        if (login == null || login.trim().isEmpty()) {
            System.out.println("Login cannot be empty!");
            return null;
        }
        if (isLoginUserExist(login)) {
            System.out.println("User with login '" + login + "' already exists!");
            return null;
        }
        counterUserId++;
        List<Account> accounts = new ArrayList<>();
        Account defaultAccount = accountService.createAccount(counterUserId, accountProperties.getDefaultAmount());
        accounts.add(defaultAccount);
        User user = new User(counterUserId, login, accounts);
        userStorage.put(counterUserId, user);
        return user;
    }

    public boolean isUserStorageExist() {
        return !userStorage.isEmpty();
    }

    public void addAccountToUser(int userId) throws Exception {
        User user = userStorage.get(userId);

        if (user == null) {
            throw new Exception("User not found!");
        }
        Account account = accountService.createAccount(userId, 0);
        user.addAccount(account);
        System.out.println("Account create: " + account);
    }

    public void transfer(int fromAccountId, int toAccountId, int amount) {
        Account accountFrom = accountService.getAccountStorage().get(fromAccountId);
        if (accountFrom == null) {
            throw new IllegalArgumentException("Account with id=" + fromAccountId + " not found");
        }
        Account accountTo = accountService.getAccountStorage().get(toAccountId);
        if (accountTo == null) {
            throw new IllegalArgumentException("Account with id=" + toAccountId + " not found");
        }
        if (amount > accountFrom.getMoneyAmount()) {
            throw new IllegalArgumentException("Not enough money to transfer from account: id=%s, moneyAmount=%s, attemptedTransfer=%s"
                    .formatted(accountFrom.getId(),
                            accountFrom.getMoneyAmount(), amount)
            );
        }

        User userFrom = findByAccountId(fromAccountId);
        User userTo = findByAccountId(toAccountId);

        boolean isDifferentUsers = (userFrom != null && userTo != null && userFrom.getId() != userTo.getId());

        var commission = (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));

        int amountToReceive = isDifferentUsers ? commission : amount;

        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToReceive);
        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);
        int result = amount - amountToReceive;

        if (isDifferentUsers) {
            System.out.println("Transfer completed from account" + fromAccountId + " to account " + toAccountId + ". Amount: " + amount + " (commission =" +
                    commission + " recipient received:" + result + ".");
        } else {
            System.out.println("Transfer completed from account" + fromAccountId + " to account " + toAccountId + ". Amount: " + amount + " (no commission," +
                    "same user)");
        }
    }

    public void closeAccount(int accountId) {
        Account account = accountService.getAccountStorage().get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account with id=" + accountId + " not found");
        }

        User user = findByAccountId(accountId);
        if (user == null) {
            throw new IllegalArgumentException("Account with id=" + accountId + " not found");
        }

        //Проверка пользователя, что у него есть другой счет
        List<Account> userAccounts = user.getAccounts();
        if (userAccounts.size() <= 1) {
            throw new IllegalStateException("Cannot close the only account. User must have at least one account");
        }

        // Находим счет для перевода остатка
        Account targetAccount = user.getAccounts().stream()
                .filter(acc -> acc.getId() != accountId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No other account available for transfer"));
        //Переводим остаток
        int remainAmount = account.getMoneyAmount();
        if (remainAmount > 0) {
            targetAccount.setMoneyAmount(targetAccount.getMoneyAmount() + remainAmount);
            account.setMoneyAmount(0);

            System.out.println("Account" + accountId + " closed.Remaining balance " + remainAmount +
                    " transferred to account " + targetAccount.getId() + ".");
        } else {
            System.out.println("Account has zero balance, no transfer needed");
        }
        // Закрываем счет, удаляем пользователя ?
        user.getAccounts().remove(account);
        accountService.getAccountStorage().remove(accountId);
        System.out.println("Account" + accountId + " closed.");
    }
}