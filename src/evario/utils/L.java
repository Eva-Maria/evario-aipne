package evario.utils;

import evario.game.Board;

import java.util.HashMap;

/**
 * Created by m on 4/26/16.
 */
public class L {

    final static String blackColor = (char) 27 + "[30m";

    final static String blueColor = (char) 27 + "[34m";
    final static String redColor = (char) 27 + "[31m";
    final static String greenColor = (char) 27 + "[32m";

    private final static HashMap<Integer, String> players = new HashMap<>();

    public static void addPlayer(String name, int number) {
        players.put(number, name);
    }

    public static void d(int number, String msg) {
        String color = blackColor;
        switch (number) {
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

        System.out.println(color + "(" + players.get(number) + " / " + number + ") " + msg + blackColor);
    }

    public static void export(int number, String msg) {
        System.out.println(SystemOutInterceptor.EXPORT + players.get(number) + ": " + msg);
    }
}
