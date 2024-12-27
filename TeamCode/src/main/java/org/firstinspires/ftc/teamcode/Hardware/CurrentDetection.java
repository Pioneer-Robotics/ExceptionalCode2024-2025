package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Utils;
import org.slf4j.helpers.Util;

public class CurrentDetection implements Runnable {
    private volatile double current;
    private final double maxCurrent;
    private final DcMotorEx motor;
    private boolean shutDown;
    public CurrentDetection(DcMotorEx motor, double maxCurrent) {
        this.motor = motor;
        // Just in case
        this.current = Float.POSITIVE_INFINITY;
        this.maxCurrent = maxCurrent;
        this.shutDown = false;
    }

    public CurrentDetection(DcMotorEx motor) {
        this.motor = motor;
        this.current = Float.POSITIVE_INFINITY;
        this.maxCurrent = Config.defaultMaxCurrent;
        this.shutDown = false;
    }

    public void run() {
        while (!shutDown || Bot.opMode.isStopRequested()) {
            current = motor.getCurrent(CurrentUnit.MILLIAMPS);
            if (current > maxCurrent) {
                Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
                Bot.opMode.gamepad1.rumble(1000);
                Bot.opMode.gamepad2.rumble(1000);
            }
        }
    }

    public double getCurrent() {
        return current;
    }

    public void stop() {
        shutDown = true;
    }
}

class MaxCurrentException extends Exception {
    public MaxCurrentException(String message) {
        super(message);
    }
}
