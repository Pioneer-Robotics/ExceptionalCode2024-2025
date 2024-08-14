package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;

import java.util.Random;

public class Utils {
    /**
     * Turns float to boolean where <0.5 is false and >0.5 is true.
     * Used with gamepad triggers that return float value.
     *
     * @param floatInput range [0,1]
     * @return boolean
     */
    public static boolean floatToBool(float floatInput) {
        return floatInput > 0.5;
    }

    /**
     * Waits for a specified amount of time using ElapsedTime
     *
     * @param time   float time to wait in seconds
     */
    public static void sleep(double time) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.seconds() < time && Bot.opMode.opModeIsActive()) {
        } // Wait for seconds
    }

    /**
     * Increments a double by value and makes sure it doesn't go over limit
     *
     * @param number number to increment
     * @param value  value to increment by
     * @param limit  maximum value of number
     * @return new number
     */
    public static double increment(double number, double value, double limit) {
        number += value;
        number = (double) Math.round(number * 1000) / 1000; // Account for floating point error
        number = Math.min(limit, number);
        return number;
    }

    /**
     * Decrements a double by value and makes sure it doesn't go under limit
     *
     * @param number number to decrement
     * @param value  value to decrement by
     * @param limit  minimum value of number
     * @return new number
     */
    public static double decrement(double number, double value, double limit) {
        number -= value;
        number = (double) Math.round(number * 1000) / 1000; // Account for floating point error
        number = Math.max(limit, number);
        return number;
    }

    /**
     * Generates a random integer from 0 to maxRange
     *
     * @param maxRange range of possibilities from 0 to n (non-inclusive)
     * @return a random number
     */
    public static int randNum(int maxRange) {
        Random random = new Random();
        return(random.nextInt(maxRange));
    }
}
