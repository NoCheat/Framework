package at.nocheat.framework.time.monotonic;

/**
 * Monotonic clock based on System.currentTimeMillis(). Not thread-safe.
 * 
 * @author mc_dev
 * 
 */
public class MonotonicMillisClock extends MonotonicAbstractClock {
    
    @Override
    protected long fetchClock() {
        return System.currentTimeMillis();
    }
    
}
