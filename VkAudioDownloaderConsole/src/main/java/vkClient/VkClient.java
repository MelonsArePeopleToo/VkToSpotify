package vkClient;

import okhttp3.*;
import parsers.LoginParser;

import java.io.IOException;
import java.util.*;


public class VkClient implements IVkClient {
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64)" +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36";


    private static final Map<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient client;
    private String id;

    public VkClient() {
        this.client = new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", USER_AGENT)
                            .build();
                    return chain.proceed(request);
                })
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl, list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> result = new ArrayList<>();

                        cookieStore.values().forEach(cookies -> cookies.forEach(cookie -> {
                            if (cookie.matches(httpUrl))
                                result.add(cookie);
                        }));

                        result.add(new Cookie.Builder()
                                .name("remixdevice")
                                .domain("vk.com")
                                .value("1920/1080/1/!!-!!!!")
                                .build()
                        );

                        return result;
                    }
                })
                .build();
    }

    @Override
    public boolean login(String login, String password) {
        Request request = new Request.Builder()
                .url("https://m.vk.com/")
                .get()
                .build();

        Response response;
        String loginUrl = "";

        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful())
                loginUrl = LoginParser.parseUrl(response.body().string());
        }
        catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }

        if(loginUrl.isEmpty()) {
            System.out.println("loginUrl is empty!");
            return false;
        }

        FormBody loginBody = new FormBody.Builder()
                .add("email", login)
                .add("pass", password)
                .build();

        try {
            request = new Request.Builder()
                    .url(loginUrl)
                    .post(loginBody)
                    .build();
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        String remixSid = "";
        String responseBody = "";

        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                responseBody = response.body().string();
                remixSid = LoginParser.parseRemixSid(response);
            }
            else
                return false;
        }
        catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }

        if (!responseBody.isEmpty() || !remixSid.isEmpty()) {
            this.id = LoginParser.parseId(responseBody);
            return true;
        }

        return false;
    }

    @Override
    public String getAudios() {
        Request request = new Request.Builder()
                .url("https://m.vk.com/audios" + this.id + "?section=all")
                .get()
                .build();

        Response response;
        String html = "";

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                html = Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return html;
    }

    @Override
    public String loadAudioBlocks(String startFrom) {

        FormBody body = new FormBody.Builder()
                .add("_ajax", "1")
                .build();

        Request request = new Request.Builder()
                .url("https://m.vk.com/audios33066660?section=my&start_from=" + startFrom)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-Requested-With", "XMLHttpRequest")
                .post(body)
                .build();

        Response response;
        String html = "";

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                html = Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return html;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String reloadAudio(String audioIds) {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder()
                .add("act", "reload_audio")
                .add("ids", String.join(",", audioIds));

        String json = "";

        Request request;
        try {
            request = new Request.Builder()
                    .url("https://m.vk.com/audio")
                    .post(requestBodyBuilder.build())
                    .build();
        } catch (IllegalArgumentException e) {
            return json;
        }

        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
            }
        } catch (IOException | NullPointerException e) {
            // nothing
        }
        return json;
    }
}
//Response{protocol=h2, code=302, message=, url=https://login.vk.com/?act=login}