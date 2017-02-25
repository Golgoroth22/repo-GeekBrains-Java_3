import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Валентин Фалин on 25.02.2017.
 */
public class MainClassTest {
    MainClass mainClass;

    @Before
    public void init() {
        mainClass = new MainClass();
    }

    @Test
    public void task1test1() {
        Assert.assertArrayEquals(new int[]{0, 5, 7}, mainClass.task1(new int[]{99, -1, 4, 0, 5, 7}));
    }

    @Test
    public void task1test2() {
        Assert.assertArrayEquals(new int[]{}, mainClass.task1(new int[]{1, 4}));
    }

    @Test
    public void task1test3() {
        Assert.assertArrayEquals(new int[]{0, 1}, mainClass.task1(new int[]{4, 4, 4, 0, 1}));
    }

    @Test(expected = RuntimeException.class)
    public void task1test4() {
        Assert.assertArrayEquals(new int[]{}, mainClass.task1(new int[]{0, 1}));
    }

    @Test
    public void task1test5() {
        Assert.assertArrayEquals(new int[]{}, mainClass.task1(new int[]{}));
    }

    @Test
    public void task2test1() {
        Assert.assertTrue(mainClass.task2(new int[]{1, 1, 4}));
    }

    @Test
    public void task2test2() {
        Assert.assertFalse(mainClass.task2(new int[]{1, -4, 4, 0}));
    }

    @Test
    public void task2test3() {
        Assert.assertFalse(mainClass.task2(new int[0]));
    }


}
