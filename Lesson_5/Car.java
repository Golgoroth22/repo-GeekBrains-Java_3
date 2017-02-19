package java_3.lesson_5;

import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    public static CyclicBarrier cyclicBarrier;
    private static int CARS_COUNT;
    private static Object lock = new Object();

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        cyclicBarrier = new CyclicBarrier(MainClass.CARS_COUNT);
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            if (cyclicBarrier.await() == 0) {
                System.out.println(MainClass.startMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        synchronized (lock) {
            CARS_COUNT--;
            if (MainClass.CARS_COUNT - 1 == CARS_COUNT) {
                System.out.println(name + MainClass.winMessage);
            }
            if (CARS_COUNT == 0) {
                System.out.println(MainClass.finishMessage);
            }
        }
    }
}

