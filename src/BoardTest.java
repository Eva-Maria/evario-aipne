import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by eve on 4/19/16.
 */
public class BoardTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testTranslatingIsIdempotent() throws Exception {
        Board board = new Board();

        int[] yAndX = new int[]{7, 0};

        yAndX = Board.translateFieldsClockwise(yAndX[0], yAndX[1]);
        yAndX = Board.translateFieldsClockwise(yAndX[0], yAndX[1]);
        yAndX = Board.translateFieldsClockwise(yAndX[0], yAndX[1]);

        Assert.assertTrue(Arrays.equals(yAndX, new int[]{7, 0}));
    }

    @Test
    public void testToString() {
        Board board = new Board();
        System.out.println(board.toString());
    }

}