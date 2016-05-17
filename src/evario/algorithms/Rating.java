package evario.algorithms;

import evario.Config;
import evario.game.Board;
import lenz.htw.aipne.Move;

/**
 * Created by m on 5/17/16.
 */
public class Rating {
    public static int rateMove(final Move move, final int[][] fields, final int player) {
        int rating = 0;
        if (move.toY == 0 && move.toX == 0) {
            rating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_CORNER;
        }

        if (move.fromX % 2 == 0) {
            int rowLength = move.toY * 2 + 1;

            if (move.toX != 0 && move.toX < rowLength - 1) {
                int neighbour = fields[move.toY][move.toX];
                if (neighbour != Board.EMPTY_FIELD && neighbour != player) {
                    rating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_BEAT_OPPONENT;
                }
            }
        }

        if (move.toY < move.fromY && move.toY >= 4) {
            rating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_MOVE_FORWARD_TO_CENTER;
        }


        return rating;
    }

    public static int rateBoard(final Board oldBoard, final Board currentBoard, final int player) {
        int rating = 0;
        int[][] fields = currentBoard.getFields();
        int[][] oldFields = oldBoard.getFields();

        for (int y = fields.length - 1; y >= 0; y--) {
            int rowLength = y * 2 + 1;

            for (int x = 0; x < rowLength; x++) {
                int field = fields[y][x];
                int currentRating = 0;

                if (field == player) {
                    if (y == 0 && x == 0) {
                        currentRating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_CORNER;
                    }

                    if (x % 2 != 0) {
                        int neighbour = oldFields[y][x];
                        if (neighbour != Board.EMPTY_FIELD && neighbour != player) {
//                            L.d(player, "detected opponent, was previous beaten on " + x + "," + y);
//                            L.d(player, "\n" + oldBoard.toString());
//                            L.d(player, lastMove.toString());
//                            L.d(player, "\n" + toString());
                            currentRating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_BEAT_OPPONENT;
                        }
                    }
                    if (currentBoard.lastMove.toY < currentBoard.lastMove.fromY && currentBoard.lastMove.toY >= 4) {
                        currentRating += Config.ALPHA_BETA_ALGORITHM_WEIGHT_MOVE_FORWARD_TO_CENTER;
                    }

                }

                rating += currentRating;
            }
        }
        return rating;
    }
}
