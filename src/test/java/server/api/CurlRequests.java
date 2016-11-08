package server.api;

import com.squareup.okhttp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Artem on 11/8/16.
 */
public class CurlRequests {

    private static final Logger log = LogManager.getLogger(CurlRequests.class);
    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String SERVICE_URL = PROTOCOL + "://" + HOST + ":" + PORT;
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean register(String user, String password) {
        MediaType mediaType = MediaType.parse("raw");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );

        String requestUrl = SERVICE_URL + "/auth/register";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            //log.warn("Something went wrong in register.", e);
            return false;
        }
    }

    public static Long login(String user, String password) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );
        String requestUrl = SERVICE_URL + "/auth/login";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return Long.parseLong(response.body().string());
        } catch (IOException e) {
            log.warn("Something went wrong in login.", e);
            return null;
        }
    }

    public static void logOut(Long token) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType, ""
        );
        String requestUrl = SERVICE_URL + "/auth/logout";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization", "Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
        }
    }

    public static void changeName(Long token, String newName) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("name=%s", newName)
        );
        String requestUrl = SERVICE_URL + "/profile/name";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization", "Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
        }
    }

    public static void changeEmail(Long token, String newEmail) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("email=%s", newEmail)
        );
        String requestUrl = SERVICE_URL + "/profile/email";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization", "Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
        }
    }

    public static void changePassword(Long token, String newPassword) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("password=%s", newPassword)
        );
        String requestUrl = SERVICE_URL + "/profile/password";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization", "Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
        }
    }

    public static String getLeaders(int n){
        String requestUrl = SERVICE_URL + "/data/leaderboard?count=" + n;

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
            return null;
        }
    }

    public static String getUsers(){
        String requestUrl = SERVICE_URL + "/data/users";

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
            return null;
        }
    }
}
