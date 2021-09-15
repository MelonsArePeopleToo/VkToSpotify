package dataModel;

public class AudioData {

    private String aiId = "";             ///
    private String reloadId = "";        ///
    private String aiTitle = "";        ///
    private String aiAuthor = "";      ///
    private String link = "";               /// заполняется после перезагрузки
    private boolean ready = false;

    public String getAiIdName() { return "aiId"; }
    public String getAiId() { return this.aiId; }
    public void setAiId(String str) { this.aiId = str; }

    public String getReloadIdName() { return "reloadId"; }
    public String getReloadId() { return this.reloadId; }
    public void setReloadId(String str) { this.reloadId = str; }

    public String getAiTitleName() { return "aiTitle"; }
    public String getAiTitle() { return this.aiTitle; }
    public void  setAiTitle(String str) { this.aiTitle = str; }

    public String getAiAuthorName() { return "aiAuthor"; }
    public String getAiAuthor() { return this.aiAuthor; }
    public void setAiAuthor(String str) { this.aiAuthor = str; }

    public String getLinkName() { return "link"; }
    public String getLink() { return this.link; }
    public void setLink(String str) { this.link = str; }

    public Boolean isReady() { return this.ready; }
    public void setReady() { this.ready = true; }
}
