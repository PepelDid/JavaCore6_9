package lesson7;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


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


    public void getWeather(String selectedCity, Period period) throws IOException {
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
                System.out.println(weatherResponse);

                String []tempWeather = weatherResponse.split("\"");
                String readWeather = new String("В городе " + selectedCity);

                for(int i = 0; i <tempWeather.length; i++){
                if(tempWeather[i].contains("Text")){
                    readWeather = readWeather + " " + tempWeather[i+2].replace(":", " ") + ", ";
                    };
                if(tempWeather[i].contains("Minimum")){
                        readWeather = readWeather + " температура от" + tempWeather[i+3].replace(":", " ");
                    };
                if(tempWeather[i].contains("Maximum")){
                        readWeather = readWeather + " до" + tempWeather[i+3].replace(":", " ") + " градусов. \n";
                    };
                };
                System.out.println(readWeather);

/*   Человекочитаемый вывод погоды я сделала,хотела экономнее сделать,но
       этот код у меня не работает,он выдает мне NullPointerException,т.е. ничего не получает на вход,не понимаю, почему:
                String  readable = objectMapper.readTree(weatherResponse).get(0).at("/Text").asText() + ". Температура воздуха от  " +
                        objectMapper.readTree(weatherResponse).get(0).at("/Minimum").asText() + " до " +
                        objectMapper.readTree(weatherResponse).get(0).at("/Maximum").asText() + " градусов.";
                System.out.println(readable);
*/

                break;
            case FIVE_DAYS:
                //TODO*: реализовать вывод погоды на 5 дней
                //Реализовано
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
                System.out.println(weatherResponseFor5);
                String []tempWeather5 = weatherResponseFor5.split("\"");
                String readWeather5 = ("В городе " + selectedCity);

                for(int i = 0; i <tempWeather5.length; i++){
                    if(tempWeather5[i].contains("Text")){
                        readWeather5 = readWeather5 + " " + tempWeather5[i+2].replace(":", " ") + "\n";
                    };
                    if(tempWeather5[i].contains("Date")){
                        readWeather5 = readWeather5 + " " + tempWeather5[i+2].replace(":", " ");
                    };
                    if(tempWeather5[i].contains("Minimum")){
                        readWeather5 = readWeather5 + " температура от" + tempWeather5[i+3].replace(":", " ");
                    };
                    if(tempWeather5[i].contains("Maximum")){
                        readWeather5 = readWeather5 + " до" + tempWeather5[i+3].replace(":", " ") + " градусов. \n";
                    };
                };
                System.out.println(readWeather5);

                break;
        }
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
