package evario.game;

import lenz.htw.aipne.Move;

/**
 * Created by m on 5/8/16.
 */
public class BoardManager {

    public static final String EXCEPTION_NO_PLAYER = "No player found";

    Board[] boards;

    public int myPlayerNumber;

    public BoardManager(final int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        boards = new Board[3];

        boards[Board.FIRST_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.FIRST_PLAYER], Board.FIRST_PLAYER);

        boards[Board.SECOND_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.SECOND_PLAYER], Board.SECOND_PLAYER);

        boards[Board.THIRD_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.THIRD_PLAYER], Board.THIRD_PLAYER);
    }

    public BoardManager(int myPlayerNumber, Board[] boards) {
        this.myPlayerNumber = myPlayerNumber;
        this.boards = boards;
    }

    public int updateBoard(Move move) {
        final Board masterBoard = getMasterBoard();

        final int currentPlayer = masterBoard.determinePlayerFrom(move);
        if (currentPlayer == Board.EMPTY_FIELD) {
            throw new IllegalStateException(EXCEPTION_NO_PLAYER);
        }

        int kickedPlayer = masterBoard.determinePlayerTo(move);
        if (kickedPlayer != Board.EMPTY_FIELD) {
            boards[kickedPlayer].playerStones--;
        }

        updateAllBoards(move, currentPlayer);
        return currentPlayer;
    }

    private void updateAllBoards(final Move move, final int currentPlayer) {
        int player = currentPlayer;
        int playersLeft = 3;

        //TODO: throw away kicked users board?
        while (playersLeft > 0) {
            playersLeft--;

            final Move translatedMove = Board.translateMoveForPlayerReverse(move, player);
            boards[player].makeMove(translatedMove, currentPlayer);

            player = ++player % 3;
        }
    }

    Board getMasterBoard() {
        return boards[Board.FIRST_PLAYER];
    }

    public Board getMyBoard() {
        return boards[myPlayerNumber];
    }

    public Board[] getAllBoards() {
        return boards;
    }

    public static BoardManager clone(final BoardManager bm) {
        Board[] clonedBoards = new Board[bm.boards.length];
        for (int i = 0; i < bm.boards.length; i++) {
            clonedBoards[i] = Board.clone(bm.boards[i]);
        }
        BoardManager clone = new BoardManager(bm.myPlayerNumber, clonedBoards);
        return clone;
    }
}
