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

    public BoardManager(int myPlayerNumber, Board[] boards) {
        this.myPlayerNumber = myPlayerNumber;
        this.boards = boards;
    }

    public int updateBoard(Move move) {
        int currentPlayer = getMasterBoard().determinePlayerFrom(move);
        if (currentPlayer == Board.EMPTY_FIELD) {
            throw new IllegalStateException(EXCEPTION_NO_PLAYER);
        }

        int fieldOccupiedBy = getMasterBoard().determinePlayerTo(move);
        boolean isOccupiedByOpponent = fieldOccupiedBy != currentPlayer && fieldOccupiedBy != Board.EMPTY_FIELD;
        if (isOccupiedByOpponent) {
            kickOpponentStone(fieldOccupiedBy);
        }

        updateAllBoards(move, currentPlayer);
        return currentPlayer;
    }

    private void kickOpponentStone(int fieldOccupiedBy) {
        switch (fieldOccupiedBy) {
            case Board.FIRST_PLAYER:
                Board.firstPlayerStones--;
                break;
            case Board.SECOND_PLAYER:
                Board.secondPlayerStones--;
                break;
            case Board.THIRD_PLAYER:
                Board.thirdPlayerStones--;
            default:
                break;
        }
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

    Board[] getAllBoards() {
        return boards;
    }

    public static BoardManager clone(BoardManager bm) {
        Board[] clonedBoards = new Board[bm.boards.length];
        for (int i = 0; i < bm.boards.length; i++) {
            clonedBoards[i] = Board.clone(bm.boards[i]);
        }
        BoardManager clone = new BoardManager(bm.myPlayerNumber, clonedBoards);
        return clone;
    }
}
