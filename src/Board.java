import lenz.htw.aipne.Move;

import java.util.ArrayList;

/**
 * Created by eve on 4/19/16.
 */
public class Board {

    static final String PRINT_ORIGIN = "  ";
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
    private final int myPlayerNumber;
    private Move lastMove;

    public Board(int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        int[][] fields = createEmptyFields();
        initPlayer(fields, FIRST_PLAYER);
        initPlayer(fields, SECOND_PLAYER);
        initPlayer(fields, THIRD_PLAYER);

        this.fields = fields;
        L.d(myPlayerNumber, "Board initialized");
    }

    void setField(int[] xAndY, int player) {
        xAndY = getTranslatedField(xAndY, player);
        fields[xAndY[1]][xAndY[0]] = player;
    }

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

//        int[] fromYAndXTranslated = getTranslatedField(move.fromY, move.fromX, player);
//        int[] toYAndXTranslated = getTranslatedField(move.toY, move.toX, player);
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


    static boolean isMoveValid(Move move, int player, int[][] board) {
        return false;
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

    private static boolean isOnField(int[] newXAndY) {
        if (newXAndY[1] < 0 || newXAndY[1] > 8) {
            return false;
        }
        if (newXAndY[0] < 0 || newXAndY[0] > newXAndY[1] * 2 + 1) {
            return false;
        }

        return true;
    }

    static int[] getTranslatedField(int[] xAndY, int player) {
        if (player == SECOND_PLAYER) {
            xAndY = translateFieldsCounterClockwise(xAndY);
        }
        if (player == SECOND_PLAYER || player == THIRD_PLAYER) {
            xAndY = translateFieldsCounterClockwise(xAndY);
        }
        return xAndY;
    }

    static int[][] createEmptyFields() {
        int[][] fields = new int[8][];
        for (int y = fields.length - 1; y >= 0; y--) {
            int rowLength = y * 2 + 1;
            fields[y] = new int[rowLength];

            for (int x = 0; x < rowLength; x++) {
                fields[y][x] = EMPTY_FIELD;
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
            int[] xAndY = getTranslatedField(initialField, player);
            fields[xAndY[1]][xAndY[0]] = player;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder header = new StringBuilder(myPlayerNumber + PRINT_ORIGIN);

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
