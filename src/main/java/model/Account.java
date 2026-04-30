package model;

public class Account {
    private final int id;
    private final int userId;
    private int moneyAmount;

    public Account(int idCounterAccount, int userId, int moneyAmount) {
        this.id = idCounterAccount;
        this.userId = userId;
        this.moneyAmount = moneyAmount;

    }

    public int getId() {
        return id;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public int setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
        return moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}