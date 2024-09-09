/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package v3.generator;

import v2.db.DBConfig;
import v3.template.TemplateProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aca
 */
public class GenerateEntities //implements GenerationProcess
{

    protected DBConnector dBConnector;
    protected Generator generator;
    public TemplateProcessor templateProcessor;

    public GenerateEntities(DBConfig dBConfig) {
        this.dBConnector = DBConnector.getInstance(dBConfig);
        this.generator = new Generator();
        templateProcessor = new TemplateProcessor();
    }
    public GenerateEntities(DBConnector dBConnector) {
        this.dBConnector = dBConnector;
        this.generator = new Generator();
        templateProcessor = new TemplateProcessor();
    }
    public StringBuilder getStringBuilder() {
        return generator.getStringBuilder();
    }

    public Generator generateSingleClass(String packageName, String schemaName, String tableName, String template) {
        Map<String, String> readFields = dBConnector.readFields(schemaName, tableName);

        templateProcessor.setFields(readFields);

        // make class / file beginning
        // parse foreachfields
        // replace keywords
        // make class end

        return generator
                .setClassName(tableName)
                .createPackageBeginning(packageName)
                .createClassBeginning()
                .createClassEnd();
    }


    public void generateFiles(String path, String packageName, String schemaName, List<String> tableNames, String template) {
        String outputPath = path+"/"+packageName ;
        new File(outputPath).mkdirs();
        tableNames.forEach(tableName -> {
                generateSingleClass(packageName, schemaName, tableName, template);
                createEntityFile(outputPath, tableName, generator.getStringBuilder().toString(), template);
                generator.getStringBuilder().setLength(0);

        });
        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    
    public void createEntityFile(String outputPath, String className, String fileExtension, String classData) {
        String filePath = outputPath + "/" + className + ".java";
        // Write the content to the Java class file
        try ( FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(classData);
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing class to file: " + e.getMessage());
        }
    }

}
