import org.junit.After;
import org.junit.Before;

import static org.mockito.Mockito.spy;

/**
 * Created by m on 4/26/16.
 */
public class ClientTest {

    //    private FakeNetworkClientWrapper nc;
    private Client client;

    @Before
    public void setUp() throws Exception {
//        nc = spy(new FakeNetworkClientWrapper());
        client = spy(new Client());
//        doReturn(nc).when(client).getNetworkClient();
    }

    @After
    public void tearDown() throws Exception {

    }

//    static class FakeNetworkClientWrapper extends NetworkClientWrapper {
//
//        private ArrayList<Move> movesPlayerOne;
//        private ArrayList<Move> movesPlayerTwo;
//        private ArrayList<Move> movesPlayerThree;
//
//        private int myPlayerNumber;
//        private int countSendCall;
//        private int countReceiveCall;
//
//        @Override
//        int getMyPlayerNumber() {
//            return myPlayerNumber;
//        }
//
//        @Override
//        Move receiveMove(int currentPlayer) {
//            countReceiveCall++;
//            return null;
//        }
//
//        @Override
//        void sendMove(Move move, int currentPlayer) {
//            countSendCall++;
//            System.out.println(currentPlayer + " - is moving: " + move);
//        }
//
//        private Move getNextMove(int player, boolean remove) {
//            ArrayList<Move> currentMoves = getMovesForPlayer(player);
//
//            if (currentMoves.size() == 0) {
//                return null;
//            }
//
//            Move currentMove = currentMoves.get(0);
//            if (remove) {
//                currentMoves.remove(0);
//            }
//            return currentMove;
//        }
//
//        private void putNextMove(int player, Move move) {
//            ArrayList<Move> moves = getMovesForPlayer(player);
//            moves.add(0, move);
//        }
//
//        private ArrayList<Move> getMovesForPlayer(int player) {
//            ArrayList<Move> currentMoves = null;
//            switch (player) {
//                case Board.FIRST_PLAYER:
//                    currentMoves = movesPlayerOne;
//                    break;
//                case Board.SECOND_PLAYER:
//                    currentMoves = movesPlayerTwo;
//                    break;
//                case Board.THIRD_PLAYER:
//                    currentMoves = movesPlayerThree;
//                    break;
//            }
//            return currentMoves;
//        }
//
//        void setMyPlayerNumber(int myPlayerNumber) {
//            this.myPlayerNumber = myPlayerNumber;
//        }
//
//        void setMovesAndPlayers(Move[] one, Move[] two, Move[] three) {
//            this.movesPlayerOne = new ArrayList<>(Arrays.asList(one));
//            this.movesPlayerTwo = new ArrayList<>(Arrays.asList(two));
//            this.movesPlayerThree = new ArrayList<>(Arrays.asList(three));
//
//            this.countSendCall = 0;
//            this.countReceiveCall = 0;
//        }
//
//        String getStats() {
//            StringBuilder builder = new StringBuilder();
//            builder.append("moves left:")
//                    .append(movesPlayerOne.size()).append(",")
//                    .append(movesPlayerTwo.size()).append(",")
//                    .append(movesPlayerThree.size())
//                    .append("; receive called:")
//                    .append(countReceiveCall)
//                    .append("; send called:")
//                    .append(countSendCall);
//            return builder.toString();
//        }
//    }
}
