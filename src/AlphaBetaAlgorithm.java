import lenz.htw.aipne.Move;

import java.util.ArrayList;

/**
 * Created by eve on 5/9/16.
 */
public class AlphaBetaAlgorithm implements Algorithm {

    private BoardManager bm;
    int depth;
    Move bestMove;
    private int myStoneWeight;
    private int myDistanceWeight;
    private int opponentStoneWeigh;
    private int opponentDistanceWeight;

    public AlphaBetaAlgorithm(BoardManager bm, int myStoneWeight, int myDistanceWeight, int opponentStoneWeigh, int opponentDistanceWeight) {
        this.bm = bm;
        this.myStoneWeight = myStoneWeight;
        this.myDistanceWeight = myDistanceWeight;
        this.opponentStoneWeigh = opponentStoneWeigh;
        this.opponentDistanceWeight = opponentDistanceWeight;
        depth = 5;
    }

    @Override
    public Move getNextMove(long timeStartMillis, int timeLimitMillis) {
        //reset best move
        bestMove = null;

        int rating = alphaBeta(depth, bm.myPlayerNumber, bm, Integer.MIN_VALUE, Integer.MAX_VALUE);
        long timeUsedMillis = System.currentTimeMillis() - timeStartMillis;

        L.d(bm.myPlayerNumber, "time: " + timeUsedMillis + " ms" + " Depth: " + depth);

        if (timeUsedMillis < 300) {
            depth++;
        }

        if (bestMove == null) {
            L.d(bm.myPlayerNumber, "Leider keine Zuege mehr verfuegbar");
            return null;
        }

        L.d(bm.myPlayerNumber, "Best move : " + bestMove + " Rating: " + rating);

        return bestMove;

    }


    public int alphaBeta(final int currentDepth, final int player, final BoardManager bm, final int alpha, final int beta) {
        ArrayList<Move> allPossibleMoves = getAllMoves(bm.getAllBoards()[player], player);

        // L.d(bm.myPlayerNumber, "Player: " + player);

        if (currentDepth == 0 || allPossibleMoves.size() == 0) {
            return rateBoard(bm, player);
        }
        //Todo: moves vorsortieren

        int bestMoveValue;
        int nextPlayer = (player + 1) % 3;
        bestMoveValue = alpha;

        for (Move m : allPossibleMoves) {

            BoardManager bmClone = BoardManager.clone(bm);
            bmClone.updateBoard(m);

            int value = -1 * alphaBeta(currentDepth - 1, nextPlayer, bmClone, -1 * beta, -1 * bestMoveValue);

            if (bestMoveValue < value) {
                bestMoveValue = value;
            }

            // Cutoff
            if (bestMoveValue >= beta) {
                break;
            }

            if (depth == currentDepth) {
                bestMove = m;
            }

        }

        return bestMoveValue;
    }

    private int rateBoard(BoardManager bm, int player) {
        Board[] allBoards = bm.getAllBoards();

        int rating = 0;

        for (int currentPlayer = 0; currentPlayer < allBoards.length; currentPlayer++) {
            Board board = allBoards[currentPlayer];
            if (currentPlayer == player) {
                rating += board.rate(currentPlayer, myStoneWeight, myDistanceWeight);
            } else {

                rating -= board.rate(currentPlayer, opponentStoneWeigh, opponentDistanceWeight);
            }
        }

        return rating;
    }

    private static ArrayList<Move> getAllMoves(Board board, int myPlayerNumber) {
        int[][] fields = board.getFields();
        ArrayList<Move> moves = new ArrayList<>();
        int playerStones = 0;

        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {
//                boolean isPlayerOnField = board.isPlayerOnField(fromX, fromY, myPlayerNumber);
//                if(!isPlayerOnField){
//                    continue;
//                }else{
//                    playerStones++;
//                }
                for (int toY = fromY; toY >= fromY - 1; toY--) {
                    for (int toX = fromX - 1; toX <= fromX + 1; toX++) {
                        Move move = new Move(fromX, fromY, toX, toY);

                        boolean isValid = Board.isMoveValid(move, myPlayerNumber, fields);
                        if (isValid) {
                            moves.add(Board.translateMoveForPlayer(move, myPlayerNumber));
//                            if(playerStones == Board.getPlayerStonesCount(myPlayerNumber)){
//                                return moves;
//                            }
                        }

                    }
                }

            }
        }

        // L.d(myPlayerNumber, "valid moved found: " + moves.size());
        return moves;
    }
}
