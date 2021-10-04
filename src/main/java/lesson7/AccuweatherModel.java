package lesson7;

import com.fasterxml.jackson.databind.ObjectMapper;

import lesson7.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccuweatherModel implements WeatherModel {
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day/349727
    private static final String PROTOKOL = "https";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECASTS = "forecasts";
    private static final String VERSION = "v1";
    private static final String DAILY = "daily";
    private static final String ONE_DAY = "1day";
    private static final String FIVE_DAYS = "5day";
    private static final String API_KEY = "pXJd8MokcZCdrd2MsoGl2DBZAyCa0zvv";
    private static final String API_KEY_QUERY_PARAM = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";
    private static final String LANGUAGE_QUERY_PARAM = "language";
    private static final String LANGUAGE = "ru";
    private static final String METRIC_QUERY_PARAM = "metric";
    private static final String METRIC = "true";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private DataBaseRepository dataBaseRepository = new DataBaseRepository();



//В базу пишет и из базы достает прогноз на 1 день
    public Weather getWeather(String selectedCity, Period period) throws IOException, SQLException {
        switch (period) {
            case NOW:
              HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOKOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .addQueryParameter(LANGUAGE_QUERY_PARAM, LANGUAGE)
                        .addQueryParameter(METRIC_QUERY_PARAM, METRIC)
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response oneDayForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = oneDayForecastResponse.body().string();
                String  date = objectMapper.readTree(weatherResponse).at("/DailyForecasts").get(0).at("/Date").asText();
                Integer temperature = objectMapper.readTree(weatherResponse).at("/DailyForecasts").get(0).at("/Temperature").at("/Minimum").at("/Value").asInt();
                Weather oneDay = new Weather (selectedCity, date, temperature);
                System.out.println(oneDay);
                saveWeather(oneDay);
                break;

            case FIVE_DAYS:
                //TODO*: реализовать вывод погоды на 5 дней. Оптимизирован вывод, совладала с маппингом. Пишет в базу.
                HttpUrl httpUrl5 = new HttpUrl.Builder()
                        .scheme(PROTOKOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .addQueryParameter(LANGUAGE_QUERY_PARAM, LANGUAGE)
                        .addQueryParameter(METRIC_QUERY_PARAM, METRIC)
                        .build();

                Request request5 = new Request.Builder()
                        .url(httpUrl5)
                        .build();

                Response fiveDayForecastResponse = okHttpClient.newCall(request5).execute();
                String weatherResponseFor5 = fiveDayForecastResponse.body().string();
                List<Weather> weathers = new ArrayList<>();
                for(int i = 0; i <5; i++){
                    String  eachDate = objectMapper.readTree(weatherResponseFor5).at("/DailyForecasts").get(i).at("/Date").asText();
                    Integer eachTemperature = objectMapper.readTree(weatherResponseFor5).at("/DailyForecasts").get(i).at("/Temperature").at("/Minimum").at("/Value").asInt();
                    System.out.println("В городе " + selectedCity + " на дату " + eachDate + " ожидается минимальная температура " + eachTemperature + " градусов Цельсия");
                    Weather weatherToList = new Weather(selectedCity, eachDate, eachTemperature);
                    weathers.add(weatherToList);
                };
                saveWeatherList(weathers);
                break;
        }
        return null;
    }

    @Override
    public boolean saveWeather(Weather weather) throws SQLException {
        return dataBaseRepository.saveWeatherToDataBase(weather);
    }

    @Override
    public Weather getSavedToDB(String inputCity) {
        return dataBaseRepository.getSavedToDB(inputCity);
    }


    public void saveWeatherList(List<Weather> weatherList) throws SQLException {
        dataBaseRepository.saveWeatherToDataBaseList(weatherList);
    }

    @Override
    public List<Weather> getSavedToDBWeatherList(String inputCity) {
        return dataBaseRepository.getSavedToDBWeatherList(inputCity);
    }



    private String detectCityKey(String selectCity) throws IOException {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOKOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectCity)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseString = response.body().string();
        String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();
        return cityKey;
    }
}
