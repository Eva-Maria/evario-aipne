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

    public Board(int myPlayerNumber) {
        this.myPlayerNumber = myPlayerNumber;
        initEmptyFields();
        initPlayer(FIRST_PLAYER);
        initPlayer(SECOND_PLAYER);
        initPlayer(THIRD_PLAYER);
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
        int[] yAndX = new int[]{y, x};
        if (player == SECOND_PLAYER || player == THIRD_PLAYER) {
            yAndX = translateFieldsClockwise(yAndX[0], yAndX[1]);
        }
        if (player == SECOND_PLAYER) {
            yAndX = translateFieldsClockwise(yAndX[0], yAndX[1]);
        }

        fields[yAndX[0]][yAndX[1]] = player;
    }

    public void makeMove(Move move, int player) {

    }

    static int[] translateFieldsClockwise(int y, int x) {
        //TODO: always use int[] instead of y and x? - in place replacement?
        System.out.print(y + "," + x + " -> ");

        int newX = 2 * y - x;
        int newY = (int) Math.ceil((14 - x) / 2f);

        System.out.println(newY + "," + newX);

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
                builder.append(fieldFormatted).append(PRINT_SPACE);
            }
            builder.append(PRINT_NEW_LINE);
        }
        return header.toString() + PRINT_NEW_LINE + builder.toString();
    }
}
