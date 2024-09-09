/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package v2.generator;

import v2.db.DBConfig;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class GenerateEntitiesORM {

    protected DBConnector dBConnector;
    protected Generator generator;

    public GenerateEntitiesORM(DBConfig dBConfig) {
        this.dBConnector = DBConnector.getInstance(dBConfig);
        this.generator = new Generator();
    }
    public GenerateEntitiesORM(DBConnector dBConnector) {
        this.dBConnector = dBConnector;
        this.generator = new Generator();
    }

    public void generateFiles(String mode, String path, String packageName, String schemaName, List<String> tableNames) {
        if(path == null)
            throw new IllegalArgumentException("Path can not be null.");

        switch(mode) {
            case "ORM Model klase":
                generateORMClasses(path, packageName, schemaName, tableNames);
                break;
            case "serverska ORM aplikacija":
                generateORMClasses(path, "model", schemaName, tableNames);
                copyServer(Path.of(path));
                buildProject(path);
                createJar(path, path+"\\out\\MainServer.jar", "v2.main.MainServer");
                break;
            default:
                throw new IllegalArgumentException("Nepoznat mod rada.");
        }

        System.out.println("Uspšeno ste generisali kod za domenske klase.");
    }

    public void generateORMClasses(String path, String packageName, String schemaName, List<String> tableNames) {
        String outputPath = path+"\\src\\"+packageName;
        new File(outputPath).mkdirs();
        tableNames.forEach(tableName -> {
            generateSingleClassORM(packageName, schemaName, tableName);
            createEntityFile(outputPath, tableName, generator.getStringBuilder().toString());
            generator.getStringBuilder().setLength(0);

        });
    }

    private void buildProject(String path) {
        String sourceDir = path + "\\src";
        String outputDir = path + "\\out";

        // Compile the project
        if (compileProject(sourceDir, outputDir)) {
            System.out.println("Project compiled successfully.");
        } else {
            System.err.println("Compilation failed.");
        }
    }

    public Generator generateSingleClassORM(String packageName, String schemaName, String tableName) {
        Map<String, String> readFields = dBConnector.readFields(schemaName, tableName);
        List<String> primaryKeys = dBConnector.readPrimKeysForTable(schemaName, tableName);
        generator.setFields(readFields);
        return generator
                .setClassName(tableName)
                .createPackageBeginning(packageName)
                .createImportsORM()
                .createClassBeginningORM()
                .createFieldsForORM(primaryKeys)
                .createConstructorsORM(readFields)
                .createSetters()
                .createGetters()
                .createToString()
                .createClassEnd();
    }

    public void createEntityFile(String outputPath, String className, String classData) {
        String filePath = outputPath + "\\" + generator.capitalize(className) + ".java";
        // Write the content to the Java class file
        try ( FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(classData);
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing class to file: " + e.getMessage());
        }
    }

    private void copyServer(Path destinationDirectoryOnFileSystem) {
        // List of directories to copy from the "src" directory
        List<String> filenames = List.of(
                "controller",
                "db",
                "exceptions",
                "systemOperation",
                "transferobject",
                "server",
                "main",
                "model"
        );

        // Copy each directory listed in "filenames" from "src"
        filenames.forEach(fileName -> {
            Path sourcePath = Paths.get("").resolve("src").resolve(fileName).toAbsolutePath();
            copyDirectory(sourcePath, destinationDirectoryOnFileSystem.resolve("src").resolve(fileName));
        });

        // Copy the entire "dist" directory
        Path distSourcePath = Paths.get("").resolve("dist").toAbsolutePath();
        copyDirectory(distSourcePath, destinationDirectoryOnFileSystem.resolve("dist"));
    }

    private void copyDirectory(Path sourcePath, Path destinationPath) {
        try {
            Files.walk(sourcePath).forEach(sourceFilePath -> {
                try {
                    // Resolve the relative path from the source root directory
                    Path relativePath = sourcePath.relativize(sourceFilePath);

                    // Resolve the destination path based on the relative path
                    Path destinationFilePath = destinationPath.resolve(relativePath);

                    if (Files.isDirectory(sourceFilePath)) {
                        // Create the directory at the destination if it doesn't exist
                        Files.createDirectories(destinationFilePath);
                    } else {
                        // Ensure the parent directories of the destination file exist
                        Files.createDirectories(destinationFilePath.getParent());

                        // Copy the file to the destination
                        Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                        System.out.println("Copied: " + sourceFilePath + " -> " + destinationFilePath);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GenerateEntitiesORM.class.getName()).log(Level.SEVERE, "Error copying file: " + sourceFilePath, ex);
                }
            });
            System.out.println("Directory " + sourcePath.getFileName() + " copied successfully.");
        } catch (IOException ex) {
            Logger.getLogger(GenerateEntitiesORM.class.getName()).log(Level.SEVERE, "Error traversing directory: " + sourcePath, ex);
        }
    }

    private static boolean compileProject(String sourceDir, String outputDir) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("No Java compiler available. Make sure you're running a JDK, not a JRE.");
            return false;
        }

        // Collect all Java files in the source directory
        List<String> javaFiles = new ArrayList<>();
        collectJavaFiles(new File(sourceDir), javaFiles);

        // Add the -d option to specify the output directory
        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(outputDir);

        // Combine options and file paths
        options.addAll(javaFiles);

        // Compile the project
        int result = compiler.run(null, null, null, options.toArray(new String[0]));
        return result == 0;
    }

    private static void collectJavaFiles(File dir, List<String> javaFiles) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                collectJavaFiles(file, javaFiles);
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file.getAbsolutePath());
            }
        }
    }

    private void createJar(String outputDir, String jarName, String mainClassName) {
        try {
            // Create the META-INF directory within the output directory
            Path metaInfDir = Paths.get(outputDir, "META-INF");
            Files.createDirectories(metaInfDir);

            // Create the manifest file in the META-INF directory
            File manifestFile = new File(metaInfDir.toString(), "MANIFEST.MF");
            try (FileWriter writer = new FileWriter(manifestFile)) {
                writer.write("Manifest-Version: 1.0\n");
                writer.write("Main-Class: " + mainClassName + "\n");
            }

            // Build the jar command
            List<String> command = new ArrayList<>();
            command.add("jar");
            command.add("cfm");
            command.add(jarName);
            command.add(manifestFile.getAbsolutePath());
            command.add("-C");
            command.add(outputDir);
            command.add(".");

            // Execute the jar command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("JAR file created successfully: " + jarName);
            } else {
                System.err.println("Failed to create JAR file. Exit code: " + exitCode);
            }

            // Optionally, clean up the temporary manifest file
            // manifestFile.delete();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
