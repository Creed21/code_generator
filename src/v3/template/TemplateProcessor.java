package v3.template;

import com.google.gson.Gson;
import v3.generator.Generator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateProcessor {
    private Generator generator;
    private Map<String, String> fields;
    private String className;
    private StringBuilder classDefinition = new StringBuilder();

    public TemplateProcessor() {
        this.generator = new Generator();
    }

    public TemplateProcessor(Map<String, String> fields) {
        this.fields = generator.getFields();
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public String parseFileBeginning(String template, String className) {
        return template.replace("{className}", className);
    }

    public String parseFileEnd(String template) {
        return template; // Usually, file end doesn't require replacements.
    }

    public String parsePackageName(String template, String packageName) {
        return template.replace("{packageName}", packageName);
    }

    public String parseFields(String template) {
        StringBuilder fieldsBuilder = new StringBuilder();
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String fieldLine = template.replace("{fieldType}", field.getValue())
                    .replace("{fieldName}", field.getKey());
            fieldsBuilder.append(fieldLine).append("\n");
        }
        return fieldsBuilder.toString();
    }

    public String parseTableName(String template, String tableName) {
        return template.replace("{tableName}", tableName);
    }

    private String forEachField(String template, Map<String, String> fields) {
        StringBuilder result = new StringBuilder(template);
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String processedString = template.replace("{fieldType}", field.getValue())
                    .replace("{fieldName}", field.getKey())
                    .replace("{fieldNameCapitalized}", capitalize(field.getKey()));
        }
        return result.toString();
    }

    private String forEachFieldConcat(String template, Map<String, String> fields) {
        StringBuilder result = new StringBuilder(template);
        fields.forEach((fieldName, fieldType) -> {
            result.append(
                    template.replace("{fieldType}", fieldType)
                    .replace("{fieldName}", fieldName)
                    .replace("{fieldNameCapitalized}", capitalize(fieldName))
            );
        });



        return template;
//        entrySet().stream()
//                .map(field -> template.replace("{fieldType}", field.getValue())
//                        .replace("{fieldName}", field.getKey())
//                        .replace("{fieldNameCapitalized}", capitalize(field.getKey())))
//                .collect(Collectors.joining(", "));
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.toUpperCase();
    }

    private String parseForEachCommand(
            String template,
            Map<String, String> fields
    ) {
        classDefinition.append(template);

        if (template.contains("forEachField")) {
            int start = template.indexOf("forEachField('") + "forEachField('".length();
            int end = template.indexOf("')", start);
            String innerTemplate = template.substring(start, end);

            return forEachField(innerTemplate, fields);
        } else if (template.contains("forEachFieldConcat")) {
            int start = template.indexOf("forEachFieldConcat('") + "forEachFieldConcat('".length();
            int end = template.indexOf("')", start);
            String innerTemplate = template.substring(start, end);

            return forEachFieldConcat(innerTemplate, fields);
        }
        return template;
    }

    private String lowercase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private void processPlaceholders(Map<String, String> placeholders, String className) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()
                    .replace("{className}", className)
                    .replace("{classNameLowercase}", lowercase(className))
                    .replace("{classNameCapitalized}", capitalize(className));

            placeholders.put(key, value);
        }
    }

    public void processTemplates(
            List<File> jsonTemplates,
            String path,
            String packageName,
            String schema,
            List<String> tableNames
    ) {
        String outputPath = path+"/"+packageName ;
        // make project directory
        new File(outputPath).mkdirs();
        tableNames.forEach(tableName -> {
            List<String> tableTemplates = processTemplates(jsonTemplates, outputPath, packageName, schema, tableName);
            tableTemplates.forEach(tableTemplate -> {
                createFile(outputPath, tableName, tableTemplate);
            });
        });
        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    public void createFile(String filePath, String fileData) {
//        String filePath = outputPath + "/" + className + "." + extension;
        // Write the content to the Java class file
        try ( FileWriter fileWriter = new FileWriter(filePath)) {
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
    public Map<String, String> processTemplates(
            List<File> jsonTemplates,
            String path,
            String packageName,
            String schema,
            String className
    ) {
        List<String> tableTemplates = new ArrayList<>();
        Gson gson = new Gson();

        for(File jsonTemplate: jsonTemplates) {
            try {
                // make file reader
                BufferedReader reader = new BufferedReader(new FileReader(jsonTemplate));
                String templateData = reader.lines().collect(Collectors.joining());
                Template template = gson.fromJson(templateData, Template.class);

                // make filePath
                // get fileName from jsonTemplate from field placeholders
                template.getPlaceholders("placeholders");

                String filePath = path + "/" + packageName + "/" + className + ".java";

                // fetch fields from database
                Map<String, String> fields = generator.getFields();

                // process for each command

                // process remaining placeholders

                // tetClassDefinition
                String templateForEach = parseForEachCommand((String) template.get("classDefinition"), fields);


                // Now process the main class definition template
                String classTemplate = (String) templateMap.get("classDefinition");

                // Replace all placeholders in the class template
//                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
//                    classTemplate = classTemplate.replace("{" + entry.getKey() + "}", entry.getValue());
//                }



                tableTemplates.add(classTemplate);

                // empty string builder for next class/file
                generator.getStringBuilder().setLength(0);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return tableTemplates;
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