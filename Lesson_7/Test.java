package com.java_3.lesson_7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Created by Валентин Фалин on 02.03.2017.
 */
public @interface Test {
    int priority() default 5;
}
