package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
     * @param opMode used to check if opMode is still active
     */
    public static void sleep(float time, LinearOpMode opMode) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.seconds() < time && opMode.opModeIsActive()) {
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
        number = Math.max(limit, number);
        return number;
    }
}
