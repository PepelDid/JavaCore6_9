package lesson7;

import java.io.IOException;
import java.util.Scanner;

public class UserInterfaceView {
    private Controller controller = new Controller();

    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String city;
            while (true) {
                System.out.println("Введите название города латиницей:");
                city = scanner.nextLine();
                if (city.matches("Sochi")) break;
                //тут у меня проблемы с регулярным выражением, я пишу "[a-zA-Z]", но это не работает,поэтому я оставила конкретный город
            }
            ;
            String command;
            while (true) {
                System.out.println("Введите желаемый период в формате: 1 для получения погоды на сегодня;" +
                        "5 для прогноза на 5 дней; для выхода введите 0:");
                command = scanner.nextLine();
                if (command.equals("1") || command.equals("5")) break;
            }
            ;
            if (command.equals("0")) break;

            //TODO* Сделать метод валидации пользовательского ввода
            // валидирует


            try {
                controller.getWeather(command, city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


