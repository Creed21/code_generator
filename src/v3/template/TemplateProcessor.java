package v3.template;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    // String that represents the class/file definition
    private StringBuilder classDefinition;
    private String className;
    private TemplateConfig templateConfig;
    private String template;
    private List<TemplateConfig> templateConfigs;
    private List<String> templateConfigNames;
    private Gson gson;


    public TemplateProcessor() {
        this.generator = new Generator();
        this.classDefinition = new StringBuilder();
        this.gson = new Gson();
    }

    public TemplateProcessor(TemplateConfig templateConfig, String template) {
        TemplateProcessor tp = new TemplateProcessor();
        this.templateConfig = templateConfig;
        this.template = template;
    }

    public String parsePackageName(String packageName) {
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

    // replace basic and remaining placeholders
    // set for the template newLine and indent values
    // ex. className/TableName, file
    // Beginning, packageName
    //      remaining fields and field types


    private void processPlaceholders(Map<String, String> placeholders) {
        // process for each field placeholders
        // find forEachField('comnad') and for each field in table replace the constuct with field and type values

        fields.forEach((fieldName, fieldType) -> {

        });

        // process remaining placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()
                    .replace("{className}", className)
                    .replace("{classNameLowercase}", lowercase(className))
                    .replace("{classNameCapitalized}", capitalize(className));

            placeholders.put(key, value);
        }
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

        // for each table
        // go through each template file pair
        // and generate files
        tableNames.forEach(tableName -> {
            // for each template config file
            jsonTemplateConfigs.forEach(jsonTemplateConfig -> {
                // load template file path from path defined in config named templateFile
                // in order to read file template
                ObjectMapper objectMapper = new ObjectMapper();
                try {

//                    TemplateConfig config = objectMapper.readValue(new File("config.json"), TemplateConfig.class);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                // process template file
                String tableTemplate = processTemplates(jsonTemplateConfigs, outputPath, packageName, schema, tableName);

                createFile(outputPath, tableTemplate, tableName, templateConfig.getExtension());
            });

            // load template file
            // process template file
//            List<String> tableTemplates = processTemplates(jsonTemplateConfigs, outputPath, packageName, schema, tableName);

//            tableTemplates.forEach(tableTemplate -> {
//                createFile(outputPath, tableName, tableTemplate);
//            });
        });
        // process remaining placeholders
        // create file

        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    public void createFile(String filePath, String fileData, String className, String extension) {
        // Create the file path
        String outputPath = filePath + "/" + className + "." + extension;
        //        String outputPath = filePath + "/" + className + "." + extension;
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
    public String processTemplates(
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
                List<Placeholder> placeholders = template.getPlaceholders();

                String filePath = path + "/" + packageName + "/" + className + placeholders.get(0);

                // fetch fields from database
                Map<String, String> fields = generator.getFields();

                // process for each command

                // process remaining placeholders

                // tetClassDefinition
//                String templateForEach = parseForEachCommand((String) template.get("classDefinition"), fields);


                // Now process the main class definition template
//                String classTemplate = (String) template.get("classDefinition");

                // Replace all placeholders in the class template
//                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
//                    classTemplate = classTemplate.replace("{" + entry.getKey() + "}", entry.getValue());
//                }



//                tableTemplates.add(classTemplate);

                // empty string builder for next class/file
                generator.getStringBuilder().setLength(0);

                reader.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
//        return tableTemplates;
        return null;
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