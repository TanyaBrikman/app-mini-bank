import config.AppConfig;
import console.ConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ConsoleListener consoleListener = context.getBean(ConsoleListener.class);
        consoleListener.startProgram();
    }
}