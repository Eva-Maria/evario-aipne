package evario.game;

import lenz.htw.aipne.Move;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        Board board = new Board();
        String asString = board.toString();
        String expected = "   00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 \n" +
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
    public void testValidMovesForFirstPlayerWithEvenX() {
        int[][] fields = Board.createEmptyFields();
        Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{6, 6}, fields);

        Move move = new Move(6, 6, 7, 6);
        boolean isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertTrue(isValid);

        move = new Move(6, 6, 5, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertTrue(isValid);


        move = new Move(4, 4, 5, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player is not present from field", isValid);

        move = new Move(6, 6, 6, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player is already on this field & no move at all", isValid);

        move = new Move(6, 6, 6, 7);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player cannot move backwards from even x", isValid);

        move = new Move(6, 6, 6, 5);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player cannot move straight ahead from even x", isValid);

        move = new Move(6, 6, 4, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player cannot move more steps as one", isValid);
    }

    @Test
    public void testValidMovesForFirstPlayerWithOddX() {
        int[][] fields = Board.createEmptyFields();
        Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{5, 5}, fields);

        Move move = new Move(5, 5, 4, 4);
        boolean isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertTrue(isValid);


        move = new Move(4, 4, 5, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player is not present from field", isValid);

        move = new Move(5, 5, 5, 5);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player is already on this field & no move at all", isValid);

        move = new Move(5, 5, 4, 5);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player cannot move to side from odd x", isValid);

        move = new Move(5, 5, 6, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player can not move backwards", isValid);

        move = new Move(5, 5, 3, 3);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player can not move more steps as one", isValid);

        Board.setPlayerOnField(Board.SECOND_PLAYER, new int[]{4, 4}, fields);
        move = new Move(5, 5, 4, 4);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Player can not beat other players on odd fields", isValid);
    }

    @Test
    public void testValidMovesForFirstPlayerAtFieldLimits() {
        int[][] fields = Board.createEmptyFields();
        Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{0, 0}, fields);

        Move move = new Move(0, 0, -1, -1);
        boolean isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Cannot leave board", isValid);


        fields = Board.createEmptyFields();
        Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{0, 7}, fields);

        move = new Move(0, 7, -1, 6);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Cannot leave board", isValid);


        fields = Board.createEmptyFields();
        Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{14, 7}, fields);

        move = new Move(14, 7, 15, 7);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertFalse("Cannot leave board", isValid);

        move = new Move(14, 7, 13, 7);
        isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testAllValidMovesForFirstPlayer() {
        int[][] fields = Board.createEmptyFields();
        int totalValidMoves = 0;

        for (int fromY = fields.length - 1; fromY >= 0; fromY--) {
            int rowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < rowLength; fromX++) {
                int validMovesCount = subTestValidMove(fields, fromX, fromY);
                totalValidMoves += validMovesCount;
//                System.out.println(fromX + ", " + fromY + " => " + validMovesCount);

                if (fromX == 0 && fromY == 0) {
                    Assert.assertEquals(0, validMovesCount);
                } else if (fromX == 0) { // left edge
                    Assert.assertEquals(1, validMovesCount);
                } else if (fromX == rowLength - 1) { // right edge
                    Assert.assertEquals(1, validMovesCount);
                } else if (fromX % 2 == 0) {
                    Assert.assertEquals(2, validMovesCount);
                } else {
                    Assert.assertEquals(1, validMovesCount);
                }
            }
        }
        System.out.println("Total valid moves counted: " + totalValidMoves);
    }

    private int subTestValidMove(int[][] fields, int fromX, int fromY) {
        ArrayList<Boolean> validFields = new ArrayList<>();
        for (int toY = -5; toY < 20; toY++) {

            for (int toX = -5; toX < 20; toX++) {
                Board.setPlayerOnField(Board.FIRST_PLAYER, new int[]{fromX, fromY}, fields);

                Move move = new Move(fromX, fromY, toX, toY);
                boolean isValid = Board.isMoveValid(move, Board.FIRST_PLAYER, fields);
                if (isValid) {
                    validFields.add(isValid);
                }
                Board.setPlayerOnField(Board.EMPTY_FIELD, new int[]{fromX, fromY}, fields);
            }
        }
        return validFields.size();
    }

    @Test
    public void testTranslateMoveForPlayer() {
        Move move = new Move(2, 4, 3, 5);
        Move actualMove = Board.translateMoveForPlayerReverse(move, Board.SECOND_PLAYER);
        Move expectedMove = new Move(6, 6, 7, 6);
        Assert.assertEquals(expectedMove.toString(), actualMove.toString());

        move = new Move(6, 4, 5, 4);
        actualMove = Board.translateMoveForPlayerReverse(move, Board.THIRD_PLAYER);
        expectedMove = new Move(6, 6, 7, 6);
        Assert.assertEquals(expectedMove.toString(), actualMove.toString());

        move = new Move(0, 2, 1, 2);
        actualMove = Board.translateMoveForPlayerReverse(move, Board.SECOND_PLAYER);
        expectedMove = new Move(4, 7, 3, 7);
        Assert.assertEquals(expectedMove.toString(), actualMove.toString());

    }

    @Test
    public void testTranslateBoardForPlayers() {
        Board board = new Board();

        Board.initialTranslateBoardForPlayer(board, Board.FIRST_PLAYER);
        Assert.assertEquals(board.getFields()[6][6], Board.FIRST_PLAYER);

        board = new Board();
        Board.initialTranslateBoardForPlayer(board, Board.SECOND_PLAYER);
        Assert.assertEquals(board.getFields()[6][6], Board.SECOND_PLAYER);

        board = new Board();
        Board.initialTranslateBoardForPlayer(board, Board.THIRD_PLAYER);
        System.out.println(board);
        Assert.assertEquals(board.getFields()[6][6], Board.THIRD_PLAYER);
    }

    @Test
    public void testCloneBoards() {
        Board board = new Board();
        Board cloned = Board.clone(board);

        Assert.assertNotEquals(board, cloned);

        board.getFields()[0][0] = Board.THIRD_PLAYER;

        Assert.assertFalse(Arrays.deepEquals(board.getFields(), cloned.getFields()));
    }
}