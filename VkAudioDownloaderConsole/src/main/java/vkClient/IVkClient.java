package vkClient;


public interface IVkClient {

    boolean login(String login, String password);

    String getAudios();

    String loadAudioBlocks(String startFrom);

    String reloadAudio(String audioId);

    String getId();
}
