import lenz.htw.aipne.Move;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by m on 4/26/16.
 */
public class ClientTest {

    private FakeNetworkClientWrapper nc;
    private Client client;

    @Before
    public void setUp() throws Exception {
        nc = spy(new FakeNetworkClientWrapper());
        client = spy(new Client());
        doReturn(nc).when(client).getNetworkClient();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testClientMoveOrderHappyPath() throws Exception {
        Move[] movesPlayerOne = {
                new Move(4, 7, 3, 7)
        };
        Move[] movesPlayerTwo = {
                new Move(0, 5, 1, 6)
        };
        Move[] movesPlayerThree = {
                new Move(6, 4, 5, 4)
        };

        nc.setMyPlayerNumber(Board.FIRST_PLAYER);
        nc.setMovesAndPlayers(movesPlayerOne, movesPlayerTwo, movesPlayerThree);
        client.run();
        assertEquals("moves left:0,0,0; receive called:4; send called:1", nc.getStats());
    }

    @Test
    public void testClientMoveOrderHappyPathWithAI() throws Exception {
        Move[] movesPlayerOne = {};
        Move[] movesPlayerTwo = {
                new Move(0, 5, 1, 6)
        };
        Move[] movesPlayerThree = {
                new Move(6, 4, 5, 4)
        };

        nc.setMyPlayerNumber(Board.FIRST_PLAYER);
        nc.setMovesAndPlayers(movesPlayerOne, movesPlayerTwo, movesPlayerThree);
        try {
            client.run();
        } catch (IllegalStateException e) {
            assertEquals("Move not allowed for player 0: 4,7 -> 3,7", e.getMessage());
        }
        assertEquals("moves left:0,0,0; receive called:6; send called:2", nc.getStats());
    }

//    @Test
//    public void testClientMoveWithKick() throws Exception {
//        Move[] movesPlayerOne = {};
//        Move[] movesPlayerTwo = {
//                new Move(0, 5, 1, 6)
//        };
//        Move[] movesPlayerThree = {};
//
//        nc.setMyPlayerNumber(Board.FIRST_PLAYER);
//        nc.setMovesAndPlayers(movesPlayerOne, movesPlayerTwo, movesPlayerThree);
//        client.run();
//        assertEquals("moves left:0,0,0; receive called:4; send called:1", nc.getStats());
//    }

    static class FakeNetworkClientWrapper extends NetworkClientWrapper {

        private ArrayList<Move> movesPlayerOne;
        private ArrayList<Move> movesPlayerTwo;
        private ArrayList<Move> movesPlayerThree;

        private int myPlayerNumber;
        private int countSendCall;
        private int countReceiveCall;

        @Override
        int getMyPlayerNumber() {
            return myPlayerNumber;
        }

        @Override
        Move receiveMove(int currentPlayer) {
            countReceiveCall++;

            Move move = getNextMove(currentPlayer, true);

            if (move != null) {
                System.out.println(currentPlayer + " - will receive: " + move);
            } else {
                System.out.println(currentPlayer + " - will receive null");
            }

            return move;
        }

        @Override
        void sendMove(Move move, int currentPlayer) {
            countSendCall++;
            Move expected = getNextMove(currentPlayer, false);

            if (currentPlayer != myPlayerNumber) {
                if (expected != null) {
                    assertEquals(expected.toString(), move.toString());
                }
            } else {
                putNextMove(currentPlayer, move);
            }

            System.out.println(currentPlayer + " - is moving: " + move);
        }

        private Move getNextMove(int player, boolean remove) {
            ArrayList<Move> currentMoves = getMovesForPlayer(player);

            if (currentMoves.size() == 0) {
                return null;
            }

            Move currentMove = currentMoves.get(0);
            if (remove) {
                currentMoves.remove(0);
            }
            return currentMove;
        }

        private void putNextMove(int player, Move move) {
            ArrayList<Move> moves = getMovesForPlayer(player);
            moves.add(0, move);
        }

        private ArrayList<Move> getMovesForPlayer(int player) {
            ArrayList<Move> currentMoves = null;
            switch (player) {
                case Board.FIRST_PLAYER:
                    currentMoves = movesPlayerOne;
                    break;
                case Board.SECOND_PLAYER:
                    currentMoves = movesPlayerTwo;
                    break;
                case Board.THIRD_PLAYER:
                    currentMoves = movesPlayerThree;
                    break;
            }
            return currentMoves;
        }

        void setMyPlayerNumber(int myPlayerNumber) {
            this.myPlayerNumber = myPlayerNumber;
        }

        void setMovesAndPlayers(Move[] one, Move[] two, Move[] three) {
            this.movesPlayerOne = new ArrayList<>(Arrays.asList(one));
            this.movesPlayerTwo = new ArrayList<>(Arrays.asList(two));
            this.movesPlayerThree = new ArrayList<>(Arrays.asList(three));

            this.countSendCall = 0;
            this.countReceiveCall = 0;
        }

        String getStats() {
            StringBuilder builder = new StringBuilder();
            builder.append("moves left:")
                    .append(movesPlayerOne.size()).append(",")
                    .append(movesPlayerTwo.size()).append(",")
                    .append(movesPlayerThree.size())
                    .append("; receive called:")
                    .append(countReceiveCall)
                    .append("; send called:")
                    .append(countSendCall);
            return builder.toString();
        }
    }
}
