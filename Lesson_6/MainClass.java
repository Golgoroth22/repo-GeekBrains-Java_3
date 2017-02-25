/**
 * Created by Валентин Фалин on 25.02.2017.
 * Урок 6. Тестирование с использованием JUnit, класс Assert
 * Задание:
 * - Написать метод, которому в качестве аргумента передается не пустой одномерный
 *   целочисленный массив, метод должен вернуть новый массив, который получен путем
 *   вытаскивания элементов из исходного массива, идущих после последней четверки. Входной
 *   массив должен содержать хотя бы одну четверку, в противном случае в методе необходимо
 *   выбросить RuntimeException.
 * - Написать набор тестов для этого метода (варианта 3-4 входных данных)
 *   вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ]
 */
public class MainClass {

    public int[] task1(int[] mass) {
        int[] result = new int[0];
        if (mass.length != 0) {
            boolean check = false;
            int index = 0;
            for (int i = 0; i < mass.length; i++) {
                if (mass[i] == 4) {
                    check = true;
                    index = i + 1;
                }
            }
            if (check) {
                result = new int[mass.length - index];
                for (int i = 0; i < result.length; i++) {
                    result[i] = mass[i + index];
                }
            } else {
                throw new RuntimeException();
            }
        }
        return result;
    }

    public boolean task2(int[] mass) {
        if (mass.length != 0) {
            for (int i : mass) {
                if (i != 1 && i != 4) return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
