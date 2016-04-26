/**
 * Created by m on 4/26/16.
 */
public class L {

    final static String blackColor = (char) 27 + "[30m";

    final static String blueColor = (char) 27 + "[34m";
    final static String redColor = (char) 27 + "[31m";
    final static String greenColor = (char) 27 + "[32m";

    static void d(int player, String msg) {
        String color = blackColor;
        switch (player) {
            case Board.FIRST_PLAYER:
                color = redColor;
                break;
            case Board.SECOND_PLAYER:
                color = greenColor;
                break;
            case Board.THIRD_PLAYER:
                color = blueColor;
                break;
        }

        System.out.println(color + "(Player " + player + ") " + msg + blackColor);
    }
}
