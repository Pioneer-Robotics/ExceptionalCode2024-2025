package org.firstinspires.ftc.teamcode.Helpers;

/**
 * Class used to help tract and count ticks.
 * One tick is one loop in the run loop
 */
public class Ticker {
    private long ticks;

    public Ticker(long startValue) {
        ticks = startValue;
    }

    /**
     * Increment tick counter
     * Should be placed at the end of the run loop
     */
    public void tick() {ticks++;}

    /**
     * Set the tick counter to a specific value
     * @param newTicks new tick value
     */
    public void setTicks(long newTicks) {ticks = newTicks;}

    /**
     * Get the current ticks
     * @return ticks
     */
    public long getTicks() {return ticks;}
}
