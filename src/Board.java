import lenz.htw.aipne.Move;

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

    final int[][] fields = new int[8][]; //[y][x]

    private final int myPlayerNumber;
    private Move lastMove;

    public Board(int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        initEmptyFields();
        initPlayer(FIRST_PLAYER);
        initPlayer(SECOND_PLAYER);
        initPlayer(THIRD_PLAYER);

        L.d(myPlayerNumber, "Board initialized");
    }

    private void initPlayer(int player) {
        setField(7, 4, player);
        setField(7, 5, player);
        setField(7, 6, player);
        setField(7, 7, player);
        setField(7, 8, player);
        setField(7, 9, player);
        setField(7, 10, player);
        setField(6, 6, player);
    }

    void setField(int y, int x, int player) {
        int[] yAndX = getFieldCoordinates(y, x, player);
        fields[yAndX[0]][yAndX[1]] = player;
    }

    private int[] getFieldCoordinates(int y, int x, int player) {
        int[] yAndX = new int[]{y, x};
        if (player == SECOND_PLAYER) {
            yAndX = translateFieldsCounterClockwise(yAndX[0], yAndX[1]);
        }
        if (player == SECOND_PLAYER || player == THIRD_PLAYER) {
            yAndX = translateFieldsCounterClockwise(yAndX[0], yAndX[1]);
        }
        return yAndX;
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

//        int[] fromYAndXTranslated = getFieldCoordinates(move.fromY, move.fromX, player);
//        int[] toYAndXTranslated = getFieldCoordinates(move.toY, move.toX, player);
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

    static int[] translateFieldsCounterClockwise(int y, int x) {
        //TODO: always use int[] instead of y and x? - in place replacement?

        int newX = 2 * y - x;
        int newY = (int) Math.ceil((14 - x) / 2f);

        System.out.println(y + "," + x + " -> " + newY + "," + newX);

        if (!isOnField(newY, newX)) {
            throw new IllegalStateException("New coordinates are invalid");
        }
        return new int[]{newY, newX};
    }

    static boolean isOnField(int y, int x) {
        if (y < 0 || y > 8) {
            return false;
        }
        if (x < 0 || x > y * 2 + 1) {
            return false;
        }

        return true;
    }

    private void initEmptyFields() {
        for (int y = fields.length - 1; y >= 0; y--) {
            int rowLength = y * 2 + 1;
            fields[y] = new int[rowLength];

            for (int x = 0; x < rowLength; x++) {
                fields[y][x] = EMPTY_FIELD;
            }
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
