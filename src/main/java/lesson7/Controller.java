package lesson7;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private WeatherModel weatherModel = new AccuweatherModel();
    private Map<Integer, Period> variants = new HashMap<>();

    public Controller() {
        variants.put(1, Period.NOW);
        variants.put(5, Period.FIVE_DAYS);
        variants.put(2, Period.DB);
        variants.put(4, Period.DB5);
    }

    public void getWeather(String userInput, String selectedCity) throws IOException, SQLException {
        Integer userIntegerInput = Integer.parseInt(userInput);

        switch (variants.get(userIntegerInput)) {
            case NOW:
                weatherModel.getWeather(selectedCity, Period.NOW);
                break;
            case FIVE_DAYS:
                weatherModel.getWeather(selectedCity, Period.FIVE_DAYS);
            case DB:
                weatherModel.getSavedToDB(selectedCity);
            case DB5:
                weatherModel.getSavedToDBWeatherList(selectedCity);

        }
    }
}
