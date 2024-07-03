package org.firstinspires.ftc.teamcode.Helpers;

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
}
