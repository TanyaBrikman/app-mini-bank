import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.UserService;

public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

    }
}