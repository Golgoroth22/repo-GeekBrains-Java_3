package com.java_3.lesson_7;

/**
 * Created by Валентин Фалин on 02.03.2017.
 */
public class TestClass {
    @Test(priority = 7)
    static void a() {
        System.out.println("a");
    }

    @Test(priority = 1)
    static void b() {
        System.out.println("b");
    }

    @Test
    static void c() {
        System.out.println("c");
    }

    @Test
    static void d() {
        System.out.println("d");
    }

    @Test
    static void e() {
        System.out.println("e");
    }

    @Test
    static void f() {
        System.out.println("f");
    }

    @Test
    static void g() {
        System.out.println("g");
    }

    @Test(priority = 7)
    static void h() {
        System.out.println("h");
    }

    @BeforeSuite
    static void init() {
        System.out.println("Start testing");
    }

    @AfterSuite
    static void exit() {
        System.out.println("End of testing");
    }
}
