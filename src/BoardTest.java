import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by eve on 4/19/16.
 */
public class BoardTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testToString() {
        Board board = new Board();
        System.out.println(board.toString());
    }

}