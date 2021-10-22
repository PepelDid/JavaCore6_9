package lesson7;

import lesson7.entity.Weather;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface WeatherModel {
    Weather getWeather(String selectedCity, Period period) throws IOException, SQLException;
    boolean saveWeather(Weather weather, String selectedCity) throws SQLException;
    Weather getSavedToDB(String city);
    List<Weather> getSavedToDBWeatherList(String selectedCity);

}
