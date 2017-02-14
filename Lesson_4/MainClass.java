package java_3.lesson_4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Валентин Фалин on 05.02.2017.
 * Урок 4. Многопоточность
 * - Создать три потока, каждый из которых выводит определенную букву(A, B и C) 5 раз, порядок
 * должен быть именно ABСABСABС. Используйте wait/notify/notifyAll.
 * - Написать совсем небольшой метод, в котором 3 потока построчно пишут данные в файл (штук
 * по 10 записей, с периодом в 20 мс)
 * - Написать класс МФУ на котором возможны одновременная печать и сканирование
 * документов, при этом нельзя одновременно печатать два документа или сканировать (при
 * печати в консоль выводится сообщения "отпечатано 1, 2, 3,... страницы", при сканировании то
 * же самое только "отсканировано...", вывод в консоль все также с периодом в 50 мс.)
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
        threeWriters();
        //part 3
        MFU mfu = new MFU(10);//В конструкторе указываем сколько листов бумаги закладываем в ящик МФУ
        new Thread(() -> mfu.printList("Война и Мир в двух словах", 6)).start();
        new Thread(() -> mfu.printList("Преступление и Наказание в деталях", 5)).start();
        new Thread(() -> mfu.scanList("Школьный фотоальбом", 5)).start();
    }

    private static void threeWriters() {
        try {
            PrintWriter pw = new PrintWriter("1.txt");
            for (int i = 1; i < 4; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        for (int j = 1; j < 11; j++) {
                            pw.append("Поток " + finalI + ", cтрока " + j + Thread.currentThread().getName() + "\n");
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
        private Object printLock = new Object();
        private Object scanLock = new Object();

        public MFU(int listsInBox) {
            this.listsInBox = listsInBox;
            System.out.printf("\nВ МФУ загружено страниц: %s\n", listsInBox);
        }

        public boolean checkBox() {
            if (listsInBox == 0) {
                return false;
            }
            return true;
        }

        public void printList(String fileName, int lists) {
            synchronized (printLock) {
                for (int i = 1; i <= lists; i++) {
                    if (!checkBox()) {
                        System.out.printf("В МФУ закончилась бумага!!! Документ <%s> напечатан не полностью.\n", fileName);
                        break;
                    }
                    System.out.printf("Печатается документ <%s>, страница %d из %d\n", fileName, i, lists);
                    listsInBox--;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void scanList(String fileName, int lists) {
            synchronized (scanLock) {
                for (int i = 1; i <= lists; i++) {
                    System.out.printf("Сканируется документ <%s>, страница %d из %d\n", fileName, i, lists);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
