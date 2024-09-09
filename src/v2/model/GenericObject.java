/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.model;

//import java.lang.annotation.Annotation;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Aca
 */
public class GenericObject implements Serializable {
    private static final long serialVersionUID = 123504822L;

    public GenericObject() {
    }
    
    public String getClassName() {
        return getClass().getSimpleName().toUpperCase(); //className;
    }
        
    protected String getGenericWhere() {
        return "WHERE 1=1 ";
    }        
    public String makeSelectRefl() {
        return "SELECT "+getColumNamesRefl()
                +"\nFROM " +  getClassName()//className
                +"\n"+getGenericWhere()
                +getWhereRefl();
    }
    public String makeSelectReflPrimKey() {
        return "SELECT "+getColumNamesRefl()
                +"\nFROM " + getClassName() //className
                +"\n"+getGenericWhere()
                +getWherePrimKeyRefl();
    }
    public String makeInsertRefl() {
         return "INSERT INTO "+getClassName() //className
                 +"\n"
                 +"("+getColumNamesRefl()+")\n"+
                 "VALUES("+getColumnValuesRefl()+")";
    }
    public String makeUpdateRefl() {
        return "UPDATE "+getClassName() //className
                +" SET\n"+
                getColumnValuesForUpdateRefl()+"\n"
                +getGenericWhere()
//                +(!getWherePrimKeyRefl().isEmpty() ? getWherePrimKeyRefl(): getWhereRefl())
                +getWherePrimKeyRefl()
                ;
    }
    public String makeDeleteRefl() {
        return "DELETE FROM "+getClassName() //className
                +"\n"
                +getGenericWhere()
                +(!getWherePrimKeyRefl().isEmpty() ? getWherePrimKeyRefl(): getWhereRefl())
                ;
    }
    protected String getColumNamesRefl() {
        StringBuilder columns = new StringBuilder("");
        for(Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String field_name = f.getName();
//            System.out.println("getColumNamesRefl -> fieldName: "+field_name);
            if(f.getName().contains("serialVersionUID")) {
//                System.out.println("getColumNamesRefl -> fieldName contains serialVersionUID: "+field_name);
                f.setAccessible(false);
                continue;
            }
            columns.append(field_name.concat(", "));
            f.setAccessible(false);
        }
        String columns_str = columns.toString();
        return columns_str.substring(0, columns_str.lastIndexOf(","));
    }
    /**
     * Returning prepared value for SQL based on it's type.
     * @param attr_type
     * @param field_value
     * @return prepared value for SQL based on it's type.
     */
    private String getFieldValueFromType(String attr_type, Object field_value) {
        switch(attr_type) {
            case "Integer":
            case "Double":
                return field_value.toString();
            case "String":
            case "Timestamp":
            case "Date":
                return "'"+field_value.toString()+"'";
            default:
                return field_value.toString();
        }
    }
    
    protected String getColumnValuesRefl() {
        String values = "";
        for(Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(f.getName().contains("serialVersionUID")) {
                f.setAccessible(false);
                continue;
            }
            String type = f.getType().getName();
            type = type.substring(type.lastIndexOf(".")+1);
            
            Object value = null;
            try {
                value = f.get(this);
            } catch (IllegalArgumentException | IllegalAccessException ex) {Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex); }            
            
            if(value == null) {
                values += "null, ";
                continue;
            }
       
            values += getFieldValueFromType(type, value);
            values += ", ";
            f.setAccessible(false);
        }
        
        values = values.substring(0, values.lastIndexOf(","));
        return values;
    }
    
    protected String getWherePrimKeyRefl() {
        String where_pk = "";
        for(Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(f.getName().contains("serialVersionUID")) {
                f.setAccessible(false);
                continue;
            }
            if(f.isAnnotationPresent(PrimaryKeyAnnotation.class)) {
                String field_name = f.getName();
                String attr_type = f.getType().getName();
                attr_type = attr_type.substring(attr_type.lastIndexOf(".")+1);

                Object field_value = null;
                try { // id = field_value
                    field_value = f.get(this);
                } catch (IllegalArgumentException | IllegalAccessException ex) {Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex); }            

                if(field_value == null)  continue;
                            // 1 = 1 and id = 1 and userName = 'concreteUserName'
                where_pk += "\n and "+field_name+" = ";

                where_pk += getFieldValueFromType(attr_type, field_value); //getFieldValueFromType(attr_type, where_pk, field_value);
            }
            f.setAccessible(false);
        }
        return where_pk;
    }

    protected  Object fetchFieldValueFromResultSet(ResultSet resultSet, String param_type, String fieldName) throws SQLException {
        Object field_value = null;
        String param_type_trim = param_type.substring(param_type.lastIndexOf(".")+1);
        switch(param_type_trim) {
            case "Integer":
                    field_value = resultSet.getInt(fieldName);
                break;
            case "Double":
                    field_value = resultSet.getDouble(fieldName);
                break;
            case "String":
                    field_value = resultSet.getString(fieldName);
                break;
            case "Timestamp":
                    field_value = resultSet.getTimestamp(fieldName);
                    break;
            case "Date":
                    field_value = resultSet.getDate(fieldName);
                break;
            default:
                field_value = resultSet.getObject(fieldName);
                break;
        }
        return field_value;
    }
    
    protected String getWhereRefl() {
        String where = "";
        for(Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(f.getName().contains("serialVersionUID")) {
                f.setAccessible(false);
                continue;
            }
            String field_name = f.getName();
            String attr_type = f.getType().getName();
            attr_type = attr_type.substring(attr_type.lastIndexOf(".")+1);
            
            Object field_value = null;
            try {
                field_value = f.get(this);
            } catch (IllegalArgumentException | IllegalAccessException ex) {Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex); }            

            if(field_value == null)  continue;

            where += "\n and "+field_name+" = ";
            where += getFieldValueFromType(attr_type, field_value);// getFieldValueFromType(attr_type, where, field_value);
            f.setAccessible(false);
        }
        
        return where;
    }
    public GenericObject getRecordRefl(ResultSet resultSet) {
        GenericObject result = null;        
        Class<?> cl = this.getClass();
        Constructor annotated_constructor = null;
        List<Object> values = new LinkedList<>();
        
        for(Constructor c : cl.getConstructors()) {
                if(c.isAnnotationPresent(ConstructorAnnotation.class)) {
                    annotated_constructor = c;
                    Parameter[] parametri = c.getParameters();
                    Class[] param_type_to_trim = new Class[parametri.length];
                    for(int i = 0; i < parametri.length; i++ ) {
                        param_type_to_trim[i] = parametri[i].getType();
                        String param_type = param_type_to_trim[i].toGenericString();
                        param_type = param_type.contains(" ") ? param_type.substring(param_type.lastIndexOf(" ")+1) : param_type;
//                        System.out.println("param_name: "+parametri[i].getName());
                        for(Field f : cl.getDeclaredFields()) {
                            f.setAccessible(true);
                            String fieldName= f.getName();
                            if(fieldName.equals(parametri[i].getName())) {
//                                if(param_type.equals("java.sql.Timestamp"))
//                                System.out.println("polje koje sadrzi timestamp: param_type: "+param_type+"  field_name: "+ fieldName+" full_param_type_name: "+param_type_to_trim[i].toGenericString());
                                Object field_value = null;
                                try {
                                    field_value = fetchFieldValueFromResultSet(resultSet, param_type, fieldName);
                                } catch (SQLException ex) {
//                                    Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("Exception: field_value catch error!!!!!!!");
                                }
//                                System.out.println("***********field value: " + (field_value == null ? "field value je null" : field_value));
                                values.add(field_value);
                            }
                            f.setAccessible(false);
                        }
                    }
                }
        }
        try {
            Object[] values_array = values.toArray();
            System.out.println("values:");
            System.out.println(values);
            result = (GenericObject) annotated_constructor.newInstance(values_array);
        
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex);}
        
        return result;
    }

    private String getColumnValuesForUpdateRefl() {
        String values = "";
        for(Field f : getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(f.getName().contains("serialVersionUID") || f.isAnnotationPresent(PrimaryKeyAnnotation.class)) {
                f.setAccessible(false);
                continue;
            }
            if(f.getName().contains("usc") || f.getName().contains("dtc")) {
                continue;
            }
            String type = f.getType().getName();
            type = type.substring(type.lastIndexOf(".")+1);
            
            values += f.getName()+" = ";
            
            Object value = null;
            try {
                value = f.get(this);
            } catch (IllegalArgumentException | IllegalAccessException ex) {Logger.getLogger(GenericObject.class.getName()).log(Level.SEVERE, null, ex); }            
            
            if(value == null) {
                values += "null,\n";
                continue;
            }
            
            values += getFieldValueFromType(type, value);
            values += ",\n";
            f.setAccessible(false);
        }
        
        values = values.substring(0, values.lastIndexOf(","));
        return values;
    }
    
}
