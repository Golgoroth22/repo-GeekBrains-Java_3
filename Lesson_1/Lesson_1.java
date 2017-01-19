package java_3.lesson_1;

import java.util.ArrayList;

/**
 * Created by Валентин Фалин on 19.01.2017.
 * Урок 1. Обобщения.
 * Задание:
 * - Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
 * - Написать метод, который преобразует массив в ArrayList;
 */
public class Lesson_1<T> {
    T firsTempElement;
    T secondTempElement;

    public void changeElements(T[] mass, int firstElementIndex, int secondElementIndex) {
        firsTempElement = mass[firstElementIndex];
        secondTempElement = mass[secondElementIndex];
        mass[firstElementIndex] = secondTempElement;
        mass[secondElementIndex] = firsTempElement;
    }

    public ArrayList<T> massNeogeneConverter(T[] mass) {
        ArrayList<T> resultList = new ArrayList<T>();
        for (T element : mass) {
            resultList.add(element);
        }
        return resultList;
    }

    public static void main(String[] args) {
        String[] testMass = new String[]{"Hi, ", "I`m ", "Bruse Wayne", ", and ", "I`m ", "Batman", "!!!"};
        for (String s : testMass) {
            System.out.print(s);
        }
        // part_1
        System.out.println();
        new Lesson_1<String>().changeElements(testMass, 2, 5);
        for (String s : testMass) {
            System.out.print(s);
        }
        // part_2
        if (testMass.getClass().getName() instanceof String) {
            ArrayList<String> resultArrayList = new Lesson_1<String>().massNeogeneConverter(testMass);
            System.out.println("\nresultArrayList is: " + resultArrayList.getClass().getName());
        }
    }
}
