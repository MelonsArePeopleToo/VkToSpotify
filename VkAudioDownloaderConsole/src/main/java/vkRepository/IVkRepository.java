package vkRepository;

import dataModel.AudioData;

import java.util.Map;

public interface IVkRepository {
    public Boolean findAllAudios();

    public void getLinks();

    public Map<Integer, AudioData> getIntegerAudioDataMap ();
}
