package lesson6;

import okhttp3.*;

import java.io.IOException;

public class RestfulBooking {
    public static void main(String[] args) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("restful-booker.herokuapp.com")
                .addPathSegment("auth")
                .build();

        String authBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        RequestBody requestAuthBody = RequestBody.create(authBody, MediaType.parse("JSON"));

        Request requestAuth = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/json")
                .post(requestAuthBody)
                .build();

        Response responseAuth = okHttpClient.newCall(requestAuth).execute();

        System.out.println(responseAuth.isSuccessful());

        String responseBodyWithToken = responseAuth.body().string();

        System.out.println(responseBodyWithToken);

        String token = responseBodyWithToken.split(":")[1];
        token = token.replaceAll("[\"}]", "");
        System.out.println(token);


        httpUrl = httpUrl.newBuilder()
                .setPathSegment(0, "booking")
                .build();

        String createBookingJson = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        RequestBody requestCreateBookingBody = RequestBody.create(createBookingJson, MediaType.parse("JSON"));

        Request requestCreateBooking = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cookie", String.format("token=%s", token))
                .post(requestCreateBookingBody)
                .build();

        Response createBookingResponse = okHttpClient.newCall(requestCreateBooking).execute();
        String infoAboutBooking = createBookingResponse.body().string();
        System.out.println(infoAboutBooking);

        String id = infoAboutBooking.split(":")[1];
        id = id.replace(",\"booking\"", "");


        httpUrl = httpUrl.newBuilder()
                .addPathSegment(id)
                .build();

        String changeBookingJson = "{\n" +
                "    \"firstname\" : \"Monika\",\n" +
                "    \"lastname\" : \"Clinton\",\n" +
                "    \"totalprice\" : 260,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-05\",\n" +
                "        \"checkout\" : \"2018-12-05\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Sauna\"\n" +
                "}";
        RequestBody requestChangeBookingBody = RequestBody.create(changeBookingJson, MediaType.parse("JSON"));
        Request requestChangeBooking = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cookie", String.format("token=%s", token))
                .patch(requestChangeBookingBody)
                .build();
        Response createChangeBookingResponse = okHttpClient.newCall(requestChangeBooking).execute();
        String changeInBooking = createChangeBookingResponse.toString();
        System.out.println(changeInBooking);

        Request getInfobyID = new Request.Builder()
                .url(httpUrl)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        Response infobyID = okHttpClient.newCall(getInfobyID).execute();
        System.out.println(infobyID.body().string());

    }
}
