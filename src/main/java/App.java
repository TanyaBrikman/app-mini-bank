import config.AppConfig;
import console.ConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.UserService;

public class App {
    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ConsoleListener consoleListener = context.getBean(ConsoleListener.class);
        consoleListener.startProgram();

    }
}