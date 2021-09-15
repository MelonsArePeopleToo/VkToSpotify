package parsers;

import okhttp3.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginParser {
    public static String parseId(String data, Patterns pattern) {
        Pattern p = Pattern.compile(pattern.getPattern());

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }

    public static String parseUrl(String data, Patterns pattern) {
        Pattern p = Pattern.compile(pattern.getPattern());

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }

    public static String parseRemixSid(Response response, Patterns pattern) {
        if (response != null && response.header("Set-Cookie") != null) {

            String data = response.headers("Set-Cookie").toString();

            Pattern p = Pattern.compile(pattern.getPattern());

            Matcher matches = p.matcher(data);

            return matches.find() ? matches.group(1) :
                    parseRemixSid(response.priorResponse(), pattern);
        }
        return "";
    }
}
