import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Валентин Фалин on 26.02.2017.
 */
public class Task3Test {
    Task3 task3;

    @Before
    public void init() {
        task3 = new Task3();
        task3.connect();
    }

    @Test
    public void task3test() {
        task3.addStudent("Stark", 199);
        task3.updateStudent("Stark", 189);
        ArrayList<String> testlist = task3.studentInfo("Stark");
        System.out.println(testlist.get(0));
        task3.deleteStudent("Stark");
    }

    @After
    public void disconnect() {
        task3.disconnect();
    }

}
