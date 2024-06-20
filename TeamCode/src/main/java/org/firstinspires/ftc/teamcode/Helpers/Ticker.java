package org.firstinspires.ftc.teamcode.Helpers;

public class Ticker {
    private long ticks;

    public Ticker(long startValue) {
        ticks = startValue;
    }

    public void tick() {ticks++;}

    public void setTicks(long newTicks) {ticks = newTicks;}

    public long getTicks() {return ticks;}
}
