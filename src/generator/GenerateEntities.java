/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator;

import db.DBConfig;

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

    public GenerateEntities(DBConfig dBConfig) {
        this.dBConnector = DBConnector.getInstance(dBConfig);
        this.generator = new Generator();
    }
    public GenerateEntities(DBConnector dBConnector) {
        this.dBConnector = dBConnector;
        this.generator = new Generator();
    }
    public StringBuilder getStringBuilder() {
        return generator.getStringBuilder();
    }

    public Generator generateSingleClass(String packageName, String schemaName, String tableName) {
        Map<String, String> readFields = dBConnector.readFields(schemaName, tableName);
        generator.setFields(readFields);
        return generator
                .setClassName(tableName)
                .createPackageBeginning(packageName)
                .createImports()
                .createClassBeginning()
                .createFileds(readFields)
                .createConstructors(readFields)
                .createSetters()
                .createGetters()
                .createToString()
                .createClassEnd();
    }


    public void generateFiles(String path, String packageName, String schemaName, List<String> tableNames) {
        String outputPath = path+"/"+packageName ;
        new File(outputPath).mkdirs();
        tableNames.forEach(tableName -> {
                generateSingleClass(packageName, schemaName, tableName);
                createEntityFile(outputPath, tableName, generator.getStringBuilder().toString());
                generator.getStringBuilder().setLength(0);

        });
        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    
    public void createEntityFile(String outputPath, String className, String classData) {
        String filePath = outputPath + "/" + generator.capitalize(className) + ".java";
        // Write the content to the Java class file
        try ( FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(classData);
        } catch (IOException e) {
            System.err.println("Error writing class to file: " + e.getMessage());
        }
    }

}
