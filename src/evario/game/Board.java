package evario.game;

import lenz.htw.aipne.Move;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eve on 4/19/16.
 */
public class Board {

    static final String PRINT_ORIGIN = "   ";
    static final String PRINT_ROW_DELIMITER = ": ";
    static final String PRINT_SPACE = " ";
    static final String PRINT_FIELD_FORMAT = "%02d";
    static final String PRINT_NEW_LINE = "\n";

    //TODO: migrate to init with 0 and players + 1?
    public static final int EMPTY_FIELD = -1;
    public static final int FIRST_PLAYER = 0;
    public static final int SECOND_PLAYER = 1;
    public static final int THIRD_PLAYER = 2;

    private final int[][] fields; //[y][x]
    public Move lastMove;

    public int playerStones = 8;

    public Board() {
        int[][] fields = createEmptyFields();
        initPlayer(fields, FIRST_PLAYER);
        initPlayer(fields, SECOND_PLAYER);
        initPlayer(fields, THIRD_PLAYER);

        this.fields = fields;
    }

    public void makeMove(final Move move, final int player) {
        fields[move.fromY][move.fromX] = EMPTY_FIELD;
        fields[move.toY][move.toX] = player;

        lastMove = move;
    }

    public int determinePlayerFrom(final Move move) {
        return fields[move.fromY][move.fromX];
    }

    public int determinePlayerTo(final Move move) {
        return fields[move.toY][move.toX];
    }

    public int[][] getFields() {
        return fields;
    }

    public static boolean isMoveValid(final Move move, final int player, final int[][] fields) {
        if (move.fromY < 0 || move.fromY >= 8) {
            return false;
        }
        if (move.fromX < 0 || move.fromX >= move.fromY * 2 + 1) {
            return false;
        }
        if (move.toY < 0 || move.toY >= 8) {
            return false;
        }
        if (move.toX < 0 || move.toX >= move.toY * 2 + 1) {
            return false;
        }


        if (fields[move.fromY][move.fromX] != player) {
            return false;
        }

        if (fields[move.toY][move.toX] == player) {
            return false;
        }

        if (move.fromX % 2 == 0) {
            // same y && x ether left or right
            if (!(move.toY == move.fromY && (move.fromX + 1 == move.toX || move.fromX - 1 == move.toX))) {
                return false;
            }
        } else {
            // toY has to be one less && toX has to be one less
            if (!(move.toY + 1 == move.fromY && move.toX + 1 == move.fromX)) {
                return false;
            }
            if (fields[move.toY][move.toX] != EMPTY_FIELD) {
                return false;
            }
        }

        return true;
    }

    static int[] translateFieldsClockwise(final int[] xAndY) {
        final int newY = (int) Math.ceil((14 - 2 * xAndY[1] + xAndY[0]) / 2f);
        final int newX = (int) Math.ceil((14 - xAndY[0]) / 2f) * 2 - 2 * xAndY[1] + xAndY[0];

        final int[] newXAndY = new int[]{newX, newY};
        return newXAndY;
    }

    static int[] translateFieldsCounterClockwise(final int[] xAndY) {
        final int newX = 2 * xAndY[1] - xAndY[0];
        final int newY = (int) Math.ceil((14 - xAndY[0]) / 2f);

        final int[] newXAndY = {newX, newY};

        return newXAndY;
    }

    static boolean isOnField(final int[] newXAndY) {
        if (newXAndY[1] < 0 || newXAndY[1] >= 8) {
            return false;
        }
        if (newXAndY[0] < 0 || newXAndY[0] >= newXAndY[1] * 2 + 1) {
            return false;
        }

        return true;
    }

    static Move translateMoveForPlayerReverse(final Move move, final int player) {
        final int[] fromXAndY = reverseTranslateFieldCoordinatesForPlayer(new int[]{move.fromX, move.fromY}, player);
        final int[] toXAndY = reverseTranslateFieldCoordinatesForPlayer(new int[]{move.toX, move.toY}, player);
        return new Move(fromXAndY[0], fromXAndY[1], toXAndY[0], toXAndY[1]);
    }

    public static Move translateMoveForPlayer(final Move move, final int player) {
        final int[] fromXAndY = initialTranslateFieldCoordinatesForPlayer(new int[]{move.fromX, move.fromY}, player);
        final int[] toXAndY = initialTranslateFieldCoordinatesForPlayer(new int[]{move.toX, move.toY}, player);
        return new Move(fromXAndY[0], fromXAndY[1], toXAndY[0], toXAndY[1]);
    }

    static int[] reverseTranslateFieldCoordinatesForPlayer(int[] xAndY, final int player) {
        if (player == THIRD_PLAYER) {
            return translateFieldsClockwise(xAndY);
        }
        if (player == SECOND_PLAYER) {
            return translateFieldsCounterClockwise(xAndY);
        }
        return xAndY;
    }

    static int[] initialTranslateFieldCoordinatesForPlayer(int[] xAndY, final int player) {
        if (player == SECOND_PLAYER) {
            return translateFieldsClockwise(xAndY);
        }
        if (player == THIRD_PLAYER) {
            return translateFieldsCounterClockwise(xAndY);
        }
        return xAndY;
    }

    static void initialTranslateBoardForPlayer(final Board board, final int player) {
        final Board cloned = Board.clone(board);
        for (int y = 7; y >= 0; y--) {
            int rowLength = y * 2 + 1;

            for (int x = 0; x < rowLength; x++) {
                int[] xAndY = new int[]{x, y};
                xAndY = initialTranslateFieldCoordinatesForPlayer(xAndY, player);
                board.fields[y][x] = cloned.fields[xAndY[1]][xAndY[0]];
            }
        }
    }

    static Board clone(final Board board) {
        final Board cloned = new Board();

        for (int y = 0; y < board.fields.length; y++) {
            cloned.fields[y] = Arrays.copyOf(board.fields[y], board.fields[y].length);
        }

        if (board.lastMove != null) {
            cloned.lastMove = new Move(board.lastMove.fromX, board.lastMove.fromY, board.lastMove.toX, board.lastMove.toY);
        }
        cloned.playerStones = board.playerStones;

        return cloned;
    }

    static int[][] createEmptyFields() {
        final int[][] fields = new int[8][];
        for (int y = fields.length - 1; y >= 0; y--) {
            final int rowLength = y * 2 + 1;
            fields[y] = new int[rowLength];

            for (int x = 0; x < rowLength; x++) {
                setPlayerOnField(EMPTY_FIELD, new int[]{x, y}, fields);
            }
        }
        return fields;
    }

    static void initPlayer(final int[][] fields, final int player) {
        ArrayList<int[]> initialFields = new ArrayList<>();
        initialFields.add(new int[]{4, 7});
        initialFields.add(new int[]{5, 7});
        initialFields.add(new int[]{6, 7});
        initialFields.add(new int[]{7, 7});
        initialFields.add(new int[]{8, 7});
        initialFields.add(new int[]{9, 7});
        initialFields.add(new int[]{10, 7});
        initialFields.add(new int[]{6, 6});

        for (int[] initialField : initialFields) {
            final int[] xAndY = initialTranslateFieldCoordinatesForPlayer(initialField, player);
            setPlayerOnField(player, xAndY, fields);
        }
    }

    static boolean setPlayerOnField(final int player, final int[] xAndY, final int[][] fields) {
        if (isOnField(xAndY)) {
            fields[xAndY[1]][xAndY[0]] = player;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder header = new StringBuilder(PRINT_ORIGIN);

        final int yMaxLength = fields.length - 1;
        for (int y = yMaxLength; y >= 0; y--) {
            int[] row = fields[y];
            builder.append(y + PRINT_ROW_DELIMITER);
            for (int x = 0; x < row.length; x++) {
                int field = row[x];
                if (y == yMaxLength) {
                    String xFormatted = String.format(PRINT_FIELD_FORMAT, x);
                    header.append(xFormatted).append(PRINT_SPACE);
                }
                String fieldFormatted = String.format(PRINT_FIELD_FORMAT, field);
                builder.append(fieldFormatted);
                if (lastMove != null && lastMove.toX == x && lastMove.toY == y) {
                    builder.append("*");
                } else if (lastMove != null && lastMove.fromX == x && lastMove.fromY == y) {
                    builder.append("!");
                } else {
                    builder.append(PRINT_SPACE);
                }
            }
            builder.append(PRINT_NEW_LINE);
        }
        return header.toString() + PRINT_NEW_LINE + builder.toString();
    }


}
