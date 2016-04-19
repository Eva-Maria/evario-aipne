/**
 * Created by eve on 4/19/16.
 */
public class Board {

    final int INVALID_FIELD = 0;
    final int EMPTY_FIELD = -1;

    final int[][] fields = new int[8][15];

    public Board() {
        for (int y = fields.length - 1; y >= 0; y--) {
            for (int x = 0; x < y * 2 + 1; x++) {
                fields[y][x] = EMPTY_FIELD;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder header = new StringBuilder("   ");

        for (int y = fields.length - 1; y >= 0; y--) {
            int[] row = fields[y];
            builder.append(y + ": ");
            for (int x = 0; x < row.length; x++) {
                if (y == 0) {
                    String xHeader = String.format("%02d", x);
                    header.append(xHeader + " ");
                }
                String field = String.format("%02d", row[x]);
                builder.append(field + " ");
            }
            builder.append("\n");
        }
        return header.toString() + "\n" + builder.toString();
    }
}
