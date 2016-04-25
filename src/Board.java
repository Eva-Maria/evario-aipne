/**
 * Created by eve on 4/19/16.
 */
public class Board {

    static final String PRINT_ORIGIN = "   ";
    static final String PRINT_ROW_DELIMITER = ": ";
    static final String PRINT_SPACE = " ";
    static final String PRINT_FIELD_FORMAT = "%02d";
    static final String PRINT_NEW_LINE = "\n";
    static final int EMPTY_FIELD = -1;

    static final int FIRST_PLAYER = 1;
    static final int SECOND_PLAYER = 2;
    static final int THIRD_PLAYER = 3;

    final int[][] fields = new int[8][]; //[y][x]

    public Board() {
        initEmptyFields();
        initPlayer(FIRST_PLAYER);
//        translateFieldsClockwise();
    }

    private void initPlayer(int player) {
        fields[7][4] = player;
        fields[7][5] = player;
        fields[7][6] = player;
        fields[7][7] = player;
        fields[7][8] = player;
        fields[7][9] = player;
        fields[7][10] = player;
        fields[6][6] = player;
    }

    static int[] translateFieldsClockwise(int y, int x) {
        if (y == 7 && x == 0) {
            return new int[]{7, 14};
        }
        if (y == 7 && x == 14) {
            return new int[]{0, 0};
        }
        if (y == 0 && x == 0) {
            return new int[]{7, 0};
        }
        throw new IllegalArgumentException("unknown: " + y + ", " + x);
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
                builder.append(fieldFormatted).append(PRINT_SPACE);
            }
            builder.append(PRINT_NEW_LINE);
        }
        return header.toString() + PRINT_NEW_LINE + builder.toString();
    }
}
