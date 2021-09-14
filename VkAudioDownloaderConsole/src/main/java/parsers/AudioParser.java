package parsers;

import dataModel.AudioData;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioParser {

    public static void parseAudioData(String audioDataJson, AudioData data) throws JSONException {
        JSONArray audioData = new JSONArray(audioDataJson);
        parse(audioData, data);
    }

    public static void parseAudioData(JSONArray audioDataJson, AudioData data) throws JSONException {
        parse(audioDataJson, data);
    }

    private static void parse(JSONArray audioDataJson, AudioData data) throws JSONException {
        String id = audioDataJson.get(1) + "_" + audioDataJson.get(0);
        data.setAiId(id);
        data.setAiTitle(audioDataJson.get(3).toString());
        data.setAiAuthor(audioDataJson.get(4).toString());

        String[] tokens = ((String) audioDataJson.get(13)).split("/");
        String actionHash = tokens.length > 2 ? tokens[2] : "";
        String urlHash = tokens.length > 5 ? tokens[5] : "";

        data.setReloadId(id + "_" + actionHash + "_" + urlHash);
    }


    public static String parseNextBlockId(String data, Patterns pattern) {
        Pattern p = Pattern.compile(pattern.getPattern());

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }
}

