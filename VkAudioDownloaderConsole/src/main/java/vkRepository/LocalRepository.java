package vkRepository;

import dataModel.AudioData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocalRepository {

    private static String dataArr = "";
    private static File newFile;

    public static boolean dropDataIntoFile(IVkRepository repo, String file) throws IOException {

        Map<Integer, AudioData> audios = repo.getIntegerAudioDataMap();

        newFile = new File(file);
        newFile.createNewFile();

        JSONCreator(audios);

        return true;
    }

    private static void JSONCreator(Map<Integer, AudioData> audios) throws IOException {

        StringBuilder write = new StringBuilder();
        JSONWriter jsonWriter = new JSONWriter(write);
        FileOutputStream fout = new FileOutputStream(newFile);

        jsonWriter.array();
        for(int i = 0; i < audios.size(); i++)
        {
            try {
                AudioData data = audios.get(i);

                jsonWriter.object();
                jsonWriter.key(data.getAiIdName()).value(data.getAiId());
                jsonWriter.key(data.getReloadIdName()).value(data.getReloadId());
                jsonWriter.key(data.getAiTitleName()).value(data.getAiTitle());
                jsonWriter.key(data.getAiAuthorName()).value(data.getAiAuthor());
                jsonWriter.key(data.getLinkName()).value(data.getLink());

                jsonWriter.endObject();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        jsonWriter.endArray();

        System.out.println("JSON: " + write.toString());
        fout.write(write.toString().getBytes());
        fout.close();
    }
}
