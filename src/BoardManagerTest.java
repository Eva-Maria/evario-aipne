import lenz.htw.aipne.Move;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by m on 5/8/16.
 */
public class BoardManagerTest {
    @Test
    public void updateBoard() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);

        Move move = new Move(6, 6, 7, 6);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        move = new Move(0, 2, 1, 2);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);

        System.out.println(bm.getMasterBoard());

        move = new Move(4, 2, 3, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        move = new Move(7, 6, 6, 5);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        Assert.assertArrayEquals(new boolean[]{true, true, true}, bm.isStillPlaying);
    }

}