package java_3.lesson_1;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Валентин Фалин on 24.01.2017.
 * Урок 1. Обобщения.
 * Задание:
 * - Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
 * - Написать метод, который преобразует массив в ArrayList;
 * - Большая задача:
 *   Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
 *   Класс Box в который можно складывать фрукты, коробки условно сортируются по типу фрукта,
 *   поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
 *   Для хранения фруктов внутри коробки можете использовать ArrayList;
 *   Сделать метод getWeight() который высчитывает вес коробки, зная кол-во фруктов и вес
 *   одного фрукта(вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
 *   Внутри класса коробка сделать метод compare, который позволяет сравнить текущую коробку
 *   с той, которую подадут в compare в качестве параметра, true - если их веса равны, false в
 *   противной случае(коробки с яблоками мы можем сравнивать с коробками с апельсинами);
 *   Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую коробку
 *   (помним про сортировку фруктов, нельзя яблоки высыпать в коробку с апельсинами),
 *   соответственно в текущей коробке фруктов не остается, а в другую перекидываются объекты,
 *   которые были в этой коробке;
 *   Ну и не забываем про метод добавления фрукта в коробку;
 */
public class Lesson_1_v2 {
    public static void main(String[] args) {
        String[] testMass = new String[]{"Hi,", "I`m", "Bruse Wayne", ",", "and", "I`m", "Batman", "!!!"};
        printMass(testMass);
        //part 1
        changeElements(testMass, 2, 6);
        printMass(testMass);
        //part 2
        ArrayList<String> arrayListTest = massToArrayConvert(testMass);
        System.out.println(arrayListTest.get(2));
        //part 3
        Box<Apple> appleBox = new Box<>(new Apple(), new Apple());
        Box<Orange> orangeBox = new Box<>(new Orange());
        Box<Apple> appleBox2 = new Box<>(new Apple(), new Apple());
        System.out.println(appleBox.compare(appleBox2));
        System.out.println(appleBox.compare(orangeBox));
        appleBox.pour(appleBox2);
        System.out.println("aB weight - " + appleBox.getWeight() + "; aB2 weigth - " + appleBox2.getWeight());
    }

    public static <T> void changeElements(T[] mass, int firstElementIndex, int secondElementIndex) {
        T tempElement = mass[firstElementIndex];
        mass[firstElementIndex] = mass[secondElementIndex];
        mass[secondElementIndex] = tempElement;
    }

    public static <T> ArrayList<T> massToArrayConvert(T[] mass) {
        ArrayList<T> resultArr = new ArrayList<T>();
        Collections.addAll(resultArr, mass);
        return resultArr;
    }

    public static <T> void printMass(T[] mass) {
        for (T o : mass) {
            System.out.print(o + " ");
        }
        System.out.println();
    }
}
