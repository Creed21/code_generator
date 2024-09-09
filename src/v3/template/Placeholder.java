package v3.template;

import java.util.Map;

public class Placeholder {
//    private String fileName;
//    private String extension;

    // Map<placeholder, value>
    private Map<String, String> placeholders;

    public Placeholder() {}
    public Placeholder(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

}
