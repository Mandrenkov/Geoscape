package util;

import core.Logger;

/**
 * The Progress class tracks the progress of a process in a thread-safe way.
 */
public class Progress {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a new Progress with the given message format, milestone interval,
     * and capacity.
     * 
     * @param format   The format of the logged messages.
     *                 This format must only contain one integer substitution.
     * @param interval The difference in progress between consecutive progress messages.
     * @param capacity The number of expected iterations in the tracked process.
     */
    public Progress(String format, int interval, int capacity) {
        this.format = format;
        this.interval = interval;
        this.capacity = capacity;
        
        this.counter = 0;
        this.milestone = interval;
    }

    /**
     * Increments the number of iterations performed by the tracked process.
     */
    public synchronized void increment() {
        ++this.counter;
    }

    /**
     * Logs a message if a new progress milestone has been reached.
     */
    public synchronized void display() {
        int done = 100*this.counter/this.capacity;
        if (done >= this.milestone) {
            Logger.info(1, this.format, this.milestone);
            this.milestone = Math.min(100, this.milestone + this.interval);
        }
    }

    // Private members
    // -------------------------------------------------------------------------    

    /**
     * The maximum value of the process counter.
     */
    int capacity;

    /**
     * The number of iterations into the tracked process.
     */
    int counter;
    
    /**
     * The format of the displayed INFO message.
     */
    String format;

    /**
     * The minimum difference in progress between two consecutive display messages.
     */
    int interval;
    
    /**
     * The current progress milestone.
     */
    int milestone;
}