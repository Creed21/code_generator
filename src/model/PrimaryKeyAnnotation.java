/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Aca
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface PrimaryKeyAnnotation {
    public String PrimaryKey() default "N";
}
