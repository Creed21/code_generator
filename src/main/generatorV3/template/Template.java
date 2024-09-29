package main.generatorV3.template;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class Template {
    private List<Placeholder> placeholders;
    private String templateDefinition;


    public Template() {}

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public String getTemplateDefinition() {
        return templateDefinition;
    }

    public void setTemplateDefinition(String templateDefinition) {
        this.templateDefinition = templateDefinition;
    }

    public String get(String key) {
        return placeholders.stream()
                .filter(placeholder -> placeholder.getPlaceholderValue(key).equals(key))
                .toString();

    }
}
