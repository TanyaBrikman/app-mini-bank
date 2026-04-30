package config;

import console.ConsoleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import properties.AccountProperties;
import service.AccountService;
import service.UserService;

@Configuration
@ComponentScan("java")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public UserService userService(AccountService accountService, AccountProperties accountProperties) {
        return new UserService(accountService, accountProperties);
    }

    @Bean
    public AccountService accountService() {
        return new AccountService();
    }

    @Bean
    public AccountProperties accountProperties(@Value("${account.default-amount:500}") int defaultAmount,
                                               @Value("${account.transfer-commission}") double transferCommission) {
        return new AccountProperties(defaultAmount, transferCommission);
    }

    @Bean
    public ConsoleListener consoleListener(UserService userService, AccountService accountService) {
        return new ConsoleListener(userService, accountService);
    }
}