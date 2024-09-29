package v3.template;

import java.util.Map;

public class TemplateConfig {
    private String folderName;
    private String fileName;
    private String extension;
    private String newLine;
    private String indent;
    private Map<String, String> placeholders;
    private String templateFile;

    public TemplateConfig() {
    }

    public TemplateConfig(String extension, String fileName, String folderName, String indent, String newLine, String templateFile) {
        this.extension = extension;
        this.fileName = fileName;
        this.folderName = folderName;
        this.indent = indent;
        this.newLine = newLine;
        this.templateFile = templateFile;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public String getPlaceholder(String key) {
        return placeholders.get(key);
    }

    public void loadPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }
}
