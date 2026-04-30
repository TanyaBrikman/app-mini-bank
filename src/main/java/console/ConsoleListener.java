package console;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.AccountService;
import service.UserService;

import java.util.Scanner;

@Component
public class ConsoleListener {

    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;
    private boolean running = true;

    @Autowired
    public ConsoleListener(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        scanner = new Scanner(System.in);
    }

    @PostConstruct
    public void init() {
        System.out.println("""
                MiniBank started. Type EXIT to stop.
                Available commands: USER_CREATE, SHOW_ALL_USERS, ACCOUNT_CREATE,
                ACCOUNT_DEPOSIT, ACCOUNT_WITHDRAW, ACCOUNT_TRANSFER, ACCOUNT_CLOSE, EXIT""");
    }

    public void startProgram() throws Exception {
        while (running) {
            try {
                ConsoleOperationType operationType = ConsoleOperationType.valueOf(scanner.next());
                processOperation(operationType);
            } catch (Exception e) {
                System.out.println("Invalid operation");
            }
        }
    }


    private void processOperation(ConsoleOperationType operationType) {
        switch (operationType) {
            case USER_CREATE:
                createUserCommand();
                break;
            case SHOW_ALL_USERS:
                showAllUsersCommand();
                break;
            case ACCOUNT_CREATE:
                accountCreateCommand();
                break;
            case ACCOUNT_DEPOSIT:
                accountDepositCommand();
                break;
            case ACCOUNT_WITHDRAW:
                accountWithDrawCommand();
                break;
            case ACCOUNT_TRANSFER:
                accountTransferCommand();
                break;
            case ACCOUNT_CLOSE:
                accountCloseCommand();
                break;
            case EXIT:
                running = false;
                scanner.close();
                break;
        }
    }

    private void createUserCommand() {
        try {
            scanner.nextLine();
            System.out.println("Enter user name: ");
            String userName = scanner.nextLine();
            if (userService.isLoginUserExist(userName)) {
                System.out.println("User already exists!");
            } else {
                System.out.println("User created: "
                        + userService.createUser(userName).toString());
            }
        } catch (Exception e) {
            System.out.println("Invalid user name");
        }

    }

    private void showAllUsersCommand() {
        System.out.println("Show all users: "
                + userService.getUsers().toString());
    }

    private void accountCreateCommand() {
        try {
            scanner.nextLine();
            System.out.println("Enter id user: ");
            String userId = scanner.nextLine();
            int id = Integer.parseInt(userId);
            userService.addAccountToUser(id);
        } catch (Exception e) {
            System.out.println("Invalid id user");
        }
    }

    private void accountDepositCommand() {
        try {
            System.out.println("Enter account id: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (accountService.isAccountExist(id)) {
                System.out.println("Enter amount:");
                int amount = Integer.parseInt(scanner.nextLine());
                System.out.println("Deposited "
                        + amount
                        + " to account "
                        + id
                        + ". New balance: "
                        + accountService.AccountDeposit(id, amount));
            }
        } catch (Exception e) {
            System.out.println("Invalid account id");
        }
    }

    private void accountWithDrawCommand() {
        try {
            System.out.println("Enter account id: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (accountService.isAccountExist(id)) {
                System.out.println("Enter amount:");
                int amount = Integer.parseInt(scanner.nextLine());
                System.out.println("Deposited " + amount + " to account "
                        + id + ". New balance: "
                        + accountService.AccountDeposit(id, amount));
            }
        } catch (Exception e) {
            System.out.println("Invalid");
        }
    }

    private void accountTransferCommand() {
        try {
            scanner.nextLine();
            System.out.println("Enter source account id: ");
            int source = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Enter target account id: ");
            int target = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Enter amount: ");
            int amountTransfer = Integer.parseInt(scanner.nextLine().trim());
            userService.transfer(source, target, amountTransfer);
        } catch (Exception e) {
            System.out.println("Invalid data");
        }
    }

    private void accountCloseCommand() {
        try {
            scanner.nextLine();
            System.out.println("Enter account id to close:");
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (accountService.isAccountExist(id)) {
                userService.closeAccount(id);
            }
            System.out.println("Account closed.");
        } catch (Exception e) {
            System.out.println("Invalid account id");
        }
    }
}