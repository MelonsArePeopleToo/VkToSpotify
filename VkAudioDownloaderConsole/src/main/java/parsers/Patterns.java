package parsers;

public enum Patterns {

    FROM_HTML("start_from=([^\"]+)"),
    FROM_JSON("next_from\":\"([^\"]+)"),
    ID("\"id\":([^,]+)"),
    URL("action=\"([^\"]+)"),
    SID("remixsid=([^;]+)");


    private String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    protected String getPattern() {
        return this.pattern;
    }
}

