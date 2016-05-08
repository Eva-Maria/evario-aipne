import lenz.htw.aipne.Move;
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

        // 1. player
        Move move = new Move(6, 6, 7, 6);
        int currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2. player
        move = new Move(0, 2, 1, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(4, 2, 3, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());
    }

    @Test
    public void updateBoardSecondPlayerKick() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);

        Move move = new Move(6, 6, 7, 6);
        int currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2.player / invalid
        move = new Move(0, 2, 2, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(4, 2, 3, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(3, 2, 2, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());
    }

    @Test
    public void updateBoardSecondAndThirdPlayerKick() throws Exception {
        BoardManager bm = new BoardManager(Board.FIRST_PLAYER);

        Move move = new Move(6, 6, 7, 6);
        int currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 2. player / invalid
        move = new Move(0, 2, 2, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.SECOND_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player
        move = new Move(4, 2, 3, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(7, 6, 6, 5);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 3. player / invalid
        move = new Move(3, 2, 3, 2);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.THIRD_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player
        move = new Move(6, 5, 5, 5);
        currentPlayer = bm.updateBoard(move);
        assertEquals(Board.FIRST_PLAYER, currentPlayer);
        System.out.println(bm.getMasterBoard());

        // 1. player / invalid
        move = new Move(6, 5, 5, 5);
        try {
            bm.updateBoard(move);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(BoardManager.EXCEPTION_NO_PLAYER, e.getMessage());
        }

        System.out.println(bm.getMasterBoard());
    }

}