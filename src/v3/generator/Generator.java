/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package v3.generator;

import java.util.List;
import java.util.Map;
import java.util.Random;
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

    
    public StringBuilder getStringBuilder() {
        return classDefinition;
    }


}
