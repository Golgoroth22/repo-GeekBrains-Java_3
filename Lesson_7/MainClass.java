package com.java_3.lesson_7;

import java.lang.reflect.Method;

/**
 * Created by Валентин Фалин on 02.03.2017.
 * Урок 7. Reflection и аннотации
 * Задание:
 * Создать класс, который может выполнять «тесты», в качестве тестов выступают классы с наборами
 * методов с аннотациями @Test. Для этого у него должен быть статический метод start(), которому в
 * качестве параметра передается или объект типа Class, или имя класса. Из «класса-теста» вначале
 * должен быть запущен метод с аннотацией @BeforeSuite если такой имеется, далее запущены методы
 * с аннотациями @Test, а по завершению всех тестов – метод с аннотацией @AfterSuite. К каждому
 * тесту необходимо также добавить приоритеты (int числа от 1 до 10), в соответствии с которыми будет
 * выбираться порядок их выполнения, если приоритет одинаковый то порядок не имеет значения.
 * Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном
 * экземпляре, иначе необходимо бросить RuntimeException при запуске «тестирования».,
 */
public class MainClass {
    static int MIN_PRIORITY = 1;
    static int MAX_PRIORITY = 10;

    public static void main(String[] args) {
        start(TestClass.class);
    }

    static void start(Class Class) {
        checkInitAndExit(Class);
        Method[] methods = Class.getDeclaredMethods();
        for (Method o : methods) {
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                try {
                    o.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = MAX_PRIORITY; i >= MIN_PRIORITY; i--) {
            for (Method o : methods) {
                if (o.isAnnotationPresent(Test.class)) {
                    if (o.getAnnotation(Test.class).priority() == i) {
                        try {
                            o.invoke(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        for (Method o : methods) {
            if (o.isAnnotationPresent(AfterSuite.class)) {
                try {
                    o.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void checkInitAndExit(Class Class) {
        Method[] methods = Class.getDeclaredMethods();
        int afterInt = 0;
        int beforeInt = 0;
        for (Method o : methods) {
            if (o.isAnnotationPresent(AfterSuite.class)) {
                afterInt++;
            }
        }
        for (Method o : methods) {
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                beforeInt++;
            }
        }
        if (afterInt != 1 || beforeInt != 1) {
            throw new RuntimeException();
        }
    }
}
