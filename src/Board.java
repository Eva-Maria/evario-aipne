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
    static final int EMPTY_FIELD = -1;
    static final int FIRST_PLAYER = 0;
    static final int SECOND_PLAYER = 1;
    static final int THIRD_PLAYER = 2;

    private final int[][] fields; //[y][x]
    private Move lastMove;

    public Board() {
        int[][] fields = createEmptyFields();
        initPlayer(fields, FIRST_PLAYER);
        initPlayer(fields, SECOND_PLAYER);
        initPlayer(fields, THIRD_PLAYER);

        this.fields = fields;
    }

//    void setField(int[] xAndY, int player) {
//        xAndY = initialTranslateFieldCoordinatesForPlayer(xAndY, player);
//        fields[xAndY[1]][xAndY[0]] = player;
//    }

    public boolean makeMoveIfValid(Move move, int player) {
//        if(!isOnField(move.fromY, move.fromX)) {
//            return false;
//        }
//        if(!isOnField(move.toY, move.toX)) {
//            return false;
//        }

        if (fields[move.fromY][move.fromX] != player) {
            return false;
        }

//        int[] fromYAndXTranslated = initialTranslateFieldCoordinatesForPlayer(move.fromY, move.fromX, player);
//        int[] toYAndXTranslated = initialTranslateFieldCoordinatesForPlayer(move.toY, move.toX, player);
//        // if x even, move left or right
//        if (!(fromYAndXTranslated[1] % 2 == 0 && toYAndXTranslated[1] % 2 != 0 && fromYAndXTranslated[0] == toYAndXTranslated[0])) {
//            return false;
//        }
//
//        if (!(fromYAndXTranslated[1] % 2 != 0 && toYAndXTranslated[1] % 2 == 0 && fromYAndXTranslated[0] == toYAndXTranslated[0] - 1)) {
//            return false;
//        }

        //TODO: check for move distance
        fields[move.fromY][move.fromX] = EMPTY_FIELD;
        fields[move.toY][move.toX] = player;

        lastMove = move;
        return true;
    }

    public int[][] getFields() {
        // Only for tests
        return fields;
    }

    static boolean isMoveValid(Move move, int player, int[][] fields) {
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
        }

        return true;
    }

    static int[] translateFieldsCounterClockwise(int[] xAndY) {
        int newX = 2 * xAndY[1] - xAndY[0];
        int newY = (int) Math.ceil((14 - xAndY[0]) / 2f);

        System.out.println(xAndY[0] + "," + xAndY[1] + " -> " + newY + "," + newX);

        int[] newXAndY = {newX, newY};
        if (!isOnField(newXAndY)) {
            throw new IllegalStateException("New coordinates are invalid");
        }

        return newXAndY;
    }

    static boolean isOnField(int[] newXAndY) {
        if (newXAndY[1] < 0 || newXAndY[1] >= 8) {
            return false;
        }
        if (newXAndY[0] < 0 || newXAndY[0] >= newXAndY[1] * 2 + 1) {
            return false;
        }

        return true;
    }

    static int[] initialTranslateFieldCoordinatesForPlayer(int[] xAndY, int player) {
        if (player == SECOND_PLAYER) {
            xAndY = translateFieldsCounterClockwise(xAndY);
        }
        if (player == SECOND_PLAYER || player == THIRD_PLAYER) {
            xAndY = translateFieldsCounterClockwise(xAndY);
        }
        return xAndY;
    }

    static void initialTranslateBoardForPlayer(Board board, int player) {
        Board cloned = Board.clone(board);
        for (int y = 7; y >= 0; y--) {
            int rowLength = y * 2 + 1;

            for (int x = 0; x < rowLength; x++) {
                int[] xAndY = new int[]{x, y};
                xAndY = initialTranslateFieldCoordinatesForPlayer(xAndY, player);
                board.fields[y][x] = cloned.fields[xAndY[1]][xAndY[0]];
            }
        }
    }

    static Board clone(Board board) {
        Board cloned = new Board();

        for (int y = 0; y < board.fields.length; y++) {
            cloned.fields[y] = Arrays.copyOf(board.fields[y], board.fields[y].length);
        }

        if (board.lastMove != null) {
            cloned.lastMove = new Move(board.lastMove.fromX, board.lastMove.fromY, board.lastMove.toX, board.lastMove.toY);
        }

        return cloned;
    }

    static int[][] createEmptyFields() {
        int[][] fields = new int[8][];
        for (int y = fields.length - 1; y >= 0; y--) {
            int rowLength = y * 2 + 1;
            fields[y] = new int[rowLength];

            for (int x = 0; x < rowLength; x++) {
                setPlayerOnField(EMPTY_FIELD, new int[]{x, y}, fields);
            }
        }
        return fields;
    }

    static void initPlayer(int[][] fields, int player) {
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
            int[] xAndY = initialTranslateFieldCoordinatesForPlayer(initialField, player);
            setPlayerOnField(player, xAndY, fields);
        }
    }

    static boolean setPlayerOnField(int player, int[] xAndY, int[][] fields) {
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
