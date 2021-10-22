package lesson7;

import lesson7.entity.Weather;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseRepository {
    private String insertWeatherQuery = "insert into weather (city, local_date, temperature) values (?, ?, ?)";
    private String insertWeather5Query = "insert into weather5 (city, local_date, temperature) values (?, ?, ?)";
    private String getWeatherQuery = "select * from weather where city =";
    private String getWeather5Query = "select * from weather5 where city =";
    private String deleteOldQuery = "delete from weather where city =";
    private static final String DB_PATH = "jdbc:sqlite:accuwthr.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean saveWeatherToDataBase(Weather weather, String city) throws SQLException {

        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            PreparedStatement deleteWeather = connection.prepareStatement(deleteOldQuery + "'" + city + "'");
            deleteWeather.execute();
            PreparedStatement insertWeather = connection.prepareStatement(insertWeatherQuery);
            insertWeather.setString(1, weather.getCity());
            insertWeather.setString(2, weather.getLocalDate());
            insertWeather.setDouble(3, weather.getTemperature());
            return insertWeather.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SQLException("Сохранение погоды в базу данных не выполнено!");
    }

    public void saveWeatherToDataBaseList(List<Weather> weatherList) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            PreparedStatement insertWeather = connection.prepareStatement(insertWeather5Query);
            for (Weather weather : weatherList) {
                insertWeather.setString(1, weather.getCity());
                insertWeather.setString(2, weather.getLocalDate());
                insertWeather.setDouble(3, weather.getTemperature());
                insertWeather.addBatch();
            }
            insertWeather.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Weather getSavedToDB(String city) {
        Weather weather = null;

        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            //TODO: реализовать этот метод получения данных из таблицы погоды. Реализован
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getWeatherQuery + "'" + city + "'");
            weather = (new Weather(result.getString("city"),
                    result.getString("local_date"),
                    result.getDouble("temperature")));
            System.out.println(weather);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return weather;
    }

    public List<Weather> getSavedToDBWeatherList(String city) {
        List<Weather> weathers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_PATH)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getWeather5Query + "'" + city + "'");
            while (resultSet.next()) {
                weathers.add(new Weather(resultSet.getString("city"),
                        resultSet.getString("local_date"),
                        resultSet.getDouble("temperature")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (Weather weather : weathers) {
            System.out.println(weather);
        }
        return weathers;
    }
}


