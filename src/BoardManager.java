import lenz.htw.aipne.Move;

/**
 * Created by m on 5/8/16.
 */
public class BoardManager {

    Board[] boards;
    int currentPlayer;
    int myPlayerNumber;
    boolean[] isStillPlaying = {true, true, true};

    public BoardManager(int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        currentPlayer = Board.FIRST_PLAYER;

        boards = new Board[3];

        boards[Board.FIRST_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.FIRST_PLAYER], Board.FIRST_PLAYER);

        boards[Board.SECOND_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.SECOND_PLAYER], Board.SECOND_PLAYER);

        boards[Board.THIRD_PLAYER] = new Board();
        Board.initialTranslateBoardForPlayer(boards[Board.THIRD_PLAYER], Board.THIRD_PLAYER);
    }

    public void updateBoard(Move move) {

        updateAllBoards(move);

        int playersLeft = 3;
        do {
            currentPlayer = ++currentPlayer % 3;
            playersLeft--;
        } while (!isStillPlaying[currentPlayer] && playersLeft >= 0);


        System.out.println("player is now: " + currentPlayer);
    }

    private void updateAllBoards(Move move) {
        int tempCurrentPlayer = currentPlayer;
        int playersLeft = 3;

        while (playersLeft > 0) {
            playersLeft--;
            if (!isStillPlaying[tempCurrentPlayer]) {
                tempCurrentPlayer = ++tempCurrentPlayer % 3;
                continue;
            }

            Move translatedMove = Board.translateMoveForPlayerReverse(move, tempCurrentPlayer);
            if (currentPlayer == tempCurrentPlayer) {
                boolean isValid = boards[currentPlayer].makeMoveIfValid(translatedMove, currentPlayer);
                isStillPlaying[currentPlayer] = isValid;
                // TODO: if !isValid -> boards[tempCurrentPlayer] = null;
            } else {
                if (isStillPlaying[currentPlayer]) {
                    boards[tempCurrentPlayer].makeMove(translatedMove, currentPlayer);
                }
            }

            tempCurrentPlayer = ++tempCurrentPlayer % 3;
        }
    }

    Board getMasterBoard() {
        return boards[Board.FIRST_PLAYER];
    }
}
