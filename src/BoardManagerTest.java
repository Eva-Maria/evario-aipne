import lenz.htw.aipne.Move;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by m on 5/8/16.
 */
public class BoardManagerTest {
    @Test
    public void updateBoardHappyPath() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);

        // 1. player
        Move move = new Move(6, 6, 7, 6);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2. player
        move = new Move(0, 2, 1, 2);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(4, 2, 3, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        Assert.assertArrayEquals(new boolean[]{true, true, true}, bm.isStillPlaying);
    }

    @Test
    public void updateBoardSecondPlayerKick() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);

        Move move = new Move(6, 6, 7, 6);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2.player / invalid
        move = new Move(0, 2, 2, 2);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());
        Assert.assertArrayEquals(new boolean[]{true, false, true}, bm.isStillPlaying);

        // 3. player
        move = new Move(4, 2, 3, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(3, 2, 2, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());
    }

    @Test
    public void updateBoardSecondAndThirdPlayerKick() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);

        Move move = new Move(6, 6, 7, 6);
        bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2. player / invalid
        move = new Move(0, 2, 2, 2);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());
        Assert.assertArrayEquals(new boolean[]{true, false, true}, bm.isStillPlaying);

        // 3. player
        move = new Move(4, 2, 3, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player / invalid
        move = new Move(3, 2, 3, 2);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());
        Assert.assertArrayEquals(new boolean[]{true, false, false}, bm.isStillPlaying);

        // 1. player
        move = new Move(6, 5, 5, 5);
        bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, bm.currentPlayer);
        System.out.println(bm.getMasterBoard());
        Assert.assertArrayEquals(new boolean[]{true, false, false}, bm.isStillPlaying);

        // 1. player / invalid
        move = new Move(6, 5, 5, 5);
        try {
            bm.updateBoard(move);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(BoardManager.EXCPETION_NO_PLAYER_LEFT, e.getMessage());
        }

        System.out.println(bm.getMasterBoard());
        Assert.assertArrayEquals(new boolean[]{false, false, false}, bm.isStillPlaying);
    }

}