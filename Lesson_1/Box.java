package java_3.lesson_1;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Валентин Фалин on 24.01.2017.
 */
public class Box<T extends Fruit> {
    ArrayList<T> arrayList = new ArrayList<T>();

    public Box(T... elements) {
        addFruit(elements);
    }

    public void addFruit(T... elements) {
        Collections.addAll(this.arrayList, elements);
    }

    public float getWeight() {
        if (this.arrayList.size() != 0) {
            return this.arrayList.size() * arrayList.get(0).weigth;
        }
        return 0.0f;
    }

    public boolean compare(Box<?> box) {
        if ((this.arrayList.size() * this.arrayList.get(0).weigth - box.arrayList.size() * box.arrayList.get(0).weigth < 0.01f)) {
            return true;
        }
        return false;
    }

    public void pour(Box<T> box) {
        T newFruit = arrayList.get(0);
        for (int i = 0; i < box.getWeight() / newFruit.weigth; i++) {
            this.arrayList.add(newFruit);
        }
        box.arrayList.clear();
    }
}
