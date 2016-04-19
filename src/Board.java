/**
 * Created by eve on 4/19/16.
 */
public class Board {

    final int INVALID_FIELD = 0;
    final int EMPTY_FIELD = -1;

    final int[][] fields = new int[8][15];

    public Board() {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder header = new StringBuilder("   ");

        for (int i = fields.length - 1; i >= 0; i--) {
            int[] row = fields[i];
            builder.append(i + ": ");
            for (int j = 0; j < row.length; j++) {
                if (i == 0) {
                    String x = String.format("%02d", j);
                    header.append(x + " ");
                }
                String field = String.format("%02d", row[j]);
                builder.append(field + " ");
            }
            builder.append("\n");
        }
        return header.toString() + "\n" + builder.toString();
    }
}
