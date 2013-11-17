package at.nocheat.framework.time.monotonic;

/**
 * Basic implementation of increasing a counter for a not necessarily monotonic
 * underlying clock, fetched with fetchClock(). Not thread-safe.
 * 
 */
public abstract class MonotonicAbstractClock implements MonotonicClock {
    
    private long clock = 0;
    
    private long lastFetch = fetchClock();
    
    protected abstract long fetchClock();
    
    @Override
    public long clock() {
        final long fetch = fetchClock();
        final long diff = fetch - this.lastFetch;
        if (diff > 0) {
            this.clock += diff;
        }
        this.lastFetch = fetch;
        return this.clock;
    }
    
    @Override
    public void reset() {
        this.clock = 0;
        this.lastFetch = fetchClock();
    }
    
}
