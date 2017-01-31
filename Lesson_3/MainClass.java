package java_3.lesson_3;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Валентин Фалин on 30.01.2017.
 * Урок 3. Средства ввода-вывода
 * Задание:
 * - Прочитать файл (около 50 байт) в байтовый массив и вывести этот массив в консоль;
 * - Последовательно сшить 5 файлов в один (файлы также ~100 байт).
 *   Может пригодиться следующая конструкция:
 *   ArrayList<InputStream> al = new ArrayList<>();
 *   ...
 *   Enumeration<InputStream> e = Collections.enumeration(al);
 * - Написать консольное приложение, которое умеет постранично читать текстовые файлы
 *   (размером > 10 mb), вводим страницу, программа выводит ее в консоль (за страницу можно
 *   принять 1800 символов). Время чтения файла должно находится в разумных пределах
 *   (программа не должна загружаться дольше 10 секунд), ну и чтение тоже не должно занимать
 *   >5 секунд.
 */
public class MainClass {
    public static void main(String[] args) throws Exception {
        //part 1
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(args[0]));
        byte[] bytes = new byte[bufferedInputStream.available()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) bufferedInputStream.read();
        }
        bufferedInputStream.close();
        for (byte b : bytes) {
            System.out.print(b + " ");
        }
        //part 2
        ArrayList<FileInputStream> arrayList = new ArrayList<>();
        arrayList.add(new FileInputStream(args[1]));
        arrayList.add(new FileInputStream(args[2]));
        arrayList.add(new FileInputStream(args[3]));
        arrayList.add(new FileInputStream(args[4]));
        arrayList.add(new FileInputStream(args[5]));
        SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(arrayList));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(args[6]));
        int x;
        while ((x = sequenceInputStream.read()) != -1) {
            bufferedOutputStream.write(x);
        }
        sequenceInputStream.close();
        bufferedOutputStream.close();
        System.out.println();
        //part 3
        FileInputStream fileInputStream = new FileInputStream(args[7]);
        RandomAccessFile randomAccessFile = new RandomAccessFile(args[7], "r");
        int listSize = 1800;
        System.out.println("Всего страниц в книге - " + fileInputStream.available() + ". На какую перейти?");
        bufferedInputStream.close();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int ourList = Integer.parseInt(bufferedReader.readLine());
        randomAccessFile.seek(ourList * listSize);
        for (int i = 0; i < listSize; i++) {
            System.out.print((char) randomAccessFile.read());
        }
        randomAccessFile.close();
    }
}
