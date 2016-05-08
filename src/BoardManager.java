import lenz.htw.aipne.Move;

/**
 * Created by m on 5/8/16.
 */
public class BoardManager {

    public static final String EXCEPTION_NO_PLAYER = "No player found";
    Board[] boards;
    int myPlayerNumber;

    public BoardManager(int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        boards = new Board[3];

        boards[Board.FIRST_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.FIRST_PLAYER], Board.FIRST_PLAYER);

        boards[Board.SECOND_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.SECOND_PLAYER], Board.SECOND_PLAYER);

        boards[Board.THIRD_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.THIRD_PLAYER], Board.THIRD_PLAYER);
    }

    public int updateBoard(Move move) {
        int currentPlayer = getMasterBoard().determinePlayer(move);
        if (currentPlayer == Board.EMPTY_FIELD) {
            throw new IllegalStateException(EXCEPTION_NO_PLAYER);
        }
        updateAllBoards(move, currentPlayer);
        return currentPlayer;
    }

    private void updateAllBoards(Move move, int currentPlayer) {
        int player = currentPlayer;
        int playersLeft = 3;

        //TODO: throw away kicked users board?
        while (playersLeft > 0) {
            playersLeft--;

            Move translatedMove = Board.translateMoveForPlayerReverse(move, player);
            boards[player].makeMove(translatedMove, currentPlayer);

            player = ++player % 3;
        }
    }

    Board getMasterBoard() {
        return boards[Board.FIRST_PLAYER];
    }

    Board getMyBoard() {
        return boards[myPlayerNumber];
    }
}
