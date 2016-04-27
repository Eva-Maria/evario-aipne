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
        for (int y = 7; y >= 0; y--) {
            int rowLength = y * 2 + 1;
            for (int x = 0; x < rowLength; x++) {
                int[] expected = {x, y};
                int[] xAndY = {x, y};

                xAndY = Board.translateFieldsCounterClockwise(xAndY);
                xAndY = Board.translateFieldsCounterClockwise(xAndY);
                xAndY = Board.translateFieldsCounterClockwise(xAndY);

                Assert.assertTrue(Arrays.equals(xAndY, expected));
                System.out.println("///////////");
            }
        }
    }

    @Test
    public void testInitAndToString() {
        Board board = new Board(Board.FIRST_PLAYER);
        String asString = board.toString();
        String expected = "0  00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 \n" +
                "7: -1 -1 -1 -1 00 00 00 00 00 00 00 -1 -1 -1 -1 \n" +
                "6: -1 -1 -1 -1 -1 -1 00 -1 -1 -1 -1 -1 -1 \n" +
                "5: 01 01 -1 -1 -1 -1 -1 -1 -1 02 02 \n" +
                "4: 01 01 01 -1 -1 -1 02 02 02 \n" +
                "3: 01 01 -1 -1 -1 02 02 \n" +
                "2: 01 -1 -1 -1 02 \n" +
                "1: -1 -1 -1 \n" +
                "0: -1 \n";
        Assert.assertEquals(expected, asString);
    }


    @Test
    public void testValidMoves() {
        Board board = new Board(Board.FIRST_PLAYER);
    }
}