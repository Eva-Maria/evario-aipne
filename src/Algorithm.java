import lenz.htw.aipne.Move;

/**
 * Created by eve on 5/9/16.
 */
public interface Algorithm {
    Move getNextMove(long timeMillis);
}
