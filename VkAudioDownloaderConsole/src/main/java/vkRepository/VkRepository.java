package vkRepository;


import dataModel.AudioData;
import decoder.VkMusicLinkDecoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import parsers.AudioParser;
import parsers.Patterns;
import vkClient.IVkClient;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;




public class VkRepository {
    private static final long DDOS_DELAY = 1000;

    private final IVkClient client;
    private final VkMusicLinkDecoder linkDecoder = new VkMusicLinkDecoder();
    private final Map<Integer, AudioData> audios = new HashMap<>();
    private final BlockingQueue<String> audioBlocks = new LinkedBlockingQueue<>();

    public VkRepository(IVkClient client) {
        this.client = client;
    }

    public Boolean findAllAudios() {

        String html = client.getAudios(); // загрузка нач страницы
        getStartPageAudios(html); // парсинг нач страницы + добавление в мапу

        getAudioBlocks(html);// загрузка блоков + парсинг id следю блока + добавление в очередь, для дальнейшей работы

        getAudios();// разгребание очереди + парсинг + извелчение полезной информации

        getLinks(); // загрузка ссылок на скачивание

        return true;
    }

    private void getLinks() {

        for (AudioData data : audios.values()) {
            String reloadAudioResponse = client.reloadAudio(data.getReloadId());

            try {
                JSONObject reloadAudioData = new JSONObject(reloadAudioResponse);
                JSONArray audioUrls = reloadAudioData.getJSONArray("data").
                        getJSONArray(0);

                for (int i = 0; i != audioUrls.length(); i++) {
                    JSONArray audioUrl = audioUrls.optJSONArray(i);

                    String author = audioUrl.optString(4);
                    String title = audioUrl.optString(3);
                    String link = audioUrl.optString(2);

                    // decode api_unavailable
                    link = linkDecoder.decode(link, Integer.parseInt(client.getId()));
                    // decode m3u8
                    link = linkDecoder.decode(link, Integer.parseInt(client.getId()));

                    data.setLink(link);
                    data.setReady();
                }
            } catch (JSONException e) {
                System.out.println("INVALID JSON: " + e.getMessage());
            }

            try {
                TimeUnit.MILLISECONDS.sleep(DDOS_DELAY);
            } catch (InterruptedException e) {
                System.out.println("DDOS DELAY: " + e.getMessage());
            }
            System.out.println(data.getLink());
        }
    }

    // придумать нормальную форму этого говна
    private void getAudios() {

        while (!audioBlocks.isEmpty()) {

            System.out.println(audioBlocks.size());
            System.out.println(audios.size());

            String audioBlock = audioBlocks.poll();

            try {
                JSONObject json = new JSONObject(audioBlock);
                String data = json.optJSONArray("data").get(1).toString();

                json = new JSONObject(data);
                data = json.optJSONArray("playlists").get(0).toString();

                json = new JSONObject(data);
                JSONArray array = json.optJSONArray("list");

                for (int i = 0; i < array.length(); i++) {
                    AudioData audioFormat = new AudioData();
                    JSONArray audioData = array.optJSONArray(i);
                    AudioParser.parseAudioData(audioData, audioFormat);

                    audios.put(audios.size() + i, audioFormat);
                }
            }
            catch (JSONException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getAudioBlocks(String html) {
        String nextBlockId = AudioParser.parseNextBlockId(html, Patterns.FROM_HTML);

        while(!nextBlockId.isEmpty()) {

            System.out.println(nextBlockId);

            String audioBlock = client.loadAudioBlocks(nextBlockId);

            nextBlockId = AudioParser.parseNextBlockId(audioBlock, Patterns.FROM_JSON);

            audioBlocks.offer(audioBlock);
            System.out.println(audioBlocks.size());
        }
    }

    private void getStartPageAudios(String html) {
        Document doc = Jsoup.parse(html);

        Elements tracks = doc.select(".AudioPlaylistRoot .audio_item");

        for (int i = 0; i < tracks.size(); i++) {
            Element audio = tracks.get(i);
            AudioData data = new AudioData();

            String audioData = audio.dataset().getOrDefault(
                    "audio", "[]");

            AudioParser.parseAudioData(audioData, data);
            audios.put(i, data);
        }
    }

    public Map<Integer, AudioData> getIntegerAudioDataMap () { return this.audios; }
}
