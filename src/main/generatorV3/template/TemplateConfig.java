package main.generatorV3.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

import static main.generatorV3.template.TemplateProcessor.capitalize;
import static main.generatorV3.template.TemplateProcessor.processDefaultPlaceholderValue;

public class TemplateConfig {
    @JsonIgnore
    private String folderName;
    @JsonIgnore
    private String fileName;
    @JsonIgnore
    private String extension;
    @JsonIgnore
    private String newLine;
    @JsonIgnore
    private String indent;
    private Map<String, String> placeholders;
    private String templateFilePath;
    private String tableName;

    public TemplateConfig() {}
    public TemplateConfig(String extension, String fileName, String folderName, String indent, String newLine, String templateFilePath) {
        this.extension = extension;
        this.fileName = fileName;
        this.folderName = folderName;
        this.indent = indent;
        this.newLine = newLine;
        this.templateFilePath = templateFilePath;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName
                .replaceAll("\\$\\{folderName}", processDefaultPlaceholderValue("folderName", getFolderName()))
                .replaceAll("\\$\\{folderNameCapitalized}", processDefaultPlaceholderValue("folderNameCapitalized", getFolderName()))
                .replaceAll("\\$\\{folderNameLower}", processDefaultPlaceholderValue("folderNameLower", getFolderName()))
                .replaceAll("\\$\\{fileName}", processDefaultPlaceholderValue("fileName", getFolderName()))
                .replaceAll("\\$\\{fileNameCapitalized}", processDefaultPlaceholderValue("fileNameCapitalized", getFolderName()))
                .replaceAll("\\$\\{fileNameLower}", processDefaultPlaceholderValue("fileNameLower", getFolderName()))
                .replaceAll("\\$\\{tableName}", processDefaultPlaceholderValue("tableName", tableName))
                .replaceAll("\\$\\{tableNameCapitalized}", processDefaultPlaceholderValue("tableNameCapitalized", tableName))
                .replaceAll("\\$\\{tableNameLower}", processDefaultPlaceholderValue("tableNameLower", tableName))
                ;
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
    public String getTemplateFilePath() {
        return templateFilePath;
    }
    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }
    public Map<String, String> getPlaceholders() { return placeholders; }
    public String getPlaceholder(String key) {
        return placeholders.get(key);
    }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public void loadDefaultPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;

        if(placeholders.get("folderName") != null) {
            folderName = processDefaultPlaceholderValue("folderName", placeholders.get("folderName"));
        }
        if(placeholders.get("fileName") != null) {
            fileName = processDefaultPlaceholderValue("fileName", placeholders.get("fileName"));
        }
        if(placeholders.get("extension") != null) {
            extension = placeholders.get("extension");
        }
        if(placeholders.get("newLine") != null) {
            newLine = placeholders.get("newLine");
        }
        if(placeholders.get("indent") != null) {
            indent = placeholders.get("indent");
        }
        if(placeholders.get("templateFile") != null) {
            templateFilePath = placeholders.get("templateFile");
        }

        processPlaceholders();
    }

    private void processPlaceholders() {
        if(placeholders != null) {

            tableName = placeholders.get("tableName");
        }
    }


}
