package java_3.lesson_4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Валентин Фалин on 05.02.2017.
 * Урок 4. Многопоточность
 * - Создать три потока, каждый из которых выводит определенную букву(A, B и C) 5 раз, порядок
 *   должен быть именно ABСABСABС. Используйте wait/notify/notifyAll.
 * - Написать совсем небольшой метод, в котором 3 потока построчно пишут данные в файл (штук
 *   по 10 записей, с периодом в 20 мс)
 * - Написать класс МФУ на котором возможны одновременная печать и сканирование
 *   документов, при этом нельзя одновременно печатать два документа или сканировать (при
 *   печати в консоль выводится сообщения "отпечатано 1, 2, 3,... страницы", при сканировании то
 *   же самое только "отсканировано...", вывод в консоль все также с периодом в 50 мс.)
 */
public class MainClass {
    private static char[] chars = {'A', 'B', 'C'};
    private static char currentChar = chars[2];
    private static Object lock = new Object();

    public static void main(String[] args) {
        //part 1
        new Thread(() -> printChar(chars[0], chars[2])).start();
        new Thread(() -> printChar(chars[1], chars[0])).start();
        new Thread(() -> printChar(chars[2], chars[1])).start();
        //part 2
        //Надеюсь я правильно понял 2е задание и не нужно было ничего синхронизировать :)
        threeWriters();
        //part 3
        System.out.println();
        MFU mfu = new MFU(10);//В конструкторе указываем сколько листов закладываем в ящик МФУ
        for (int i = 1; i <= 3; i++) { //Создаем 3 потока для печати из МФУ
            int finalI = i;
            new Thread(() -> {
                while (mfu.checkBox()) { //Проверка на оставшееся количество листов, которые доступны для печати
                    mfu.printList(finalI); //Печатаем
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        for (int i = 1; i <= 3; i++) { //Создаем 3 потока для сканирования
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 1; j <= 4; j++) { //Сканировать будет по 4 раза каздым потоком, т.к. думаю эта операция не ограничена необходимыми материалами для нее :)
                        mfu.scanList(finalI, j);//Сканируем
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private static void threeWriters() {
        try {
            PrintWriter pw = new PrintWriter("1.txt");
            for (int i = 1; i < 4; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        for (int j = 1; j < 11; j++) {
                            pw.append("Поток " + finalI + ", cтрока " + j + "\n");
                            pw.flush();
                            Thread.sleep(20);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printChar(char printingChar, char previousChar) {
        synchronized (lock) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentChar != previousChar)
                        lock.wait();
                    System.out.print(printingChar);
                    currentChar = printingChar;
                    lock.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MFU {
        private int listsInBox;
        private Object lock1 = new Object();
        private Object lock2 = new Object();

        public MFU(int listsInBox) {
            this.listsInBox = listsInBox;
            System.out.println("В МФУ загружено страниц:" + listsInBox);
        }

        public boolean checkBox() {
            if (listsInBox == 0) {
                return false;
            }
            return true;
        }

        public void printList(int i) {
            synchronized (lock1) {
                System.out.println("Отпечатана страница. Отпеатал поток №" + i + ". Осталось отпечатать:" + listsInBox);
                listsInBox--;
            }
        }

        public void scanList(int i, int j) {
            synchronized (lock2) {
                System.out.println("Отсканирована страница №" + j + ". Отсканировал поток №" + i);
            }
        }
    }
}
