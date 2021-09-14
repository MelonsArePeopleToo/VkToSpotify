package parsers;

public enum Patterns {

    FROM_HTML("start_from=([^\"]+)"),
    FROM_JSON("next_from\":\"([^\"]+)");

    private String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    protected String getPattern() {
        return this.pattern;
    }
}

