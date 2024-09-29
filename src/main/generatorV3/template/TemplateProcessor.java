package main.generatorV3.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.generatorV3.generator.DBConnector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateProcessor {
    private DBConnector dBConnector;
    private Map<String, String> fields;
    private TemplateConfig templateConfig;
    private Template template;

    public TemplateProcessor() {}
    public TemplateProcessor(DBConnector dBConnector) {
        this.dBConnector = dBConnector;
    }
    public TemplateProcessor(TemplateConfig templateConfig, Template template, String schema, String tableName) {
        this.templateConfig = templateConfig;
        this.template = template;
        this.dBConnector = DBConnector.getInstance();
        this.fields = dBConnector.readFields(schema, tableName);
    }
    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
    public String parsePackageName(String packageName) {
        return template.getTemplateDefinition().replace("${packageName}", packageName);
    }

    private static String wrapPlaceholderName(String placeholderName) {
        return "${" + placeholderName.toLowerCase() + "}";
    }

    public static String processDefaultPlaceholderValue(String placeholderName, String placeholderValue) {
        var placeholder = wrapPlaceholderName(placeholderName).toLowerCase();

        if(placeholderValue != null && !placeholderValue.isEmpty()) {
            if(placeholder.contains("capitalized")) {
                return placeholder.replace(placeholder, capitalize(placeholderValue));
            }
            else if(placeholder.contains("lower")) {
                return placeholder.replace(placeholder, lowercase(placeholderValue));
            }
            else {
                return placeholder.replace(placeholder, placeholderValue);
            }
        }

        return null;
    }

//    public String parseFields(String template) {
//        StringBuilder fieldsBuilder = new StringBuilder();
//        for (Map.Entry<String, String> field : fields.entrySet()) {
//            String fieldLine = template.replace("{fieldType}", field.getValue())
//                    .replace("{fieldName}", field.getKey());
//            fieldsBuilder.append(fieldLine).append("\n");
//        }
//        return fieldsBuilder.toString();
//    }
//
//    public String parseTableName(String template, String tableName) {
//        return template.replace("{tableName}", tableName);
//    }

    private String forEachField(String template, Map<String, String> fields) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> field : fields.entrySet()) {
            // replace plaseholders ${indent} and ${newLine} ${fieldType} {fieldName} {fieldNameCapitalized} ...

            // replace in this short template wrapped field and its value
            result.append(
                template
                    .replace("${fieldType}", processDefaultPlaceholderValue("fieldType", field.getValue()))
                    .replace("${fieldTypeCapitalized}", processDefaultPlaceholderValue("fieldTypeCapitalized", field.getValue()))
                    .replace("${fieldTypeLower}", processDefaultPlaceholderValue("fieldTypeLower", field.getValue()))
                    .replace("${fieldName}", processDefaultPlaceholderValue("fieldName", field.getKey()))
                    .replace("${fieldNameCapitalized}", processDefaultPlaceholderValue("fieldNameCapitalized", field.getKey()))
                    .replace("${fieldNameLower}", processDefaultPlaceholderValue("fieldNameLower", field.getKey()))
                    .replace("${indent}", processDefaultPlaceholderValue("indent", templateConfig.getIndent()))
                    .replace("${newLine}", processDefaultPlaceholderValue("newLine", templateConfig.getNewLine()))

            );
        }

        return result.toString();
    }

    private String forEachFieldConcat(String template, Map<String, String> fields) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> field : fields.entrySet()) {
            // replace plaseholders ${indent} and ${newLine} ${fieldType} {fieldName} {fieldNameCapitalized} ...
            // replace in this short template wrapped field and its value
            result.append(
                    template
                            .replace("${fieldType}", processDefaultPlaceholderValue("fieldType", field.getValue()))
                            .replace("${fieldTypeCapitalized}", processDefaultPlaceholderValue("fieldTypeCapitalized", field.getValue()))
                            .replace("${fieldTypeLower}", processDefaultPlaceholderValue("fieldTypeLower", field.getValue()))
                            .replace("${fieldName}", processDefaultPlaceholderValue("fieldName", field.getKey()))
                            .replace("${fieldNameCapitalized}", processDefaultPlaceholderValue("fieldNameCapitalized", field.getKey()))
                            .replace("${fieldNameLower}", processDefaultPlaceholderValue("fieldNameLower", field.getKey()))
                            .replace("${indent}", processDefaultPlaceholderValue("indent", templateConfig.getIndent()))
                            .replace("${newLine}", processDefaultPlaceholderValue("newLine", templateConfig.getNewLine()))

            ).append(", ");
        }

        return result.deleteCharAt(result.lastIndexOf(", ")).toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // take one forEachCommand and return text which will replace expression
    private String parseForEachLoops(
            String template, // foreach construct
            Map<String, String> fields
    ) {
        String result = template;

        // find all basic for each construct and parse them
        // Define the regular expression to match the pattern `${forEachField(%text%)}`
        String regexForEach = "forEachField\\(.*'\\)";

        // Compile the pattern
        Pattern patternForEach = Pattern.compile(regexForEach);
        Matcher matcherForEach = patternForEach.matcher(template);

        // Loop through and find all matches
        while (matcherForEach.find()) {
            // Group 0 contains the extracted text inside the placeholder
            String foundString = matcherForEach.group(0);

            int start = foundString.indexOf("forEachField('") + "forEachField('".length();
            int end = foundString.indexOf("')", start);
            String innerForeachTemplate = foundString.substring(start, end);

            result = result.replace(foundString, forEachField(innerForeachTemplate, fields));
        }

        // find all basic for each concat construct and parse them
        // Define the regular expression to match the pattern `${forEachFieldConcat(%text%)}`
        String regexForEachConcat = "forEachFieldConcat\\(.*'\\)";

        // Compile the pattern
        Pattern patternForEachConcat = Pattern.compile(regexForEachConcat);
        Matcher matcherForEachConcat = patternForEachConcat.matcher(template);

        // Loop through and find all matches
        while (matcherForEachConcat.find()) {
            // Group 0 contains the extracted text inside the placeholder
            String foundString = matcherForEachConcat.group(0);

            int start = foundString.indexOf("forEachFieldConcat('") + "forEachFieldConcat('".length();
            int end = foundString.indexOf("')", start);
            String innerForeachTemplate = foundString.substring(start, end);

            result = result.replace(foundString, forEachFieldConcat(innerForeachTemplate, fields));
        }

        return result;
    }

    private static String lowercase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    // process all config files
    public void processTemplates(
            List<File> jsonTemplateConfigs,
            String path,
            String packageName,
            String schema,
            List<String> tableNames
    ) {
        // make path
        String outputPath = path+"/"+packageName ;

        // make project directory
        new File(outputPath).mkdirs();

        ObjectMapper objectMapper = new ObjectMapper();

        // for each table
        // go through each template file pair
        // and generate files
        tableNames.forEach(tableName -> {
            // for each template config file
            jsonTemplateConfigs.forEach(jsonTemplateConfig -> {

                try {
                    // load template config file
                    TemplateConfig config = objectMapper.readValue(jsonTemplateConfig, TemplateConfig.class);
                    config.loadDefaultPlaceholders(config.getPlaceholders());
                    config.setTableName(tableName);

                    this.templateConfig = config;

                    // load template file
                    StringBuilder templateText = new StringBuilder();
                    try(BufferedReader br = new BufferedReader(new FileReader(config.getTemplateFilePath()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            templateText.append(line).append("\n");
                        }
                    }

                    // process template file
                    String processedTemplate = processTemplate(templateText.toString(), config, schema, tableName);

                    // create file
                    System.out.println("printing: "+config.getFileName());

                    createFile(outputPath+"/"+config.getFolderName(), processedTemplate, config.getFileName(), config.getExtension());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    public void createFile(String filePath, String fileData, String className, String extension) {
        // create folder
        new File(filePath).mkdirs();
        // Create the file path
        String outputPath = filePath + "/" + className + "." + extension;

        // Write the content to the Java class file
        try ( FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(fileData);
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing class to file: " + e.getMessage());
        }
    }

// example
//    {
//        // "fileBegining": "public class {className} {",
//        //    "fileEnd": "}",
//        //    "packageName": "package {packageName};",
//        "classDefinition": {
//        "
//		package model;
//
//        public class {TableName} {
//            forEachField('private {fieldType} {fieldName};')
//
//            public {TableName}() {}
//
//            public {TableName}(forEachFieldConcat({fieldType} {fieldName})) {
//                forEachField('this.{fieldName} = {fieldName};')
//            }
//
//            forEachField('public void set{fieldNameCapitalized}({fieldType} {fieldName}) {
//            this.{fieldName} = {fieldName};
//        }')
//
//        forEachField('public {fieldType} get{fieldNameCapitalized}() {
//        return {fieldName}
//    }')
//    }
//		"
//}
//}
    public String processTemplate(
            String template,
            TemplateConfig templateConfig,
            String schema,
            String tableName
    ) {
        // load fields from DB for given table
        this.fields = dBConnector.readFields(schema, tableName, templateConfig.getExtension());

        String result = template;

        try {
            // process basic for each construct and for each concat constructs
            result = parseForEachLoops(result, fields);

            // process the rest of template default placeholders
            result = result
                    .replaceAll("\\$\\{packageName}", processDefaultPlaceholderValue("packageName", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{packageNameCapitalized}", processDefaultPlaceholderValue("packageNameCapitalized", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{packageNameLower}", processDefaultPlaceholderValue("packageNameLower", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{folderName}", processDefaultPlaceholderValue("folderName", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{folderNameCapitalized}", processDefaultPlaceholderValue("folderNameCapitalized", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{folderNameLower}", processDefaultPlaceholderValue("folderNameLower", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{fileName}", processDefaultPlaceholderValue("fileName", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{fileNameCapitalized}", processDefaultPlaceholderValue("fileNameCapitalized", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{fileNameLower}", processDefaultPlaceholderValue("fileNameLower", templateConfig.getFolderName()))
                    .replaceAll("\\$\\{tableName}", processDefaultPlaceholderValue("tableName", tableName))
                    .replaceAll("\\$\\{tableNameCapitalized}", processDefaultPlaceholderValue("tableNameCapitalized", tableName))
                    .replaceAll("\\$\\{tableNameLower}", processDefaultPlaceholderValue("tableNameLower", tableName))
                    .replaceAll("\\$\\{indent}", processDefaultPlaceholderValue("indent", templateConfig.getIndent()))
                    .replaceAll("\\$\\{newLine}", processDefaultPlaceholderValue("newLine", templateConfig.getNewLine()))
            ;


            // process the rest of placeholders

            // return processed template so it can be written to file
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    public List<File> chooseJsonFiles() {
        // Create a JFileChooser instance
        JFileChooser chooser = new JFileChooser();

        // Set the file filter to only allow JSON files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
        chooser.setFileFilter(filter);

        // Allow multiple file selection
        chooser.setMultiSelectionEnabled(true);

        // Show the open dialog and capture the user's action
        int returnVal = chooser.showOpenDialog(null);

        // If the user approved the selection, return the selected files as a list
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            return new ArrayList<>(List.of(files));
        }

        // If the user canceled the operation or no file was selected, return an empty list
        return new ArrayList<>();
    }

}