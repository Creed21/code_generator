/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Aca
 */
public class Generator {

    private final StringBuilder classDefinition;
    private String packageName;
    private String className;
    private Map<String, String> fields;

    public Generator() {
        this.classDefinition = new StringBuilder();
    }
    public Generator(String packageName, String tableName, Map<String, String> fields) {
        this.classDefinition = new StringBuilder();
        this.packageName = packageName;
        this.className = processClassName(tableName);
        this.fields = fields;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public Generator setClassName(String className) {
        this.className = capitalize(className);
        return this;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    
    public final String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public final String processClassName(String className) {
        Pattern pattern = Pattern.compile("_(.)");
        className = capitalize(className);
        Matcher matcher = pattern.matcher(className);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(result);

        return result.toString();
    }
    public final String processFieldName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        String[] partsOfName = fieldName.split("_");
        for(int i = 1; i < partsOfName.length; i++) {
            sb.append(capitalize(partsOfName[i]));
        }
        return sb.toString();
    }

    public Generator createPackageBeginning(String packageName) {
        classDefinition.append("package ").append(packageName).append(";\n\n");
        return this;
    }
    
    public Generator createClassBeginning() {
        classDefinition.append("public class ").append(className).append(" {\n");
        return this;
    }
    public Generator createClassEnd() {
        classDefinition.append("}");
        return this;
    }
    
    public Generator createImports() {
        fields.forEach((fieldName, fieldType) -> {
            switch (fieldType) {
                case "java.sql.Timestamp":
                    classDefinition.append( "import java.sql.Timestamp;\n");
                    break;
                case "java.sql.Date":
                    classDefinition.append( "import java.sql.Date;\n");
                    break;
                default:
                    ;
            }
        });
        classDefinition.append("\n");
        
        trimFieldType();
        
        return this;
    }
    private void trimFieldType() {
        fields.replaceAll((fieldName, fieldType) -> {
            return fieldType.substring(fieldType.lastIndexOf(".")+1);
        });
    }

    /**
     * 
     * @param fields Map<String, String> - keys = fieldNames, values = fieldTypes
     * @return 
     */
    public Generator createFileds(Map<String, String> fields) {
        String modifier = "\tprivate";
        fields.forEach((fieldName, fieldType) -> {
            classDefinition.append(modifier).append(" ").append(fieldType).append(" ").append(fieldName).append(";\n");
        });
        classDefinition.append("\n");
        return this;
    }
    
    public Generator createConstructors(Map<String, String> fields) {
        createEmptyConstructor(className);
        createFullConstructor(className, fields);
        return this;
    }

    private StringBuilder createEmptyConstructor(String tableName) {
        return classDefinition.append("\tpublic ").append(tableName).append("() {};\n");
    }
    
    private Generator createFullConstructor(String tableName, Map<String, String> fields) {
        if(fields.isEmpty()) 
            return this;
        // create signature
        classDefinition.append("\tpublic ").append(tableName).append("(");
        fields.forEach((fieldName, fieldType) -> {
            classDefinition.append(fieldType).append(" ").append(fieldName).append(", ");
        });
        classDefinition
                .deleteCharAt(classDefinition.lastIndexOf(", "))
                .append(") {\n");
        // create constructor body
        fields.forEach((fieldName, fieldType) -> {
            classDefinition.append("\t\tthis.").append(fieldName).append("= ").append(fieldName).append(";\n");
        });
        // create method end
        classDefinition.append("\t}\n\n");
        return this;
    }

    public Generator createSetters() {
        fields.forEach((fieldName, fieldType) -> {
            classDefinition
                    .append("\tpublic void ").append("set").append(capitalize(fieldName))
                    .append("(").append(capitalize(fieldType)).append(" ").append(fieldName)
                    .append(") {\n")
                    .append("\t\tthis.").append(fieldName).append("= ").append(fieldName)
                    .append(";\n")
                    .append("\t}\n");
        });
        return this;
    }

    public Generator createGetters() {
        fields.forEach((fieldName, fieldType) -> {
            classDefinition
                    .append("\tpublic ").append(fieldType);
            if(fieldType.equals("Boolean"))
                classDefinition.append(" is");
            else
                classDefinition.append(" get");
            
            classDefinition.append(capitalize(fieldName))
                    .append("() {\n")
                    .append("\t\treturn this.").append(fieldName).append(";\n")
                    .append("\t}\n");
        });
        return this;
    }

    public Generator createToString() {
        classDefinition
                .append("\n\t@Override")
                .append("\n\tpublic String toString() {\n")
                .append("\t\treturn \"").append(className).append("{");
        fields.forEach((fieldName, fieldType) -> {
            classDefinition.append(fieldName).append("=\"+").append(fieldName).append("+\", ")
                ;
        });
        int length = classDefinition.length();
        classDefinition
                .deleteCharAt(length-1)
                .deleteCharAt(length-2)
                .deleteCharAt(length-3)
                .append("\"}\";")
                .append("\n\t}\n");
        
        return this;
    }
    
    public StringBuilder getStringBuilder() {
        return classDefinition;
    }
}
